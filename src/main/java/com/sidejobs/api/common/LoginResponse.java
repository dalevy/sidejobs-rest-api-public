package com.sidejobs.api.common;

import java.io.Serializable;

import com.sidejobs.api.entities.User;

public class LoginResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private String message;
	private ResponseCodes code;
	
	public LoginResponse() {}
	
	public LoginResponse(User user)
	{
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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
	
}
