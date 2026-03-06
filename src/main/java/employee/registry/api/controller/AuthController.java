package employee.registry.api.controller;

import employee.registry.api.dto.AuthRequestDto;
import employee.registry.api.dto.AuthResponseDto;
import employee.registry.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlock(@PathVariable String username) {
        authService.unlockAccount(username);
        return ResponseEntity.ok("Аккаунт " + username + " разблокирован");
    }
}
