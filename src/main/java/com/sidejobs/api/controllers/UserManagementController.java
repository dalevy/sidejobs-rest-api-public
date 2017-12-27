package com.sidejobs.api.controllers;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

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
	
	@RequestMapping(value="/verification/{token}", method=RequestMethod.GET)
	public ResponseWrapper<VerificationResponse> getTokenVerificationStatus(@PathVariable String token)
	{
		ResponseWrapper<VerificationResponse> result = new ResponseWrapper<VerificationResponse>(ResponseStatus.Failure);
		Verification veri = this.verificationsRepository.findVerificationByToken(token);
		
		System.out.println("Verifying token");
		if(veri == null)
			return result;
		
		if(!veri.isTokenValid(new Timestamp(System.currentTimeMillis())))
		{
			System.out.println("Token is not valid");
			return result;

		}
		
		this.verificationsRepository.closeVerificationToken(token);
		this.usersRepository.setUserAccountVerified(veri.getUserId());
		
		System.out.println("Token verified");
		
		result =  new ResponseWrapper<VerificationResponse>(ResponseStatus.Success,new VerificationResponse(ResponseCodes.USER_REGISTRATION_SUCCESSFUL));
	
		return result;
	}
	
	@RequestMapping(value="/register/worker", method=RequestMethod.POST)
	public ResponseWrapper<RegistrationResponse> registerStudent(
			HttpServletRequest request,
			@RequestParam("email")String email,
			@RequestParam("firstname")String firstName,
			@RequestParam("lastname")String lastName,
			@RequestParam("password")String password

			) throws MessagingException, UnsupportedEncodingException{
		
		ResponseWrapper<RegistrationResponse> response =  null;
		
		System.out.println("Checking if user exists");
		//first check if the user already exists
		Optional<User> existingUser = this.usersRepository.findUserByEmail(email);
		
		if(existingUser.isPresent())
		{
			return response = new ResponseWrapper<RegistrationResponse>(ResponseStatus.Failure,new RegistrationResponse(ResponseCodes.USER_EXISTS));
		}
		
		//create a new password hash
		System.out.println("Generating password hash...");
				
		BCryptPasswordUtil passUtil = new BCryptPasswordUtil();
		String passHash = passUtil.hash(password, 11);
		
		int userIdLength = Integer.parseInt(env.getProperty("sidejobs.user.id.length"));
		String id = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0,userIdLength);

		System.out.println("id :"+id);
				
		if(this.usersRepository == null)
			System.out.println("Users is null");
		
		System.out.println("id: "+id);
		System.out.println("firstName: "+firstName);
		System.out.println("lastName: "+lastName);
		System.out.println("email: "+email);
		System.out.println("passHash: "+passHash);
		
		this.usersRepository.registerStudentUser(id, firstName, lastName, email, passHash, "");
		
		RegistrationResponse regResponse = new RegistrationResponse(ResponseCodes.USER_REGISTRATION_SUCCESSFUL);
		regResponse.setUserId(id);
		response = new ResponseWrapper<RegistrationResponse>(ResponseStatus.Success,regResponse);
				
		//send verification email
		User u = this.usersRepository.findUserById(id);
		
		String server = env.getProperty("sidejobs.web.token.path"); 
		
		MailClientUtil maiClient = new MailClientUtil(mailSender,u,server);
		String token = maiClient.sendRegistrationConfirmation();
		
		verificationsRepository.insertVerificationToken(token, u.getId(), "New Registration");
			
		return response;

		
	}


}
