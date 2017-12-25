package com.sidejobs.api.common;

import java.io.Serializable;

public class RegistrationResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private ResponseCodes code;
	private String userId;
	
	public RegistrationResponse() {}
	
	public RegistrationResponse(ResponseCodes code)
	{
		this.message = ResponseCodes.getResponseMessage(code);
		this.code = code;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	

}
