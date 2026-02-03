package kg.azar.votingbackend.controller;

import kg.azar.votingbackend.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/live")
    public ResponseEntity<Map<String, Long>> getLiveResults() {
        return ResponseEntity.ok(resultService.getLiveResults());
    }

    @GetMapping("/final")
    public ResponseEntity<Map<String, Long>> getFinalResults(@RequestParam Long sessionId) {
        return ResponseEntity.ok(resultService.getFinalResults(sessionId));
    }
}
