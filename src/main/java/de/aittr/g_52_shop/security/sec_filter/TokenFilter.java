package de.aittr.g_52_shop.security.sec_filter;

import de.aittr.g_52_shop.security.AuthInfo;
import de.aittr.g_52_shop.security.sec_service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
// Фильтр, который извлекает и проверяет токен авторизации из запроса
// Если токен действителен, то устанавливает информацию о пользователе в контексте безопасности

@Component
public class TokenFilter extends GenericFilterBean {

    private final TokenService service;

    public TokenFilter(TokenService service) {
        this.service = service;
    }

    // Метод фильтрации запроса
    // Извлекает токен из запроса, проверяет его, и если он действителен, устанавливает аутентификацию пользователя
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);

        // Если токен существует и действителен, извлекаем данные и устанавливаем аутентификацию
        if (token != null && service.validateAccessToken(token)) {
            Claims claims = service.getAccessClaims(token);
            AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);
            authInfo.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
    // Метод извлекает токен из заголовка Authorization запроса
    // Если токен начинается с префикса "Bearer ", он удаляет его и возвращает сам токен
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        // Bearer hjg4gh2g4h2jh4y2gnbl2kj4324

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Убираем префикс "Bearer "
        }
        return null;
    }
}