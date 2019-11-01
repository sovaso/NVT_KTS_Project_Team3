package com.nvt.kts.team3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nvt.kts.team3.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
