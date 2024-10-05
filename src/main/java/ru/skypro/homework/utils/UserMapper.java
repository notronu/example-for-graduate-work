package ru.skypro.homework.utils;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Service
public class UserMapper {

    public static User toDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setPhone(userEntity.getPhone());
        user.setRole(userEntity.getRole());
        user.setImage("/users/" + userEntity.getEmail() + "/image");
        return user;
    }

    public static UserEntity toEntity(Register dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.getUsername().toLowerCase());
        userEntity.setPhone(dto.getPhone());
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setRole(dto.getRole());
        userEntity.setUsername(dto.getUsername().toLowerCase());
        return userEntity;
    }
}
