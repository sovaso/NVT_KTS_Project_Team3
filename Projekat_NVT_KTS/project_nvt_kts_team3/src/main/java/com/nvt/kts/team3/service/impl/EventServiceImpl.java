package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.service.EventService;

@Service
public class EventServiceImpl implements EventService{

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public Event findById(Long id) {
		return eventRepository.getOne(id);
	}

	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		eventRepository.deleteById(id);
	}
	
	@Override
	public ArrayList<Event> getReservedTickets(Long eventId) {
		return eventRepository.getReservedTickets(eventId);
	}

	@Override
	public ArrayList<Event> getSoldTickets(Long eventId) {
		return eventRepository.getSoldTickets(eventId);
	}

	@Override
	public List<Event> getActiveEvents() {
		return eventRepository.getActiveEvents();
	}

}
