package kg.azar.auth.config;

import jakarta.annotation.PostConstruct;
import kg.azar.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final AuthService authService;

    @PostConstruct
    public void seedAdmins() {
        authService.ensureAdminExists("argen.azanov@alatoo.edu.kg", "password121234");
        authService.ensureAdminExists("admin.admin@alatoo.edu.kg", "password121234");
    }
}
