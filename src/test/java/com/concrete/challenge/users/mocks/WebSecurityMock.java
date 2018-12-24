package com.concrete.challenge.users.mocks;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.concrete.challenge.users.UserService;
import com.concrete.challenge.users.filters.ExceptionHandlerFilter;
import com.concrete.challenge.users.filters.JWTAuthenticationFilter;
import com.concrete.challenge.users.filters.JWTAuthorizationFilter;
import com.concrete.challenge.users.security.MyAuthenticationFailureHandler;
import com.concrete.challenge.users.security.MyBasicAuthenticationEntryPoint;

@Profile("SECURITY_MOCK") // If we use the SECURITY_MOCK profile, we use this bean!
@EnableWebSecurity
public class WebSecurityMock extends WebSecurityConfigurerAdapter {
	@Autowired
    private CustomAuthenticationProviderMock authProvider;
    @Autowired
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private UserService userService;
    @Autowired
    private MyAuthenticationFailureHandler failureHandler;
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
        	.authenticationProvider(authProvider)
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