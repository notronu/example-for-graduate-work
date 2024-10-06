package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.PasswordIsNotCorrectException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.UserMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    @Value("${path.to.user.images}")
    private String imagePath;
    @Value("${path.to.default.user.image}")
    private String pathToDefaultUserImage;


    @Override
    public void updatePassword(NewPassword dto, String username) throws PasswordIsNotCorrectException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        if (encoder.matches(dto.getCurrentPassword(), userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(dto.getNewPassword()));
            userRepository.save(userEntity);
        } else {
            throw new PasswordIsNotCorrectException();
        }
    }
    @Override
    public byte[] getImage(String username) {
        try {
            UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username is not found"));
            if (userEntity.getImage() == null) {
                userEntity.setImage(pathToDefaultUserImage);
            }
            return Files.readAllBytes(Path.of(userEntity.getImage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public User getInfoAboutMe(String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return UserMapper.toDto(userEntity);
    }
    @Override
    public UpdateUser updateInfoAboutMe(String username, UpdateUser dto) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setPhone(dto.getPhone());
        userRepository.save(userEntity);
        return dto;
    }
    @Override
    public void updateMyImage(String username, MultipartFile file) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        uploadImage(userEntity, file);
    }
    @Override
    public UserEntity registerUser(Register dto) {
        if (userRepository.findByEmail(dto.getUsername()).isPresent()) {
            throw new UserAlreadyRegisteredException(dto.getUsername());
        } else {
            UserEntity userEntity = userMapper.toEntity(dto);
            userEntity.setPassword(encoder.encode(dto.getPassword()));
            return userRepository.save(userEntity);
        }
    }
    public void uploadImage(UserEntity userEntity, MultipartFile file) throws IOException {
        Path path = Path.of(imagePath, userEntity.getEmail() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));


        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);

        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
            userEntity.setImage(path.toString());
            userRepository.save(userEntity);
        }
    }
}