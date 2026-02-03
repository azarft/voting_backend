package kg.azar.votingbackend.controller;

import kg.azar.votingbackend.dto.AuthRequest;
import kg.azar.votingbackend.dto.AuthResponse;
import kg.azar.votingbackend.dto.VerifyRequest;
import kg.azar.votingbackend.entity.User;
import kg.azar.votingbackend.repository.UserRepository;
import kg.azar.votingbackend.security.JwtService;
import kg.azar.votingbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/request-code")
    public ResponseEntity<String> requestCode(@Valid @RequestBody AuthRequest request) {
        authService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("Verification code sent to " + request.getEmail());
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerifyRequest request) {
        if (authService.verifyCode(request.getEmail(), request.getCode())) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found after verification"));

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    "",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );

            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid or expired verification code");
        }
    }
}
