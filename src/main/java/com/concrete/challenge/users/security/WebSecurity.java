package com.concrete.challenge.users.security;

import static com.concrete.challenge.users.security.SecurityConstants.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.concrete.challenge.users.UserService;
import com.concrete.challenge.users.filters.ExceptionHandlerFilter;
import com.concrete.challenge.users.filters.JWTAuthenticationFilter;
import com.concrete.challenge.users.filters.JWTAuthorizationFilter;

@Profile("!SECURITY_MOCK") // If we don't use the SECURITY_MOCK profile, we use this bean!
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CustomAuthenticationProvider authProvider;
    @Autowired
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
    
    private MyAuthenticationFailureHandler failureHandler;
    
    @Autowired
    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, CustomAuthenticationProvider authProvider,
    		MyAuthenticationFailureHandler failureHandler) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authProvider = authProvider;
        this.failureHandler = failureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
        	.authorizeRequests()
        	.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
        	.anyRequest().authenticated()
        	.and()
        	.httpBasic()
	        .authenticationEntryPoint(authenticationEntryPoint);
	    
        // os filtros devem ser configurados separados do authorizeRequests()
	    http.addFilter(new JWTAuthenticationFilter(authenticationManager(), userService, failureHandler))
	        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
	    	.addFilterBefore(new ExceptionHandlerFilter(), JWTAuthenticationFilter.class);
        	
	    	//
	        	
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authProvider);
    }
    

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}