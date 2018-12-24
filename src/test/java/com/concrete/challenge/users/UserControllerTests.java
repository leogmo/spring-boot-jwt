package com.concrete.challenge.users;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.hamcrest.Matchers.notNullValue;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.mocks.RepositoryMock;
import com.concrete.challenge.users.repositories.AppUserRepository;
import com.concrete.challenge.users.security.SecurityConstants;
import com.concrete.challenge.users.security.TokenJWT;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

	@MockBean
	AppUserRepository appUserRepository;
	@Autowired
	UserService userService;
	
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
    UserController userController;
	
	MockMvc mockMvc;
	
	private AppUser userExists(String email) {	
		if(email.equals("jose@silva.org")) {
			 AppUser user = new AppUser();
			 user.setEmail("jose@silva.org");
			 user.setId(1);
			 user.setName("Jose da Silva");
			 user.setPassword("$2a$10$zBvbYZTgvcRwnzq82Zf5KeQVn0P.5Phb2UaStvilpXQ8Q/7uKRH3u");
			 user.setCreated(new Date());
			 user.setLastLogin(new Date());
			 user.setModified(new Date());
			 user.setToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQHNpbHZhLm9yZyIsImV4cCI6MTU0NTUyNDEzMn0.n5wZv58_RSQF0ssBH9LXYiqIJsv4Y28tzvOM5vlUcWq2dRNMDL3yO54FiDS8RFQ8j3cOkNS2PcDet250w2giiA");
			 
			 return user;
		} else {
			return null;
		}
	}
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.userController).addFilters(springSecurityFilterChain).build();
	}
	
	@Test
	public void signUpReturnsValidUser() throws Exception {
		when(appUserRepository.save(any(AppUser.class))).then(returnsFirstArg());
		when(appUserRepository.findByEmail(any(String.class))).thenReturn(userExists(returnsFirstArg().toString()));
		String body = "{\r\n" + 
				"        \"name\": \"João da Silva\",\r\n" + 
				"        \"email\": \"joao@silva.org\",\r\n" + 
				"        \"password\": \"hunter2\",\r\n" + 
				"        \"phones\": [\r\n" + 
				"            {\r\n" + 
				"                \"number\": \"987654321\",\r\n" + 
				"                \"ddd\": \"21\"\r\n" + 
				"            }\r\n" + 
				"        ]\r\n" + 
				"    }";
		
		mockMvc.perform(post(Constants.USERS_SIGNUP_PATH).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.created", notNullValue()))
        .andExpect(jsonPath("$.lastLogin", notNullValue()))
        .andExpect(jsonPath("$.modified", notNullValue()))
        .andExpect(jsonPath("$.token", notNullValue()));
	}
	
	@Test
	public void signUpWithExistingEmailReturnsEmailExistsError() throws Exception {
		when(appUserRepository.save(any(AppUser.class))).then(returnsFirstArg());
		when(appUserRepository.findByEmail("jose@silva.org")).thenReturn(new AppUser());
		String body = "{\r\n" + 
				"        \"name\": \"João da Silva\",\r\n" + 
				"        \"email\": \"jose@silva.org\",\r\n" + 
				"        \"password\": \"hunter2\",\r\n" + 
				"        \"phones\": [\r\n" + 
				"            {\r\n" + 
				"                \"number\": \"987654321\",\r\n" + 
				"                \"ddd\": \"21\"\r\n" + 
				"            }\r\n" + 
				"        ]\r\n" + 
				"    }";
		
		mockMvc.perform(post(Constants.USERS_SIGNUP_PATH).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.EMAIL_EXISTS)));
	}
	
	@Test
	public void successLoginReturnsValidUser() throws Exception {
		String body = "{\r\n" + 
				"	\"email\": \"jose@silva.org\",\r\n" + 
				"	\"password\": \"hunter2\"\r\n" + 
				"}";
		
		mockMvc.perform(post(Constants.LOGIN_PATH).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.created", notNullValue()))
        .andExpect(jsonPath("$.lastLogin", notNullValue()))
        .andExpect(jsonPath("$.modified", notNullValue()))
        .andExpect(jsonPath("$.token", notNullValue()));
	}
	
	@Test
	public void LoginWithNonExistentEmailReturnsInvalidUserOrPassword() throws Exception {
		String body = "{\r\n" + 
				"	\"email\": \"antonio@silva.org\",\r\n" + 
				"	\"password\": \"hunter2\"\r\n" + 
				"}";
		
		mockMvc.perform(post(Constants.LOGIN_PATH).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.INVALID_USERNAME_PASSWORD)));
	}
	
	@Test
	public void LoginWithWrongPasswordReturnsInvalidUserOrPassword() throws Exception {
		String body = "{\r\n" + 
				"	\"email\": \"jose@silva.org\",\r\n" + 
				"	\"password\": \"12345\"\r\n" + 
				"}";
		
		mockMvc.perform(post(Constants.LOGIN_PATH).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.INVALID_USERNAME_PASSWORD)));
	}
	
	@Test
	public void getProfileWithoutTokenReturnsUnauthorized() throws Exception {
		mockMvc.perform(get(Constants.USERS_PROFILE_PATH+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header(SecurityConstants.HEADER_STRING, ""))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.UNAUTHORIZED)));
	}
	
	@Test
	public void getProfileWithDifferentTokenReturnsUnauthorized() throws Exception {
		when(appUserRepository.findById(1L)).thenReturn(Optional.of(RepositoryMock.users.get(0)));
		String firstToken = TokenJWT.createToken("joao@silva.org");
		Thread.sleep(5000);
		String secondToken = TokenJWT.createToken("joao@silva.org");
		RepositoryMock.users.get(0).setToken(secondToken);
		RepositoryMock.users.get(1).setToken(TokenJWT.createToken("jose@silva.org"));
		mockMvc.perform(post(Constants.USERS_PROFILE_PATH+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header(SecurityConstants.HEADER_STRING, firstToken))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.UNAUTHORIZED)));
	}
	
	@Test
	public void getProfileLastLoginMoreThan30MinutesReturnsInvalidSession() throws Exception {
		when(appUserRepository.findById(1L)).thenReturn(Optional.of(RepositoryMock.users.get(0)));
		String firstToken = TokenJWT.createToken("joao@silva.org");
		RepositoryMock.users.get(0).setToken(firstToken);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -31);
		RepositoryMock.users.get(0).setLastLogin(cal.getTime());
		
		mockMvc.perform(post(Constants.USERS_PROFILE_PATH+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header(SecurityConstants.HEADER_STRING, firstToken))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.mensagem", equalTo(Constants.INVALID_SESSION)));
	}
	
	@Test
	public void getProfileLastLoginLessThan30MinutesReturnsProfile() throws Exception {
		when(appUserRepository.findById(1L)).thenReturn(Optional.of(RepositoryMock.users.get(0)));
		String firstToken = TokenJWT.createToken("joao@silva.org");
		RepositoryMock.users.get(0).setToken(firstToken);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -29);
		RepositoryMock.users.get(0).setLastLogin(cal.getTime());
		
		mockMvc.perform(post(Constants.USERS_PROFILE_PATH+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header(SecurityConstants.HEADER_STRING, firstToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.created", notNullValue()))
        .andExpect(jsonPath("$.lastLogin", notNullValue()))
        .andExpect(jsonPath("$.modified", notNullValue()))
        .andExpect(jsonPath("$.token", notNullValue()))
		.andExpect(jsonPath("$.id", equalTo(1)));
	}
}
