package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.RoleRepository;
import de.aittr.g_52_shop.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
// Сервис, который генерирует access и refresh токены, проверяет их на соответствие,
// а также переводит токены в объект AuthInfo для использования в процессе авторизации.
@Service
public class TokenService {

    private SecretKey accessKey;
    private SecretKey refreshKey;
    private RoleRepository roleRepository;

    // Конструктор инициализирует ключи для подписи токенов, используя переданные фразы,
    // а также роль из репозитория для дальнейшего использования при создании токенов.
    public TokenService(
            @Value("${key.access}") String accessPhrase,
            @Value("${key.refresh}") String refreshPhrase,
                        RoleRepository roleRepository) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));

        this.roleRepository = roleRepository;
    }

    // Метод для создания access токена для пользователя с истечением срока действия через 1 день.
    public String generateAccessToken(UserDetails user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expiration = currentDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername()) // Используем имя пользователя как subject
                .expiration(expirationDate)  // Устанавливаем срок действия токена
                .signWith(accessKey)         // Подписываем токен с использованием accessKey
                .claim("roles", user.getAuthorities())  // Добавляем роли пользователя
                .claim("name", user.getUsername())     // Добавляем имя пользователя
                .compact();
    }

    // Метод для создания refresh токена для пользователя с истечением срока действия через 7 дней.
    public String generateRefreshToken(UserDetails user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expiration = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername()) // Используем имя пользователя как subject
                .expiration(expirationDate)  // Устанавливаем срок действия токена
                .signWith(refreshKey)        // Подписываем токен с использованием refreshKey
                .compact();
    }

    // Проверяет действительность access токена с использованием accessKey.
    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken, accessKey);
    }

    // Проверяет действительность refresh токена с использованием refreshKey.
    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken, refreshKey);
    }

    // Проверяет действительность любого токена, используя указанный ключ (accessKey или refreshKey).
    public boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)        // Проверка подписи токена с использованием ключа
                    .build()
                    .parseSignedClaims(token);  // Парсинг токена для извлечения данных
            return true;
        } catch (Exception e) {
            return false;  // Возвращаем false, если токен недействителен
        }
    }

    // Извлекает Claims (требуемые данные) из access токена.
    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    // Извлекает Claims (требуемые данные) из refresh токена.
    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    // Извлекает Claims из токена с использованием указанного ключа.
    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)        // Проверка подписи токена
                .build()
                .parseSignedClaims(token)  // Парсинг токена
                .getPayload();          // Извлечение полезной нагрузки токена (данных)
    }

    // Преобразует Claims в объект AuthInfo, который используется для дальнейшей авторизации.
    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        String username = claims.getSubject();  // Извлекаем имя пользователя из Claims

        // Извлекаем список ролей пользователя из Claims.
        // Каждая роль представлена как HashMap с ключом "authority" и значением роли (например, "ROLE_ADMIN").
        List<LinkedHashMap<String, String>> rolesList =
                (List<LinkedHashMap<String, String>>) claims.get("roles");

        Set<Role> roles = new HashSet<>();  // Множество для хранения ролей

        // Перебираем список ролей и добавляем их в Set, если роль найдена в репозитории
        for (LinkedHashMap<String, String> roleEntry : rolesList) {
            String roleTitle = roleEntry.get("authority");
            roleRepository.findByTitle(roleTitle).ifPresent(roles::add);
        }

        return new AuthInfo(username, roles);  // Возвращаем объект AuthInfo с пользователем и его ролями
    }

}