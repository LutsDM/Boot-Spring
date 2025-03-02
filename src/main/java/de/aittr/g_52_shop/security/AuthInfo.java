package de.aittr.g_52_shop.security;

import de.aittr.g_52_shop.domain.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
// Класс, представляющий данные аутентификации пользователя, реализует интерфейс Authentication
// и используется для хранения информации о пользователе после успешной авторизации.
public class AuthInfo implements Authentication {

    private boolean authentication; // Флаг, указывающий, аутентифицирован ли пользователь
    private String username;
    private Set<Role> roles;

    public AuthInfo(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return authentication == authInfo.authentication && Objects.equals(username, authInfo.username) && Objects.equals(roles, authInfo.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authentication, username, roles);
    }
    // Метод getName возвращает имя пользователя, используемое для аутентификации
    @Override
    public String getName() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("Auht info: авторизован - %b, имя - %s, роли - %s.", authentication, username, roles);
    }
    // Метод для получения списка авторизаций (ролей) пользователя
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }
    // Метод, возвращающий главную сущность, представляющую пользователя (principal). В данном случае это имя пользователя.
    @Override
    public Object getPrincipal() {
        return username;
    }
    // Метод для проверки, аутентифицирован ли пользователь
    @Override
    public boolean isAuthenticated() {
        return authentication;
    }
    // Метод для установки флага аутентификации
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authentication = isAuthenticated;

    }
}
