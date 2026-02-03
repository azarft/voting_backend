package kg.azar.votingbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    @Email
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@alatoo\\.edu\\.kg$", message = "Only @alatoo.edu.kg emails are allowed")
    private String email;
}
