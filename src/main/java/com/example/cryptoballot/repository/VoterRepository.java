package com.example.cryptoballot.repository;

import com.example.cryptoballot.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    Voter findByUsername(String username);
}
