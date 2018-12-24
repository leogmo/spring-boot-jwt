package com.concrete.challenge.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorMessage {
	public String mensagem;
	
	public ErrorMessage(String msg) {
		this.mensagem = msg;
	}
		
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
			return Constants.ERROR_MESSAGE_TO_JSON_CONVERT_ERROR;
		}
	}
}
