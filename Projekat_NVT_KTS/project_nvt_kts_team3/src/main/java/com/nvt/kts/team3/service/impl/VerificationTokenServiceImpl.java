package com.nvt.kts.team3.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.VerificationToken;
import com.nvt.kts.team3.repository.VerificationTokenRepository;
import com.nvt.kts.team3.service.VerificationTokenService;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public void saveToken(VerificationToken token) {
		verificationTokenRepository.save(token);

	}

}
