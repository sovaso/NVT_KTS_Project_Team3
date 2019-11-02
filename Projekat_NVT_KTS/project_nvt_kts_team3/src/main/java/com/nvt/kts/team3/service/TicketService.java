package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.Ticket;

public interface TicketService {
	public Ticket findById(Long id);
	public Ticket save(Ticket ticket);
	public List<Ticket> findAll();
	public void remove(Long id);
}
