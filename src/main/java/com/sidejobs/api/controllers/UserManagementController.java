package com.sidejobs.api.controllers;

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
import com.sidejobs.events.OnRegistrationCompleteEvent;

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
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public UserManagementController(UsersRepository usersRepository, VerificationsRepository verificationsRepository)
	{
		this.usersRepository = usersRepository;
		this.verificationsRepository = verificationsRepository;
	}
	
	@RequestMapping(value="/verification/{token}", method=RequestMethod.GET)
	public ResponseWrapper<VerificationResponse> getTokenVerificationStatus(@PathVariable String token)
	{
		ResponseWrapper<VerificationResponse> result = new ResponseWrapper<VerificationResponse>(ResponseStatus.Failure);
		Verification veri = this.verificationsRepository.findVerificationByToken(token);
		
		if(veri == null)
			return result;
		
		result =  new ResponseWrapper<VerificationResponse>(ResponseStatus.Success,new VerificationResponse(ResponseCodes.USER_REGISTRATION_SUCCESSFUL));
	
		return result;
	}
	
	@RequestMapping(value="/register/worker", method=RequestMethod.GET)
	public ResponseWrapper<RegistrationResponse> registerStudent(
			HttpServletRequest request,
			@RequestParam("email")String email,
			@RequestParam("firstname")String firstName,
			@RequestParam("lastname")String lastName,
			@RequestParam("password")String password

			) throws MessagingException{
		
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
		
		String server = env.getProperty("sidejobs.web.address"); 
		
		MailClientUtil maiClient = new MailClientUtil(mailSender,u,server);
		String token = maiClient.sendRegistrationConfirmation();
		
		verificationsRepository.insertVerificationToken(token, u.getId(), "New Registration");
			
		return response;

		
	}
	
	   private void confirmRegistration(User user, String url) {
	        String token = UUID.randomUUID().toString();
	        System.out.println("Sending mail");
	        String recipientAddress = user.getEmail();
	        String subject = "Registration Confirmation";
	        String confirmationUrl 
	          = url + "/regitrationConfirm.html?token=" + token;
	         
	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(recipientAddress);
	        email.setSubject(subject);
	        email.setText(" rn" + "http://localhost:8080" + confirmationUrl);
	        mailSender.send(email);
	        System.out.println("Mail sent");
	    }

}
