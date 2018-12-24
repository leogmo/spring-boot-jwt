package com.concrete.challenge.users.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.concrete.challenge.users.Constants;
import com.concrete.challenge.users.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {      
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException, ServletException {

    	ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            mapper.writeValue(response.getWriter(), new ErrorMessage(Constants.INVALID_USERNAME_PASSWORD));
        } else {
        	mapper.writeValue(response.getWriter(), new ErrorMessage(Constants.AUTHENTICATION_FAILED));
        }
    }
}