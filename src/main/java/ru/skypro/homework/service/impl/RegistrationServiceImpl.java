package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.RegistrationService;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;


    @Override
    public boolean register(Register register) {
        if(userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        } else {
            userService.registerUser(register);
            return true;
        }
    }
}
