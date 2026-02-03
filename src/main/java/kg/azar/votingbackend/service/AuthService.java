package kg.azar.votingbackend.service;

import kg.azar.votingbackend.entity.Role;
import kg.azar.votingbackend.entity.User;
import kg.azar.votingbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final CacheManager cacheManager;

    public void sendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache != null) {
            cache.put(email, code);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Voting Verification Code");
        message.setText("Your verification code is: " + code + ". It expires in 5 minutes.");
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log error in real scenario
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public boolean verifyCode(String email, String code) {
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache == null) return false;

        String storedCode = cache.get(email, String.class);
        if (storedCode != null && storedCode.equals(code)) {
            cache.evict(email);
            
            // Register user if not exists
            if (userRepository.findByEmail(email).isEmpty()) {
                User user = User.builder()
                        .email(email)
                        .role(Role.USER)
                        .verified(true)
                        .build();
                userRepository.save(user);
            }
            return true;
        }
        return false;
    }
}
