package com.github.shoma0xcc.Linkster.auth.controller;


import com.github.shoma0xcc.Linkster.auth.core.AuthCore;
import com.github.shoma0xcc.Linkster.auth.dto.SigninRequest;
import com.github.shoma0xcc.Linkster.auth.dto.SignupRequest;
import com.github.shoma0xcc.Linkster.auth.repository.AuthRepository;
import com.github.shoma0xcc.Linkster.user.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthCore authCore;



    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        if (repository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }
        if (repository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different email");
        }
        UserEntity user = new UserEntity();
        String hashed = passwordEncoder.encode(signupRequest.getPassword());
        user.setLastName(signupRequest.getLastName());
        user.setFirstName(signupRequest.getFirstName());
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hashed);
        repository.save(user);
        return ResponseEntity.ok("Success");

    }

    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SigninRequest signupRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));

        }catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = authCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }


}
