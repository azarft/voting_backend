package kg.azar.votingbackend.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private Long sessionId;
    private Long optionId;
}
