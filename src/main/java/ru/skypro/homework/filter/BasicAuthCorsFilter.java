package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Фильтр для добавления заголовка CORS (Cross-Origin Resource Sharing) в ответы на запросы.
 * Этот фильтр срабатывает один раз для каждого запроса и добавляет заголовок, разрешающий использование учетных данных
 * (например, куки или заголовки авторизации) в кросс-доменных запросах.
 *
 * Класс расширяет {@link OncePerRequestFilter}, что гарантирует выполнение фильтра для каждого запроса только один раз.
 */

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Добавляет заголовок "Access-Control-Allow-Credentials" в ответ, разрешающий отправку учетных данных при CORS-запросах.
     *
     * @param httpServletRequest  текущий HTTP-запрос.
     * @param httpServletResponse текущий HTTP-ответ, к которому добавляется заголовок.
     * @param filterChain         цепочка фильтров для передачи запроса дальше.
     * @throws ServletException если возникает ошибка обработки фильтра.
     * @throws IOException      если возникает ошибка ввода-вывода при обработке фильтра.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
