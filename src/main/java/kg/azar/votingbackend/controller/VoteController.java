package kg.azar.votingbackend.controller;

import kg.azar.votingbackend.dto.VoteRequest;
import kg.azar.votingbackend.entity.Vote;
import kg.azar.votingbackend.messaging.VoteEvent;
import kg.azar.votingbackend.messaging.producer.VoteProducer;
import kg.azar.votingbackend.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VotingService votingService;
    private final VoteProducer voteProducer;

    @PostMapping
    public ResponseEntity<String> submitVote(@RequestBody VoteRequest request, Authentication authentication) {
        String userEmail = authentication.getName();
        Vote vote = votingService.submitVote(userEmail, request.getSessionId(), request.getOptionId());
        
        VoteEvent event = new VoteEvent(
                vote.getId(),
                vote.getUser().getId(),
                vote.getSession().getId(),
                vote.getOption().getId(),
                vote.getTimestamp()
        );
        voteProducer.sendVoteEvent(event);
        
        return ResponseEntity.ok("Vote submitted successfully");
    }
}
