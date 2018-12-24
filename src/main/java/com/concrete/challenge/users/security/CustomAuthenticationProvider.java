package com.concrete.challenge.users.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.concrete.challenge.users.Constants;
import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.repositories.AppUserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AppUserRepository applicationUserRepository;
		
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
  
        String name = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
         
        AppUser applicationUser = applicationUserRepository.findByEmail(name);

        if (applicationUser == null) {
        	throw new UsernameNotFoundException(Constants.INVALID_USERNAME_PASSWORD);
        }
        if (!new BCryptPasswordEncoder().matches(password, applicationUser.getPassword())) {
        	throw new UsernameNotFoundException(Constants.INVALID_USERNAME_PASSWORD);
        }
        return new UsernamePasswordAuthenticationToken(applicationUser, password, new ArrayList<>());
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
