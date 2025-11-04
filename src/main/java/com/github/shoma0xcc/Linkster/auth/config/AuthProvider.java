package com.github.shoma0xcc.Linkster.auth.config;

import com.github.shoma0xcc.Linkster.auth.service.AuthService;
import com.github.shoma0xcc.Linkster.auth.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final AuthService authService;

    @Autowired
    public AuthProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetailsImpl userDetail = authService.loadUserByUsername(username);

        String password = authentication.getCredentials().toString();

        if (!password.equals(userDetail.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        return new UsernamePasswordAuthenticationToken(userDetail, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
