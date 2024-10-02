package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.MapperUtils;
import ru.skypro.homework.utils.UserMapper;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Реализация сервиса для управления пользователями.
 * Предоставляет методы для обновления пароля, получения информации о пользователе,
 * обновления данных пользователя и его аватара.
 */
@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    @Value("${path.to.user.images}")
    private String imagePath;
    @Value("${path.to.default.user.image}")
    private String pathToDefaultUserImage;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    /**
     * Устанавливает новый пароль для пользователя.
     * Сравнивает текущий пароль с сохранённым и, в случае совпадения, обновляет пароль.
     *
     * @param newPassword объект {@link NewPassword}, содержащий текущий и новый пароли.
     * @return {@code true}, если пароль успешно обновлён, иначе {@code false}.
     */
    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        Optional<UserEntity> userOptional = userRepository.findById(1); // Заглушка для авторизации
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (user.getPassword().equals(newPassword.getCurrentPassword())) {
                user.setPassword(newPassword.getNewPassword());
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Получает информацию о текущем пользователе.
     *
     * @return объект {@link User}, содержащий информацию о пользователе,
     * или {@code null}, если пользователь не найден.
     */
    @Override

    public User getUser() {
        Optional<UserEntity> userOptional = userRepository.findById(1); // Заглушка для текущего пользователя
        return userOptional.map(MapperUtils::toUserDto).orElse(null);
    }

    /**
     * Обновляет информацию о пользователе (имя, фамилия, телефон).
     *
     * @param updateUser объект {@link UpdateUser}, содержащий обновлённые данные пользователя.
     * @return объект {@link UpdateUser} с обновлённой информацией, или {@code null}, если пользователь не найден.
     */
    @Override

    public UpdateUser updateUser(UpdateUser updateUser) {
        Optional<UserEntity> userOptional = userRepository.findById(1);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setPhone(updateUser.getPhone());
            userRepository.save(user);
            return updateUser;
        }
        return null;
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
            UserEntity userEntity = UserMapper.toEntity(dto);
            userEntity.setPassword(encoder.encode(dto.getPassword()));
            return userRepository.save(userEntity);
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
