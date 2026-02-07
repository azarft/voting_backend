package kg.azar.result.dto;

import lombok.Data;
import java.util.List;

@Data
public class SessionRequest {
    private String title;
    private List<String> options;
}
