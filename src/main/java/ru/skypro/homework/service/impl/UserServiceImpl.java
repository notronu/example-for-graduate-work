package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.MapperUtils;

import java.util.Optional;

/**
 * Реализация сервиса для управления пользователями.
 * Предоставляет методы для обновления пароля, получения информации о пользователе,
 * обновления данных пользователя и его аватара.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

    /**
     * Обновляет аватар пользователя.
     *
     * @param image файл изображения для обновления аватара пользователя.
     */
    @Override
    public void updateImage(MultipartFile image) {
        Optional<UserEntity> userOptional = userRepository.findById(1);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setImage(image.getOriginalFilename());
            userRepository.save(user);
        }
    }
}
