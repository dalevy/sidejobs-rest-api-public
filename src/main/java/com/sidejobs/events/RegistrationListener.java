package com.sidejobs.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.sidejobs.api.entities.User;

@Lazy(false)
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	  
	    @Autowired
	    private MessageSource messages;
	  
	    @Autowired
	    private JavaMailSender mailSender;
	 
	    @Override
	    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
	    	System.out.println("Listener received new application event");
	        this.confirmRegistration(event);
	    }
	 
	    private void confirmRegistration(OnRegistrationCompleteEvent event) {
	        User user = event.getUser();
	        String token = UUID.randomUUID().toString();
	         
	        String recipientAddress = user.getEmail();
	        String subject = "Registration Confirmation";
	        String confirmationUrl 
	          = event.getAppUrl() + "/regitrationConfirm.html?token=" + token;
	        String message = messages.getMessage("message.regSucc", null, event.getLocale());
	         
	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(recipientAddress);
	        email.setSubject(subject);
	        email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
	        mailSender.send(email);
	    }
}
