package com.concrete.challenge.users;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.entities.Phone;
import com.concrete.challenge.users.exceptions.UserExistsException;
import com.concrete.challenge.users.repositories.AppUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
	
	@MockBean
	AppUserRepository appUserRepository;
	@Autowired
	UserService userService;
	
	private AppUser userExists(String email) {	
		if(email.equals("jose@silva.org")) {
			 AppUser user = new AppUser();
			 user.setEmail("jose@silva.org");
			 user.setId(1);
			 user.setName("Jose da Silva");
			 user.setPassword("hunter2");
			 
			 return user;
		} else {
			return null;
		}
	}
	
	@Test
	public void CreateReturnsValidUser() throws Exception {	
		when(appUserRepository.save(any(AppUser.class))).then(returnsFirstArg());
		when(appUserRepository.findByEmail(any(String.class))).thenReturn(userExists(returnsFirstArg().toString()));
		
		AppUser user = new AppUser();
		 user.setEmail("joao@silva.org");
		 user.setName("João da Silva");
		 user.setPassword("hunter2");
		 user.getPhones().add(new Phone() {
			 {
				 setNumber("987654321");
				 setDdd("21");
			 }
		 });

		 AppUser saved = userService.create(user);
		 
		 assertNotNull(saved.getCreated());
		 assertNotNull(saved.getLastLogin());
		 assertNotNull(saved.getModified());
		 assertNotNull(saved.getToken());
		 assertTrue(new BCryptPasswordEncoder().matches("hunter2", saved.getPassword()));
	}

	@Test(expected = UserExistsException.class)
	public void CreateUserWithExistingEmailThrowsException() throws Exception {	
		when(appUserRepository.save(any(AppUser.class))).then(returnsFirstArg());
		when(appUserRepository.findByEmail("jose@silva.org")).thenReturn(new AppUser());
		
		AppUser user = new AppUser();
		 user.setEmail("jose@silva.org");
		 user.setName("João da Silva");
		 user.setPassword("hunter2");
		 user.getPhones().add(new Phone() {
			 {
				 setNumber("987654321");
				 setDdd("21");
			 }
		 });

		 userService.create(user);		 
	}

	@Test
	public void findByIdReturnsUserWithSameId() throws Exception {	
		AppUser user = new AppUser();
		user.setId(1);
		user.setEmail("jose@silva.org");
		user.setName("João da Silva");
		user.setPassword("hunter2");
		user.getPhones().add(new Phone() {
			 {
				 setNumber("987654321");
				 setDdd("21");
			 }
		 });
		when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
		
		Optional<AppUser> found = userService.findById(1L);
		assertTrue(found.isPresent());
		
		found = userService.findById(2L);
		assertFalse(found.isPresent());

		 userService.create(user);		 
	}
}
