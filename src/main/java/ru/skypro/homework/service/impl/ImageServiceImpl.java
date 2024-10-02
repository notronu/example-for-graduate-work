package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarPath;
    @Override
    public ImageEntity saveImage(MultipartFile imageFile) throws IOException {
        ImageEntity image = new ImageEntity();
        createNewPathAndSaveFile(imageFile, image);

        return getSave(image);
    }
    @Override
    public ImageEntity getImage(Integer imageId) {
        return imageRepository.findById(imageId).get();
    }
    @Override
    public ImageEntity updateImage(MultipartFile imageFile, Integer imageId) throws IOException {
        ImageEntity image = getImage(imageId);

        Path path = Path.of(image.getPath());
        Files.deleteIfExists(path);

        ImageEntity newPathAndSaveFile = createNewPathAndSaveFile(imageFile, image);
        return getSave(newPathAndSaveFile);
    }
    @Override
    public byte[] getByteFromFile(String path) throws IOException {
        return Files.readAllBytes(Path.of(avatarPath, path));
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ImageEntity getSave(ImageEntity image) {
        return imageRepository.save(image);
    }
    private ImageEntity createNewPathAndSaveFile(MultipartFile imageFile, ImageEntity image) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();

        String fileName = UUID.randomUUID() + "." + getExtension(Objects.requireNonNull(originalFilename));
        Path path = Path.of(avatarPath, fileName);

        Files.createDirectories(path.getParent());

        readAndWriteInTheDirectory(imageFile, path);

        image.setPath(path.toString());
        image.setContentType(imageFile.getContentType());
        image.setSize(imageFile.getSize());

        return image;
    }
    private void readAndWriteInTheDirectory(MultipartFile fileImage, Path path) throws IOException {
        try (
                InputStream inputStream = fileImage.getInputStream();
                OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 4096);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 4096);
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
