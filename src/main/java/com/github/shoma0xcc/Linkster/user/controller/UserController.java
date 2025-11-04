package com.github.shoma0xcc.Linkster.user.controller;

import com.github.shoma0xcc.Linkster.auth.user.UserDetailsImpl;
import com.github.shoma0xcc.Linkster.user.dto.*;
import com.github.shoma0xcc.Linkster.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateRequest req) {
        var dto = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }


//    @GetMapping("/{id}")
//    public UserDto followToUser(@PathVariable Long id){
//        return service.follow();
//    }


    @GetMapping
    public Page<UserDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        return service.list(page, size, sort);
    }

    @GetMapping("/{id}/followers")
    public Page<UserDto> listFollowers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @PathVariable Long id
    ){
        return service.listFollower(id, page, size, sort);
    }

    @GetMapping("/{id}/subscribe")
    public Page<UserDto> listSubscriber(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @PathVariable Long id
    ){
        return service.listSubscriber(id, page, size, sort);
    }


    @PatchMapping("/follow/{followerId}")
    public String follow(@AuthenticationPrincipal UserDetailsImpl subscriber, @PathVariable Long followerId){
        Long subscriberId = subscriber.getUser().getId();
        return service.follow(subscriberId, followerId);
    }

    @PatchMapping("/un_follow/{followerId}")
    public String unFollow(@AuthenticationPrincipal UserDetailsImpl subscriber, @PathVariable Long followerId){
        Long subscriberId = subscriber.getUser().getId();
        return service.unFollow(subscriberId, followerId);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@PathVariable Long id, @Valid @RequestBody UserPatchRequest req) {
        return service.patch(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }



}
