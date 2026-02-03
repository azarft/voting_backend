package kg.azar.votingbackend.controller;

import kg.azar.votingbackend.entity.VotingSession;
import kg.azar.votingbackend.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final VotingService votingService;

    @GetMapping("/active")
    public ResponseEntity<VotingSession> getActiveSession() {
        return ResponseEntity.ok(votingService.getActiveSession());
    }
}
