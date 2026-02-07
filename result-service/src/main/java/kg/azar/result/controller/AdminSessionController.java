package kg.azar.result.controller;

import kg.azar.result.dto.SessionRequest;
import kg.azar.result.entity.VotingSession;
import kg.azar.result.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AdminSessionController {

    private final SessionService sessionService;

    @GetMapping("/session/active")
    public ResponseEntity<VotingSession> getActiveSession() {
        return sessionService.getActiveSession()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/session")
    public ResponseEntity<List<VotingSession>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/admin/session/{id}")
    public ResponseEntity<VotingSession> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping("/admin/session")
    public ResponseEntity<VotingSession> createSession(@RequestBody SessionRequest request) {
        return ResponseEntity.ok(sessionService.createSession(request));
    }

    @PostMapping("/admin/session/activate/{id}")
    public ResponseEntity<String> activateSession(@PathVariable Long id) {
        sessionService.activateSession(id);
        return ResponseEntity.ok("Session " + id + " activated");
    }

    @PostMapping("/admin/session/close/{id}")
    public ResponseEntity<String> closeSession(@PathVariable Long id) {
        sessionService.closeSession(id);
        return ResponseEntity.ok("Session " + id + " closed");
    }

    @DeleteMapping("/admin/session/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok("Session " + id + " deleted");
    }
}
