package com.nvt.kts.team3.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.UploadFileDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;

public interface EventService {
	public Event findById(Long id);
	public Event save(EventDTO eventDYO) throws ParseException;
	public Event update(EventDTO eventDYO) throws ParseException;
	public List<Event> findAll();
	public void remove(Long id);
	public ArrayList<Event> getReservedTickets(Long eventId);
	public ArrayList<Event> getSoldTickets(Long eventId);
	public List<Event> getActiveEvents();
	boolean eventIsActive(long eventId);
	public EventReportDTO getEventReport(Long id);
	public double getEventIncome(Long id);
	public String uploadFile(UploadFileDTO uploadFileDTO) throws IOException, GeneralSecurityException;
	public List<Event> findByName(String name);
	public List<Event> findByLocationAddress(String locationAddress);
	public List<Event> findByLocation(Location location);
	public List<Event> findByType(EventType type);
	public List<Event> findAllSortedName();
	public List<Event> findAllSortedDateDesc();
	public List<Event> findAllSortedDateAcs();
	public List<Event> searchEvent(String field, LocalDateTime startDate, LocalDateTime endDate);
	
}
