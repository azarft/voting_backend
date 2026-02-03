package kg.azar.votingbackend.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteEvent implements Serializable {
    private Long voteId;
    private Long userId;
    private Long sessionId;
    private Long optionId;
    private LocalDateTime timestamp;
}
