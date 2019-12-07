package com.nvt.kts.team3.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.TicketService;

import exception.NotUserReservation;
import exception.TicketExpired;
import exception.TicketNotFound;
import exception.TicketNotReserved;

@Service
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Ticket findById(Long id) {
		return ticketRepository.getOne(id);
	}

	@Override
	@Transactional(readOnly = false)
	@Modifying
	public Ticket save(Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	@Override
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(Long id) {
		ticketRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Ticket saveAndFlush(Ticket ticket) {
		return ticketRepository.saveAndFlush(ticket);
	}

	@Override
	public List<Ticket> getMaintenanceReservedTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceReservedTickets(maintenanceID);
	}

	@Override
	public List<Ticket> getMaintenanceSoldTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceSoldTickets(maintenanceID);
	}

	@Override
	public List<Ticket> getMaintenanceTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceTickets(maintenanceID);
	}

	@Override
	public List<Ticket> getEventTickets(long eventID) {
		return ticketRepository.getEventTickets(eventID);
	}

	@Override
	public List<Ticket> getEventReservedTickets(long eventID) {
		return ticketRepository.getEventReservedTickets(eventID);
	}

	@Override
	public List<Ticket> getEventSoldTickets(long eventID) {
		return ticketRepository.getEventSoldTickets(eventID);
	}

	@Override
	public List<Ticket> getLeasedZoneReservedTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneReservedTickets(leasedZoneID);
	}

	@Override
	public List<Ticket> getLeasedZoneSoldTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneSoldTickets(leasedZoneID);
	}

	@Override
	public List<Ticket> getLeasedZoneTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneTickets(leasedZoneID);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<Ticket> deleteByZoneId(long zoneId) {
		return ticketRepository.deleteByZoneId(zoneId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean cancelTicket(Long id) {
		Optional<Ticket> tic = this.ticketRepository.findById(id);
		if (tic.isPresent()) {
			Ticket t = tic.get();
			Reservation r = t.getReservation();
			RegularUser logged = (RegularUser) userRepository
					.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

			if (r != null) {
				if (logged.getId() == r.getUser().getId()) {
					if (t.getZone().getMaintenance().getReservationExpiry().isAfter(LocalDateTime.now())) {
						t.setReserved(false);
						t.setReservation(null);
						r.getReservedTickets().remove(t);
						reservationRepository.save(r);
						ticketRepository.save(t);
						if (r.getReservedTickets().size() == 0) {
							reservationRepository.deleteById(r.getId());

						}
					} else {
						throw new TicketExpired();
					}
					return true;
				} else {
					throw new NotUserReservation();

				}
			} else {
				throw new TicketNotReserved();
			}
		} else {
			throw new TicketNotFound();
		}

	}

	@Override
	public List<Ticket> getExpieredUnpaidTickets(LocalDateTime hourAgo, LocalDateTime now) {
		return ticketRepository.getExpieredUnpaidTickets(hourAgo, now);
	}
}
