package com.example.ScienceStationProject.repository;

import com.example.ScienceStationProject.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    Optional<VerificationToken> findOneByToken (String token);
}
