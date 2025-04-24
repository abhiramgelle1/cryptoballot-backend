package com.example.cryptoballot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/security")
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityMetricsController {

    // Simulated counters for demonstration.
    private AtomicLong totalRequests = new AtomicLong(0);
    private AtomicLong blockedRequests = new AtomicLong(0);
    private AtomicLong acceptedRequests = new AtomicLong(0);

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics() {
        // For each call, simulate that a random number of requests have been processed.
        long inc = (long)(5 + Math.random() * 10);
        totalRequests.addAndGet(inc);
        // For demonstration, assume 10% are blocked.
        long blocked = totalRequests.get() / 10;
        long accepted = totalRequests.get() - blocked;
        blockedRequests.set(blocked);
        acceptedRequests.set(accepted);
        
        return ResponseEntity.ok(new Metrics(totalRequests.get(), acceptedRequests.get(), blockedRequests.get()));
    }

    public static class Metrics {
        private long totalRequests;
        private long acceptedRequests;
        private long blockedRequests;

        public Metrics(long totalRequests, long acceptedRequests, long blockedRequests) {
            this.totalRequests = totalRequests;
            this.acceptedRequests = acceptedRequests;
            this.blockedRequests = blockedRequests;
        }

        public long getTotalRequests() {
            return totalRequests;
        }
        public long getAcceptedRequests() {
            return acceptedRequests;
        }
        public long getBlockedRequests() {
            return blockedRequests;
        }
    }
}
