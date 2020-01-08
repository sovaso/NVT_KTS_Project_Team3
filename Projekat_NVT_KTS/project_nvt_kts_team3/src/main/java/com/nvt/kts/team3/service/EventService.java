package com.nvt.kts.team3.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.List;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.UploadFileDTO;
import com.nvt.kts.team3.model.Event;

public interface EventService {
	public Event findById(Long id);
	public Event save(EventDTO eventDYO) throws ParseException;
	public Event update(EventDTO eventDYO) throws ParseException;
	public List<Event> findAll();
	public void remove(Long id);
	public List<Event> getActiveEvents();
	boolean eventIsActive(long eventId);
	public EventReportDTO getEventReport(Long id);
	public double getEventIncome(Long id);
	public String uploadFile(UploadFileDTO uploadFileDTO) throws IOException, GeneralSecurityException;

	public List<Event> findAllSortedName();
	public List<Event> findAllSortedDateDesc();
	public List<Event> findAllSortedDateAcs();
	public List<Event> searchEvent(String field, String startDate, String endDate);
	
}
