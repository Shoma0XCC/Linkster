package com.github.shoma0xcc.Linkster.auth.config;

import com.github.shoma0xcc.Linkster.auth.core.AuthCore;
import com.github.shoma0xcc.Linkster.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private AuthCore authCore;
    private AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;
        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")){
                jwt = headerAuth.substring(7);
            }
            if (jwt != null){
                try {
                    username = authCore.getNameFromJwt(jwt);

                }
                catch (Exception e){
                    //TODO
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    userDetails = authService.loadUserByUsername(username);
                    auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        }
        catch (Exception e){
            // TODO
        }
        filterChain.doFilter(request, response);
    }
}
