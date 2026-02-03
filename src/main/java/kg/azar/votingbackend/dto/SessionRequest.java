package kg.azar.votingbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class SessionRequest {
    private String title;
    private List<String> options;
}
