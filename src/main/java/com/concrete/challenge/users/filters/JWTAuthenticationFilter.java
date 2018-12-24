package com.concrete.challenge.users.filters;

import static com.concrete.challenge.users.security.SecurityConstants.HEADER_STRING;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.concrete.challenge.users.Constants;
import com.concrete.challenge.users.ErrorMessage;
import com.concrete.challenge.users.UserService;
import com.concrete.challenge.users.entities.AppUser;
import com.concrete.challenge.users.security.MyAuthenticationFailureHandler;
import com.concrete.challenge.users.security.TokenJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserService userDetailsService;
    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userDetailsService, MyAuthenticationFailureHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AppUser creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = TokenJWT.createToken(((AppUser)auth.getPrincipal()).getEmail());
        ObjectMapper mapper = new ObjectMapper();
        String msg = "";
        
        Date data = new Date();
        AppUser userFound = (AppUser)auth.getPrincipal();
        if (userFound.getId() == -1) {
        	msg = mapper.writeValueAsString(new ErrorMessage(userFound.getName()));
        	res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
	        userFound.setToken(token);
	        userFound.setLastLogin(data);
	        userFound.setModified(data);
	        userDetailsService.save(userFound);
	        msg = mapper.writeValueAsString(auth.getPrincipal());
	        res.setStatus(HttpServletResponse.SC_OK);
        }
        res.addHeader(HEADER_STRING, token);
        res.setContentType(Constants.CONTENT_TYPE_JSON);
        PrintWriter out = res.getWriter();
        out.print(msg);
        out.flush();
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}