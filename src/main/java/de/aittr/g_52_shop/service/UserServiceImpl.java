package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.ConfirmationCode;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.ConfirmationCodeRepository;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.service.interfaces.EmailService;
import de.aittr.g_52_shop.service.interfaces.RoleService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final ConfirmationCodeRepository confirmationRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository repository, ConfirmationCodeRepository confirmationRepository,
                           BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService) {
        this.repository = repository;
        this.confirmationRepository = confirmationRepository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    // При помощи этого метода фреймворк будет получать из БД
    // объекты пользователей вместе с их ролями
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")
        );
    }

    @Override
    public void register(User user) {
        user.setId(null);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(false);
        user.setRoles(Set.of(roleService.getRoleUser()));

        repository.save(user);
        emailService.sendConfirmationEmail(user);
    }

    @Override
    public boolean confirmUser(String code) {
        Optional<ConfirmationCode> confirmationCodeOpt = confirmationRepository.findByCode(code);

        if (confirmationCodeOpt.isPresent()) {
            ConfirmationCode confirmationCode = confirmationCodeOpt.get();

            if (confirmationCode.getExpired().isAfter(LocalDateTime.now())) {
                User user = confirmationCode.getUser();
                user.setActive(true);
                repository.save(user);
                confirmationRepository.delete(confirmationCode);
                return true;
            }
        }
        return false;
    }

}
