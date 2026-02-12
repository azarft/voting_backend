package kg.azar.voting.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final long WINDOW_MILLIS = 10_000; // 10 seconds
    private final Map<String, Long> lastCallByIp = new ConcurrentHashMap<>();

    public boolean allow(String ip) {
        long now = Instant.now().toEpochMilli();
        return lastCallByIp.compute(ip, (k, last) -> {
            if (last == null || now - last >= WINDOW_MILLIS) {
                return now;
            }
            return last;
        }) == now;
    }
}
