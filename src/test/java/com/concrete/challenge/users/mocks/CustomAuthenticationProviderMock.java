package com.concrete.challenge.users.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
public class CustomAuthenticationProviderMock implements AuthenticationProvider {
			
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
  
        String name = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        
        List<AppUser> usersFound = RepositoryMock.users.stream()
        .filter(user -> user.getEmail().equals(name))
        .collect(Collectors.toList());

        AppUser found = usersFound.isEmpty() ? null : usersFound.get(0);
        
        if (found == null) {
        	throw new UsernameNotFoundException(Constants.INVALID_USERNAME_PASSWORD);
        }
        if (!new BCryptPasswordEncoder().matches(password, found.getPassword())) {
        	throw new UsernameNotFoundException(Constants.INVALID_USERNAME_PASSWORD);
        }
        return new UsernamePasswordAuthenticationToken(found, password, new ArrayList<>());
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
