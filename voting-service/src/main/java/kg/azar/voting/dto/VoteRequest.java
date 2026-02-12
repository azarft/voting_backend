package kg.azar.voting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotNull
    private Long optionId;
}
