package ru.skypro.homework.service.impl;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.MyUserDetailsService;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;


/**
 * Сервис для обработки логики авторизации и регистрации пользователей.
 * Реализует методы для входа в систему и регистрации новых пользователей.
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsService myUserDetailService;
    private final PasswordEncoder encoder;


    @Override
    public boolean login(String userName, String password) {
        return true;
    }
    @Override
    public boolean login(Login loginDto) {

        MyUserDetails userDetails = myUserDetailService.loadUserByUsername(loginDto.getUsername());
        return encoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }

}
