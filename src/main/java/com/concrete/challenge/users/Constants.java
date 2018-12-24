package com.concrete.challenge.users;

public class Constants {
	public static final long ONE_MINUTE_IN_MILLISECONDS = 60_000; 
	public static final long THIRDY_MINUTES_IN_MILLISECONDS = 1_800_000; 
	public static final long ONE_DAY_IN_MILLISECONDS = 86_400_000; 
	
	public static final String UNAUTHORIZED = "Não autorizado";
	public static final String REALM_NAME = "Concrete";
	public static final String INVALID_USERNAME_PASSWORD = "Usuário e/ou Senha inválidos";
	public static final String AUTHENTICATION_FAILED = "Authentication failed";
	public static final String ERROR_MESSAGE_TO_JSON_CONVERT_ERROR = "Erro ao converter ErrorMessage para Json";
	public static final String INVALID_SESSION = "Sessão inválida";
	public static final String NOT_FOUND = "Não encontrado";
	public static final String EMAIL_EXISTS = "E-mail já existente";
	
	public static final String USERS_PATH = "/users";
	public static final String SIGN_UP_PATH = "/sign-up";
	public static final String PROFILE_PATH = "/profile/{id}";
	public static final String USERS_SIGNUP_PATH = USERS_PATH + SIGN_UP_PATH;
	public static final String USERS_PROFILE_PATH = USERS_PATH + "/profile";
	public static final String LOGIN_PATH = "/login";
	
	public static final String ENCODE_UTF8 = "UTF-8";
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final String CONTENT_TYPE_JSON = "application/json";
}
