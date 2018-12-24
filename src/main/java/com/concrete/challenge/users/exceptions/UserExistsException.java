package com.concrete.challenge.users.exceptions;

public class UserExistsException extends Exception{
	private static final long serialVersionUID = -3422397088474906029L;

	public UserExistsException(String errorMessage) {
        super(errorMessage);
    }
}
