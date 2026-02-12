package kg.azar.voting.controller;

import kg.azar.common.messaging.VoteEvent;
import kg.azar.voting.dto.VoteRequest;
import kg.azar.voting.messaging.VoteProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/polls")
@RequiredArgsConstructor
public class VoteController {

    private final VoteProducer voteProducer;
    private final kg.azar.voting.service.RateLimiterService rateLimiterService;

    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> submitVote(@PathVariable("id") Long sessionId,
                                           @RequestBody VoteRequest request,
                                           @CookieValue(value = "deviceId", required = false) String deviceId,
                                           HttpServletResponse response,
                                           HttpServletRequest httpRequest) {
        if (deviceId == null || deviceId.isBlank()) {
            deviceId = java.util.UUID.randomUUID().toString();
            Cookie cookie = new Cookie("deviceId", deviceId);
            cookie.setHttpOnly(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
            response.addCookie(cookie);
        }

        String ip = getClientIp(httpRequest);
        if (!rateLimiterService.allow(ip)) {
            return ResponseEntity.status(429).build();
        }

        VoteEvent event = VoteEvent.builder()
                .deviceId(deviceId)
                .ipAddress(ip)
                .sessionId(sessionId)
                .optionId(request.getOptionId())
                .timestamp(LocalDateTime.now())
                .build();

        voteProducer.sendVoteEvent(event);

        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        } else {
            // X-Forwarded-For can contain multiple IPs, use the first one
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
