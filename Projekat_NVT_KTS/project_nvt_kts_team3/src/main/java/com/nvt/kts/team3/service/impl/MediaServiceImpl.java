package com.nvt.kts.team3.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Media;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.MediaRepository;
import com.nvt.kts.team3.service.MediaService;

@Service
public class MediaServiceImpl implements MediaService {
	
	@Autowired
	private MediaRepository mediaRepository;

	@Override
	public Media save(Media m) {
		return mediaRepository.save(m);
	}
	

}
