package ru.skypro.homework.utils;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Service
public class UserMapper {

    public static UserEntity toEntity(Register dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.getUsername().toLowerCase());
        userEntity.setPhone(dto.getPhone());
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setRole(dto.getRole());
        return userEntity;
    }
}
