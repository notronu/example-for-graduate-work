package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface UserService {
    boolean setNewPassword(NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser userPatch);

    User updateImage(MultipartFile animalPhoto);

    Register registerUser(Register register);

    void loginUser(Login login);
}
