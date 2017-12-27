package com.sidejobs.api.common;

public enum ResponseCodes {

	SUCCESS,
	NOT_FOUND,
	FAILURE,
	USER_REGISTRATION_SUCCESSFUL,
	USER_DOESNT_EXIST,
	USER_EXISTS;
	
	public static String getResponseMessage(ResponseCodes code)
	{
		String message = "";
		switch(code)
		{

			case SUCCESS:
				message = "The server request was successful.";
				break;
			case NOT_FOUND:
				message = "The server request was not successful - resource not found.";
				break;
			case FAILURE:
				message = "The server request has failed.";
				break;
			case USER_REGISTRATION_SUCCESSFUL:
				message = "The user registration was successful.";
				break;
			case USER_EXISTS:
				message = "The registration request has failed - user exists";
				break;
			case USER_DOESNT_EXIST:
				message = "The user does not exist";
				break;

		}
		
		return message;
	}

}
