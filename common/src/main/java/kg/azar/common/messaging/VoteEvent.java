package kg.azar.common.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEvent implements Serializable {
    private Long userId;
    private Long sessionId;
    private Long optionId;
    private LocalDateTime timestamp;
}
