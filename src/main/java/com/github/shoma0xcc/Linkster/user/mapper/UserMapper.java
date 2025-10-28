package com.github.shoma0xcc.Linkster.user.mapper;
import com.github.shoma0xcc.Linkster.user.dto.*;
import com.github.shoma0xcc.Linkster.user.models.UserEntity;


public class UserMapper {
    public static UserDto toDto(UserEntity m) {
        return new UserDto(m.getId(), m.getUsername(), m.getEmail(), m.getFirstName(), m.getLastName(), m.getStatus());
    }
    public static UserEntity fromCreate(UserCreateRequest r) {
        return new UserEntity(r.username(), r.email(), r.firstName(), r.lastName(), r.status());
    }
    public static void applyUpdate(UserUpdateRequest r, UserEntity m) {
        m.setUsername(r.username());
        m.setEmail(r.email());
        m.setFirstName(r.firstName());
        m.setLastName(r.lastName());
        m.setStatus(r.status());
    }
    public static void applyPatch(UserPatchRequest r, UserEntity m) {
        if (r.username() != null) m.setUsername(r.username());
        if (r.email()    != null) m.setEmail(r.email());
        if (r.firstName()!= null) m.setFirstName(r.firstName());
        if (r.lastName() != null) m.setLastName(r.lastName());
        if (r.status()   != null) m.setStatus(r.status());
    }
}
