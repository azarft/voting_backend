package kg.azar.voting.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kg.azar.common.messaging.VoteEvent;
import kg.azar.voting.dto.VoteRequest;
import kg.azar.voting.messaging.VoteProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteProducer voteProducer;

    @Value("${app.jwt.secret}")
    private String secretKey;

    @PostMapping
    public ResponseEntity<Void> submitVote(@RequestBody VoteRequest request, @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        
        Long userId = claims.get("userId", Long.class);

        VoteEvent event = VoteEvent.builder()
                .userId(userId)
                .sessionId(request.getSessionId())
                .optionId(request.getOptionId())
                .timestamp(LocalDateTime.now())
                .build();

        voteProducer.sendVoteEvent(event);

        return ResponseEntity.ok().build();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
