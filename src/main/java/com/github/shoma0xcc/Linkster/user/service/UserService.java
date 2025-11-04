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

    @Transactional
    public String follow(Long subscriberId, Long followerId){
        if (subscriberId.equals(followerId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }
        UserEntity subscriber = repository.findById(subscriberId).orElseThrow();
        UserEntity follower   = repository.findById(followerId).orElseThrow();
        if (follower.getSubscriptions().contains(subscriber)) {
            return "Already following " + subscriber.getUsername();
        }
        follower.getSubscriptions().add(subscriber);
        subscriber.getFollowers().add(follower);
        return "You follow to " + follower.getUsername();
    }

    @Transactional
    public String unFollow(Long subscriberId, Long followerId){
        if (subscriberId.equals(followerId)) return "Nothing to do";
        UserEntity subscriber = repository.findById(subscriberId).orElseThrow();
        UserEntity follower   = repository.findById(followerId).orElseThrow();
        boolean removed = follower.getSubscriptions().remove(subscriber);
        if (removed) subscriber.getFollowers().remove(follower);
        return removed ? "Unfollowed " + follower.getUsername() : "You were not following";
    }

    @Transactional
    public Page<UserDto> listFollower(Long id, int page, int size, String sort){
        Sort sorts = Sort.by(
                (sort != null && sort.contains(","))
                        ? Sort.Order.by(sort.split(",")[0]).with(
                        "desc".equalsIgnoreCase(sort.split(",")[1]) ? Sort.Direction.DESC : Sort.Direction.ASC
                )
                        : Sort.Order.asc(sort == null ? "username" : sort)
        );
        Pageable pageable = PageRequest.of(page, size, sorts);
        return repository.findAllByFollowers_Id(id, pageable).map(UserMapper::toDto);
    }

    @Transactional
    public Page<UserDto> listSubscriber(Long id, int page, int size, String sort){
        Sort sorts = Sort.by(
                (sort != null && sort.contains(","))
                        ? Sort.Order.by(sort.split(",")[0]).with(
                        "desc".equalsIgnoreCase(sort.split(",")[1]) ? Sort.Direction.DESC : Sort.Direction.ASC
                )
                        : Sort.Order.asc(sort == null ? "username" : sort)
        );
        Pageable pageable = PageRequest.of(page, size, sorts);
        return repository.findAllBySubscriptions_Id(id, pageable).map(UserMapper::toDto);
    }

}
