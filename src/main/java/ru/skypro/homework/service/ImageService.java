package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;

import java.io.IOException;

@Service
public interface ImageService {

    /**
     * Сохраняет изображение в хранилище.
     *
     *       @param imageFile Файл изображения.
     *       @return Сохраненное изображение.
     *       @throws IOException Если возникла ошибка во время записи файла.
     */
    ImageEntity saveImage(MultipartFile imageFile) throws IOException;

    /**
     *  Возвращает изображение по его идентификатору.
     *
     *       @param imageId Идентификатор изображения.
     *       @return Изображение.
     */
    ImageEntity getImage(Integer imageId);

    /**
     * Обновляет изображение по его идентификатору.
     *
     *       @param image Файл изображения.
     *       @param imageId  Идентификатор изображения.
     *       @return Обновленное изображение.
     *       @throws IOException Если возникла ошибка во время записи файла.
     */
    ImageEntity updateImage(MultipartFile image, Integer imageId) throws IOException;

    /**
     * Возвращает байты изображения по его пути.
     *
     *       @param path Путь к изображению.
     *       @return Байты изображения.
     *       @throws IOException Если возникла ошибка во время чтения файла.
     */
    byte[] getByteFromFile(String path) throws IOException;
}
