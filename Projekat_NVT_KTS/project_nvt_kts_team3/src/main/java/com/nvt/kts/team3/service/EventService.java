package com.nvt.kts.team3.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.model.Event;

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
}
