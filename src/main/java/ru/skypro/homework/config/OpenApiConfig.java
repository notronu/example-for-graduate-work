package ru.skypro.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

/**
 * Конфигурационный класс для настройки OpenAPI, который позволяет добавлять поддержку дополнительных типов
 * данных для конвертера сообщений HTTP. В данном случае, добавляется поддержка медиатипа "application/octet-stream".
 *
 * <p>
 * Этот класс помечен аннотацией {@link Configuration}, что указывает на то, что это класс конфигурации Spring.
 * </p>
 *
 * @author Ваше Имя
 * @version 1.0
 * @see MappingJackson2HttpMessageConverter
 * @since 2024
 */
@Configuration
public class OpenApiConfig {

    /**
     * Конструктор, принимающий {@link MappingJackson2HttpMessageConverter}.
     * <p>
     * Добавляет MIME-тип "application/octet-stream" к списку поддерживаемых типов медиа,
     * что позволяет обрабатывать двоичные данные.
     * </p>
     *
     * @param converter Конвертер для преобразования HTTP-сообщений с помощью Jackson.
     */
    public OpenApiConfig(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }
}
