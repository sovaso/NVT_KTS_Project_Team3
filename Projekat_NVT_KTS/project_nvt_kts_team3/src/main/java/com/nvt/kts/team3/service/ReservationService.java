package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.Reservation;

public interface ReservationService {
	public Reservation findById(Long id);
	public Reservation save(Reservation reservation);
	public List<Reservation> findAll();
	public void remove(Long id);
}
