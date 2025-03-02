package de.aittr.g_52_shop.security.sec_dto;

public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    // Конструктор для инициализации объекта с access и refresh токенами,
    // когда пользователь отправляет логин и пароль для аутентификации
    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Конструктор для инициализации объекта с только access токеном,
    // когда пользователь отправляет refresh токен для обновления access токена
    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return String.format("Token Respons Dto: access token - %s, refresh token - %s.", accessToken, refreshToken);
    }
}
