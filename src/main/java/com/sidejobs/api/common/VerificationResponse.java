package com.sidejobs.api.common;

import java.io.Serializable;

import com.sidejobs.api.entities.User;

public class VerificationResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private ResponseCodes code;
	private User user;
	
	public VerificationResponse() {}
	
	public VerificationResponse(ResponseCodes code)
	{
		this.code = code;
	}
	
	public VerificationResponse(ResponseCodes code, User user)
	{
		this(code);
		this.user = user;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseCodes getCode() {
		return code;
	}

	public void setCode(ResponseCodes code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
