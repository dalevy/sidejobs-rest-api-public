package com.sidejobs.api.controllers;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sidejobs.api.common.LoginResponse;
import com.sidejobs.api.common.RegistrationResponse;
import com.sidejobs.api.common.ResponseCodes;
import com.sidejobs.api.common.ResponseStatus;
import com.sidejobs.api.common.ResponseWrapper;
import com.sidejobs.api.common.VerificationResponse;
import com.sidejobs.api.entities.Specialty;
import com.sidejobs.api.entities.User;
import com.sidejobs.api.entities.Verification;
import com.sidejobs.api.repositories.UsersRepository;
import com.sidejobs.api.repositories.VerificationsRepository;
import com.sidejobs.api.util.BCryptPasswordUtil;
import com.sidejobs.api.util.MailClientUtil;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/admin/users")
public class UserManagementController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final UsersRepository usersRepository;
	private final VerificationsRepository verificationsRepository;
	
	@Autowired
	private Environment env;
	
    @Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	public UserManagementController(UsersRepository usersRepository, VerificationsRepository verificationsRepository)
	{
		this.usersRepository = usersRepository;
		this.verificationsRepository = verificationsRepository;
	}
	
	@RequestMapping(value="/search/user/{id}", method=RequestMethod.GET)
	public ResponseWrapper<User> getUser(@PathVariable("id") String id){
		
		ResponseWrapper<User> result = new ResponseWrapper<User>(ResponseStatus.Failure);
		
		User user = this.usersRepository.findUserById(id);
		
		if(user == null)
			return result;
		
		result =  new ResponseWrapper<User>(ResponseStatus.Success,user);
			
		return result;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseWrapper<LoginResponse> getUserLoginInfo(
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="password", required=true) String password)
	{
		ResponseWrapper<LoginResponse> result = new ResponseWrapper<LoginResponse>(ResponseStatus.Failure);
		
		BCryptPasswordUtil passUtil = new BCryptPasswordUtil();
		
		System.out.println("User email: "+email);
		
		Optional<User> opt = this.usersRepository.findUserByEmail(email);
		User usr = (opt.isPresent()) ? opt.get() : null;
		
		//no such user
		if(usr == null)
			return result;
		
		boolean passValid = passUtil.verifyHash(password, usr.getPassword());
		
		System.out.println("Bcrypt password is : "+passValid);
		//password failure
		if(!passValid){
			this.usersRepository.changeUserPasswordFailureCount(usr.getId(), usr.getPassword_failures()+1);
			//fail password max
			if(usr.getPassword_failures()+1 == Integer.parseInt(env.getProperty("sidejobs.user.password.failure.max")))
			{
				usr.setStatus("Locked");
				this.usersRepository.changeUserAccountStatus(usr.getId(), "Locked");
			}
			return result;
		}
		
		//correct login - reset password fail count
		if(usr.getPassword_failures() > 0)
			this.usersRepository.changeUserPasswordFailureCount(usr.getId(), 0);
		
		result = new ResponseWrapper<LoginResponse>(ResponseStatus.Success, new LoginResponse(usr));
		
		return result;
	}
	
	/***
	 * Verifies a registration email token is valid and then closes the registration token and sets the user status to "Active"
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/verification/{token}", method=RequestMethod.GET)
	public ResponseWrapper<VerificationResponse> getTokenVerificationStatus(@PathVariable String token)
	{
		ResponseWrapper<VerificationResponse> result = new ResponseWrapper<VerificationResponse>(ResponseStatus.Failure);
		Verification veri = this.verificationsRepository.findVerificationByToken(token);
		
		logger.debug("Verifying email registration token: "+token);
		if(veri == null)
			return result;
		
		if(!veri.isTokenValid(new Timestamp(System.currentTimeMillis())))
		{
			logger.debug("Registration token: "+token+" is not valid based on timestamp verification checks");
			return result;

		}
		
		logger.debug("Closing registration token: "+token);
		this.verificationsRepository.closeVerificationToken(token);
		this.usersRepository.setUserAccountVerified(veri.getUserId());
		this.usersRepository.changeUserAccountStatus(veri.getUserId(), "Active");
		User user = this.usersRepository.findUserById(veri.getUserId());
		
		logger.debug("Registration token verified");
		
		result =  new ResponseWrapper<VerificationResponse>(ResponseStatus.Success,new VerificationResponse(ResponseCodes.USER_REGISTRATION_SUCCESSFUL,user));
	
		return result;
	}
	
	/***
	 * Reqister a new user with the role worker, the user will need to verify their account via a confirmation email. 
	 * Until then the account status will remain as "Unverified"
	 * 
	 * @param request
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/register/worker", method=RequestMethod.POST)
	public ResponseWrapper<RegistrationResponse> registerStudent(
			HttpServletRequest request,
			@RequestParam(value ="email", required=true)String email,
			@RequestParam(value ="firstname", required=true)String firstName,
			@RequestParam(value ="lastname", required=true)String lastName,
			@RequestParam(value ="password", required=true)String password
			) throws MessagingException, UnsupportedEncodingException{
		
		logger.debug("Recieved worker registration request with email: "+email+" firstname: "+firstName+" lastname: "+lastName+" password: "+password);
		
		ResponseWrapper<RegistrationResponse> response =  null;
		
		logger.debug("Checking if user with email: "+email+" exists");
		
		//first check if the user already exists
		Optional<User> existingUser = this.usersRepository.findUserByEmail(email);
		
		if(existingUser.isPresent())
			return response = 
				new ResponseWrapper<RegistrationResponse>(ResponseStatus.Failure,new RegistrationResponse(ResponseCodes.USER_EXISTS,null));
			
		//create a new password hash
		logger.debug("Generating password hash for new user with email: "+email);
				
		BCryptPasswordUtil passUtil = new BCryptPasswordUtil();
		String passHash = passUtil.hash(password, 11);
		
		int userIdLength = Integer.parseInt(env.getProperty("sidejobs.user.id.length"));
		String id = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0,userIdLength);
				
		if(this.usersRepository == null)
			logger.info("Users Repository object is null after attempt to register worker with email: "+email);
		
		this.usersRepository.registerStudentUser(id, firstName, lastName, email, passHash, "");
		
		//send verification email
		User u = this.usersRepository.findUserById(id);
		
		logger.debug("User registration successful for user with email: "+email);
		
		RegistrationResponse regResponse = new RegistrationResponse(ResponseCodes.USER_REGISTRATION_SUCCESSFUL,u);
		response = new ResponseWrapper<RegistrationResponse>(ResponseStatus.Success,regResponse);
						
		String server = env.getProperty("sidejobs.web.token.path"); 
		
		MailClientUtil maiClient = new MailClientUtil(mailSender,u,server);
		String token = maiClient.sendRegistrationConfirmation();
		
		logger.debug("Adding verification token to the verifications table");
		this.verificationsRepository.insertVerificationToken(token, u.getId(), "New Registration");
			
		return response;

		
	}


}
