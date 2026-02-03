package kg.azar.votingbackend.controller;

import kg.azar.votingbackend.dto.SessionRequest;
import kg.azar.votingbackend.entity.VotingSession;
import kg.azar.votingbackend.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/session")
@RequiredArgsConstructor
public class AdminController {

    private final VotingService votingService;

    @GetMapping
    public ResponseEntity<List<VotingSession>> getAllSessions() {
        return ResponseEntity.ok(votingService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingSession> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(votingService.getSessionById(id));
    }

    @PostMapping
    public ResponseEntity<VotingSession> createSession(@RequestBody SessionRequest request) {
        return ResponseEntity.ok(votingService.createSession(request));
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<String> activateSession(@PathVariable Long id) {
        votingService.activateSession(id);
        return ResponseEntity.ok("Session " + id + " activated");
    }

    @PostMapping("/close/{id}")
    public ResponseEntity<String> closeSession(@PathVariable Long id) {
        votingService.closeSession(id);
        return ResponseEntity.ok("Session " + id + " closed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable Long id) {
        votingService.deleteSession(id);
        return ResponseEntity.ok("Session " + id + " deleted");
    }
}
