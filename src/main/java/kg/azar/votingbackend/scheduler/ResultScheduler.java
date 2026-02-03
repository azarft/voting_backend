package kg.azar.votingbackend.scheduler;

import kg.azar.votingbackend.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResultScheduler {

    private final ResultService resultService;

    @Scheduled(fixedRate = 2000)
    public void refreshResults() {
        log.debug("Refreshing live results cache...");
        resultService.refreshLiveResults();
    }
}
