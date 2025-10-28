package com.github.shoma0xcc.Linkster.user.service;
import com.github.shoma0xcc.Linkster.user.dto.UserCreateRequest;
import com.github.shoma0xcc.Linkster.user.dto.UserDto;
import com.github.shoma0xcc.Linkster.user.dto.UserPatchRequest;
import com.github.shoma0xcc.Linkster.user.dto.UserUpdateRequest;
import com.github.shoma0xcc.Linkster.user.mapper.UserMapper;
import com.github.shoma0xcc.Linkster.user.models.UserEntity;

import com.github.shoma0xcc.Linkster.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public UserDto create(UserCreateRequest req) {
        // примитивная защита от дублей
        if (repository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (repository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already taken");
        }
        UserEntity m = UserMapper.fromCreate(req);
        return UserMapper.toDto(repository.save(m));
    }

    @Transactional
    public UserDto get(Long id) {
        UserEntity m = repository.findById(id).orElseThrow();
        return UserMapper.toDto(m);
    }

    @Transactional()
    public Page<UserDto> list(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return repository.findAll(pageable).map(UserMapper::toDto);
    }

    @Transactional
    public UserDto update(Long id, UserUpdateRequest req) {
        UserEntity m = repository.findById(id).orElseThrow();
        UserMapper.applyUpdate(req, m);
        return UserMapper.toDto(repository.save(m));
    }

    @Transactional
    public UserDto patch(Long id, UserPatchRequest req) {
        UserEntity m = repository.findById(id).orElseThrow();
        UserMapper.applyPatch(req, m);
        return UserMapper.toDto(repository.save(m));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
