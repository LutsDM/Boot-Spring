package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.entity.User;

public interface EmailService {
    void sendConfirmationEmail (User user);
}
