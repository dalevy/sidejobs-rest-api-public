package com.sidejobs.api.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.sidejobs.api.entities.User;

public class MailClientUtil {

	private User user;
	private String path;
	
	private JavaMailSender mailSender;
	
	public MailClientUtil(JavaMailSender mailSender,User user, String path)
	{
		this.user = user;
		this.path = path;
		this.mailSender = mailSender;
	}
	
	public String sendRegistrationConfirmation() throws UnsupportedEncodingException {
	   String token = 
			   UUID.randomUUID()
			   .toString()
			   .toUpperCase()
			   .replaceAll("-", "")+
			   UUID.randomUUID()
			   .toString()
			   .toUpperCase()
			   .replaceAll("-", "")
			   .substring(0,10);
	   
	   System.out.println("Sending mail");
	   
	   String subject = "SideJobs Registration Confirmation";
	   Map<String,String> params = new HashMap<>();
	   params.put("token", token);
	   params.put("user", user.getId());
	   
	   String confirmationUrl = path +"?"+ ParameterStringBuilder.getParamsString(params);
	   
	   String html = "<html><body>Please click the following link to confirm your registration: <a href='"+confirmationUrl+"'>"+confirmationUrl+"</a></body><br/>You can also copy and paste it into your browser</html>";
	   
	   System.out.println("Creating mime message -- email: "+user.getEmail()+" subject: "+subject+" for server: "+path);
	   System.out.println(html);
	   
	   MimeMessagePreparator messagePreparator = mimeMessage -> {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	        messageHelper.setFrom("sample@dolszewski.com");
	        messageHelper.setTo(user.getEmail());
	        messageHelper.setSubject(subject);
	        messageHelper.setText(html,true);
	    };
		   System.out.println("Attempting to send mail with token: "+token);

	   mailSender.send(messagePreparator);
	   
	   System.out.println("Mail sent");
	   
	   return token;
	 }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
