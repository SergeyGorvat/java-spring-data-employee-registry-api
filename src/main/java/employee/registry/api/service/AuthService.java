package employee.registry.api.service;

import employee.registry.api.dto.AuthRequestDto;
import employee.registry.api.dto.AuthResponseDto;
import employee.registry.api.exception.AccountLockedException;
import employee.registry.api.model.User;
import employee.registry.api.repository.UserRepository;
import employee.registry.api.security.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JWTUtils jwtUtils;

    @Lazy
    @Autowired
    private AuthService self;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       UserService userService,
                       JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Неверный логин или пароль"));

        if (!user.isAccountNonLocked()) {
            log.warn("Вход в заблокированный аккаунт '{}'", dto.getUsername());
            throw new AccountLockedException("Аккаунт заблокирован. Обратитесь к администратору");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            self.handleFailedAttempt(user);
            throw new BadCredentialsException("Неверный логин или пароль");
        }

        user.setFailedAttempts(0);
        userRepository.save(user);

        UserDetails userDetails = userService.loadUserByUsername(dto.getUsername());
        String token = jwtUtils.generateToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userDetails);

        log.info("Пользователь '{}' вошёл в систему", dto.getUsername());

        return AuthResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .message("Вход выполнен успешно")
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFailedAttempt(User user) {
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new BadCredentialsException("Пользователь не найден"));

        int attempts = freshUser.getFailedAttempts() + 1;
        freshUser.setFailedAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            freshUser.setAccountNonLocked(false);
            log.warn("Аккаунт '{}' заблокирован после {} попыток",
                    freshUser.getUsername(), attempts);
        } else {
            log.warn("Неудачная попытка входа для '{}'. Попыток: {}/{}",
                    freshUser.getUsername(), attempts, MAX_FAILED_ATTEMPTS);
        }

        userRepository.save(freshUser);
    }

    @Transactional
    public void unlockAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Пользователь не найден"));
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        userRepository.save(user);
        log.info("Аккаунт '{}' разблокирован", username);
    }
}