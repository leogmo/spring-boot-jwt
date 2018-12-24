package com.concrete.challenge.users;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.exceptions.UserExistsException;
import com.concrete.challenge.users.repositories.AppUserRepository;
import com.concrete.challenge.users.security.TokenJWT;

@Service
public class UserService{
	@Autowired
    private AppUserRepository appUserRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(AppUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public AppUser create(AppUser user) throws UserExistsException{
    	AppUser emailExists = appUserRepository.findByEmail(user.getEmail());
		if (emailExists != null) {
			throw new UserExistsException(Constants.EMAIL_EXISTS);
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Date data = new Date();
		user.setCreated(data);
		user.setLastLogin(data);
		user.setModified(data);
		user.setToken(TokenJWT.createToken(user.getEmail()));
		return appUserRepository.save(user);
    }
    
    public void save(AppUser user) {
    	appUserRepository.save(user);
    }
    
    public Optional<AppUser> findById(Long id){
    	return appUserRepository.findById(id);
    }
}