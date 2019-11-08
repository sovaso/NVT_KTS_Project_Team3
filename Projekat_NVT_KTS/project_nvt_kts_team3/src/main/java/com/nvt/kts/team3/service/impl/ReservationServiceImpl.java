package com.nvt.kts.team3.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@Override
	public Reservation findById(Long id) {
		return reservationRepository.getOne(id);
	}

	@Override
	public Reservation create(Reservation reservation) {
		Optional<Event> eventOpt = eventRepository.findById(reservation.getEvent().getId());
		Event e = eventOpt.get();
		if (e.isStatus() == false || e == null) {
			return null;
		}
		reservation.setEvent(e);
		RegularUser logged = (RegularUser) userRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (logged == null) {
			return null;
		}
		List<Reservation> userReservations = findByUserAndPaid(logged, false);
		int numTickets = 0;
		for (Reservation r : userReservations) {
			numTickets += r.getReservedTickets().size();
		}
		if (numTickets > 100) {
			return null;
		}
		boolean created = false;
		Set<Ticket> old = reservation.getReservedTickets();
		Set<Ticket> resTickets = new HashSet<Ticket>();
		reservation.setReservedTickets(resTickets);
		for (Ticket t : old) {
			Optional<Ticket> ticketOpt = this.ticketRepository.findById(t.getId());
			if (ticketOpt.isPresent()) {
				Ticket ticket = ticketOpt.get();
				if (ticket.isReserved() == false
						&& ticket.getZone().getMaintenance().getReservationExpiry().after(new Date())) {
					if (created == false) {
						reservationRepository.save(reservation);
						created = true;
					}
					reservation.getReservedTickets().add(ticket);
					ticket.setReserved(true);
					ticket.setReservation(reservation);
					ticketRepository.save(ticket);
					reservation.setTotalPrice(reservation.getTotalPrice() + ticket.getPrice());
				}
			}
		}
		if (created == false) {
			return null;
		}
		reservation.setUser(logged);
		reservation.setReservedTickets(resTickets);
		return this.reservationRepository.save(reservation);

	}

	@Override
	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		reservationRepository.deleteById(id);
	}

	@Override
	public List<Reservation> findByUser(RegularUser u) {
		return reservationRepository.findByUser(u);
	}

	@Override
	public List<Reservation> findByEvent(Event e) {
		return reservationRepository.findByEvent(e);
	}

	@Override
	public List<Reservation> findByUserAndPaid(RegularUser u, boolean paid) {
		return reservationRepository.findByUserAndPaid(u, paid);
	}

}
