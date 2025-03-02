package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import de.aittr.g_52_shop.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// Это сервис авторизации, который проверяет сырой пароль клиента с паролем из базы и выдает access и refresh Токен
@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage; // Хранит refresh токены для пользователей
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }
    // Метод для логина пользователя, который проверяет введенный пароль и выдает токены
    public TokenResponseDto login(User inboundUser) throws AuthException {
        //достаем юзернейм из объекта пользователя, который нам прислали
        String username = inboundUser.getUsername();
        // По этому юзернейму ищем пользователя в базе, чтобы сравнить пароль из базы и присланный
        UserDetails foundUser = userService.loadUserByUsername(username);

        //Сравниваем входящий пароль и пароль в базе данных и если совпадают то генерируем токены обернутый в объект Respons Token DTO
        if (passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);// Генерация access токена
            String refreshToken = tokenService.generateRefreshToken(foundUser);// Генерация refresh токена
            refreshStorage.put(username, refreshToken); //Сохраняем сюда рефреш токен чтобы потом проверять выдавался ли вообще пользователю этот токен
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    //Метод для получения accessToken, когда приходит рефреш токен
    public TokenResponseDto getNewAccessToken(String inboundRefreshToken) {
        //Достаем данные пользователя из присланного refresh токена
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        //Достаем именно имя пользователя из присланного refresh токена
        String username = refreshClaims.getSubject();
        //Проверяем выдавали ли токен
        String foundRefreshToken = refreshStorage.get(username); // Получаем сохраненный refresh токен по имени пользователя
        // Если refresh токен совпадает, генерируем новый access токен
        if (foundRefreshToken != null && foundRefreshToken.equals(inboundRefreshToken)) {
            //Генерируем новый accessToken
            UserDetails foundUser = userService.loadUserByUsername(username);//Подгружаем юзера из базы
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken);
        } else {
            return new TokenResponseDto(null);

        }
    }
}


