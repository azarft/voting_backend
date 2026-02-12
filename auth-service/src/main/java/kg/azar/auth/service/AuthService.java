package kg.azar.auth.service;

import kg.azar.auth.entity.Role;
import kg.azar.auth.entity.User;
import kg.azar.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    public User ensureAdminExists(String email, String rawPassword) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User admin = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(Role.ADMIN)
                    .build();
            return userRepository.save(admin);
        });
    }
}
