package com.nvt.kts.team3.service;

import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.model.Event;

public interface EventService {
	public Event findById(Long id);
	public Event save(Event event);
	public List<Event> findAll();
	public void remove(Long id);
	public ArrayList<Event> getReservedTickets(Long eventId);
	public ArrayList<Event> getSoldTickets(Long eventId);
	public List<Event> getActiveEvents();
	public EventReportDTO getEventReport(Long id);
}
