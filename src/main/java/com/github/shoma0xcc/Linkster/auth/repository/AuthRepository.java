package com.github.shoma0xcc.Linkster.auth.repository;

import com.github.shoma0xcc.Linkster.auth.service.AuthService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.shoma0xcc.Linkster.user.models.UserEntity;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
