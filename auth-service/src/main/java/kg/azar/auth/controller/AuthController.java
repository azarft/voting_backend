package kg.azar.auth.controller;

import jakarta.validation.Valid;
import kg.azar.auth.dto.AuthRequest;
import kg.azar.auth.dto.AuthResponse;
import kg.azar.auth.dto.VerifyRequest;
import kg.azar.auth.entity.User;
import kg.azar.auth.security.JwtService;
import kg.azar.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        User user = authService.authenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails, user.getId());
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }
}
