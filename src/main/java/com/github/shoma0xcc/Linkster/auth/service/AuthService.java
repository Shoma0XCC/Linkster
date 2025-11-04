package com.github.shoma0xcc.Linkster.auth.service;

import com.github.shoma0xcc.Linkster.auth.repository.AuthRepository;
import com.github.shoma0xcc.Linkster.auth.user.UserDetailsImpl;
import com.github.shoma0xcc.Linkster.user.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;



@Service
public class AuthService implements UserDetailsService {
    private final AuthRepository repository;

    @Autowired
    public AuthService(AuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = repository.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsImpl(user.get());
    }
}
