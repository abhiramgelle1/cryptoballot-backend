package com.example.cryptoballot.controller;

import com.example.cryptoballot.model.Vote;
import com.example.cryptoballot.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:3000")
public class VoteController {
    @Autowired
    private VoteRepository voteRepository;

    @PostMapping("/cast")
    public ResponseEntity<String> castVote(@RequestBody Vote vote) {
    	String nonce = UUID.randomUUID().toString();
        vote.setNonce(nonce);
        voteRepository.save(vote);
        return ResponseEntity.ok("Vote cast successfully!");
    }

    // Retrieve all votes (if needed for debugging)
    @GetMapping("/all")
    public ResponseEntity<List<Vote>> getAllVotes() {
        return ResponseEntity.ok(voteRepository.findAll());
    }

    // Get votes by a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Vote>> getVotesByUser(@PathVariable String username) {
        List<Vote> userVotes = voteRepository.findAll().stream()
            .filter(v -> v.getVoterUsername().equals(username))
            .collect(Collectors.toList());
        return ResponseEntity.ok(userVotes);
    }

    // Get total vote count in the DB
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalVoteCount() {
        long count = voteRepository.count();
        return ResponseEntity.ok(count);
    }
}
