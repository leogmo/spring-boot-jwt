package com.concrete.challenge.users;

import java.util.Calendar;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.exceptions.UserExistsException;
import com.concrete.challenge.users.security.SecurityConstants;

@RestController
@RequestMapping(Constants.USERS_PATH)
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(Constants.SIGN_UP_PATH)
	public ResponseEntity signUp(@RequestBody AppUser user) {
		try {
			AppUser saved = userService.create(user);
			return new ResponseEntity<AppUser>(saved, HttpStatus.CREATED);
		} catch (UserExistsException e) {
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(Constants.PROFILE_PATH)
	public ResponseEntity getProfile(@PathVariable Long id, HttpServletRequest request) {
		Optional<AppUser> user = userService.findById(id);
		if (user.isPresent()) {
			if (user.get().getToken().equals(request.getHeader(SecurityConstants.HEADER_STRING))) {
				Calendar dataInicial = Calendar.getInstance();
				dataInicial.setTime(user.get().getLastLogin());
				long diferenca = (System.currentTimeMillis() - dataInicial.getTimeInMillis())  / (60 * 1000);
				if (diferenca > 30) {
					return new ResponseEntity<ErrorMessage>(new ErrorMessage(Constants.INVALID_SESSION), HttpStatus.UNAUTHORIZED);
				}
				return new ResponseEntity<AppUser>(user.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<ErrorMessage>(new ErrorMessage(Constants.UNAUTHORIZED), HttpStatus.UNAUTHORIZED); 
			}
		} 
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(Constants.NOT_FOUND), HttpStatus.NOT_FOUND);
	}	
}
