package com.nvt.kts.team3.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.ReservationService;

import exception.EventNotActive;
import exception.EventNotFound;
import exception.LocationNotFound;
import exception.NoLoggedUser;
import exception.NotUserReservation;
import exception.ReservationAlreadyPaid;
import exception.ReservationCannotBeCancelled;
import exception.ReservationCannotBeCreated;
import exception.ReservationExpired;
import exception.ReservationNotFound;
import exception.TooManyTicketsReserved;

@Service
//@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public Optional<Reservation> findById(Long id) {
		return reservationRepository.findById(id);
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Reservation create(Reservation reservation) {
		Optional<Event> eventOpt = eventRepository.findById(reservation.getEvent().getId());
		if (!eventOpt.isPresent()) {
			throw new EventNotFound();
		} else {
			Event e = eventOpt.get();
			if (e.isStatus() == false) {
				throw new EventNotActive();
			} else {
				reservation.setEvent(e);
				System.out.println("Name"+SecurityContextHolder.getContext());
				RegularUser logged = (RegularUser) userRepository
						.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
				if (logged == null) {
					throw new NoLoggedUser();
				} else {
					//RegularUser ru=(RegularUser) userRepository.findById(logged.getId()); //OVO
					List<Reservation> userReservations = reservationRepository.findByUserAndPaid(logged, false);
					System.out.println("Korisnik "+logged.getUsername());
					int numTickets = 0;
					for (Reservation r : userReservations) {
						numTickets += r.getReservedTickets().size();
					}
					if (numTickets > 3) { //100
						throw new TooManyTicketsReserved();
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
									&& ticket.getZone().getMaintenance().getReservationExpiry().isAfter(LocalDateTime.now())) {
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
						throw new ReservationCannotBeCreated();
					} else {
						reservation.setUser(logged); //logged
						reservation.setReservedTickets(resTickets);
						
						return this.reservationRepository.save(reservation);
					}
				}
			}
		}

	}

	@Override
	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean remove(Long id) {
		Optional<Reservation> res = reservationRepository.findById(id);
		if (res.isPresent()) {
			Reservation r = res.get();
			if (!r.isPaid()) {
				RegularUser logged = (RegularUser) userRepository
						.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
				if (logged.getId() == r.getUser().getId()) {
					int noSuccess = 0;
					for (Ticket t : r.getReservedTickets()) {
						if (t.getZone().getMaintenance().getReservationExpiry().isAfter(LocalDateTime.now())
								&& t.getReservation().getId() == r.getId() && t.isReserved() == true) {
							t.setReserved(false);
							t.setReservation(null);
						} else {
							noSuccess++;
						}
					}
					if (noSuccess == 0) {
						reservationRepository.deleteById(id);
						return true;
					} else {
						throw new ReservationCannotBeCancelled();
					}
				} else {
					throw new NotUserReservation();
				}
			} else {
				throw new ReservationAlreadyPaid();
			}
		} else {
			throw new ReservationNotFound();
		}
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

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean payReservation(Long id) {
		Optional<Reservation> res = reservationRepository.findById(id);
		if (res.isPresent()) {
			Reservation r = res.get();
			RegularUser logged = (RegularUser) userRepository
					.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (logged.getId() == r.getUser().getId()) {
				if (r.isPaid() == false) {
					if (r.getEvent().isStatus() == true) {
						int noSuccess = 0;
						for (Ticket t : r.getReservedTickets()) {
							if (!t.getZone().getMaintenance().getReservationExpiry().isAfter(LocalDateTime.now())) {
								noSuccess++;
							}
						}
						if (noSuccess < r.getReservedTickets().size()) {
							r.setPaid(true);
							reservationRepository.save(r);
							return true;
						} else {
							throw new ReservationExpired();
						}
					} else {
						throw new EventNotActive();
					}
				} else {
					throw new ReservationAlreadyPaid();
				}
			} else {
				throw new NotUserReservation();
			}
		} else {
			throw new ReservationNotFound();
		}
	}

	@Override
	public List<Reservation> getLocationReservations(Long id) {
		Optional<Location> loc = this.locationRepository.findById(id);
		if (loc.isPresent()) {
			List<Reservation> res = reservationRepository.findAll();
			List<Reservation> ret = new ArrayList<Reservation>();
			for (Reservation r : res) {
				if (r.getEvent().getLocationInfo().getId() == id) {
					ret.add(r);
				}
			}
			return ret;
		} else {
			throw new LocationNotFound();
		}
	}

	@Override
	@Transactional(readOnly = false)
	@Modifying
	public void delete(Long id) {
		reservationRepository.deleteById(id);
	}
	
	

	@Override
	public List<Reservation> findReservationsLoggedUser() {
		RegularUser logged = (RegularUser) userRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		List<Reservation> reservations=findByUser(logged);
		return reservations;
	}

}
