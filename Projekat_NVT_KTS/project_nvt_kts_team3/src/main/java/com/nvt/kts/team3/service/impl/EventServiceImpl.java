package com.nvt.kts.team3.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidEventType;
import exception.InvalidPrice;
import exception.LocationNotAvailable;
import exception.LocationNotChangeable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

	@Autowired
	private MaintenanceService maintenanceService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationZoneService locationZoneService;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private EventRepository eventRepository;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public Event findById(Long id) {
		return eventRepository.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Event save(EventDTO eventDTO) throws ParseException {
		if (EventType.valueOf(eventDTO.getEventType()) == null) {
			throw new InvalidEventType();
		}
		Location location = locationService.findById(eventDTO.getLocationId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}

		Event event = new Event(eventDTO.getName(), true, EventType.valueOf(eventDTO.getEventType()),
				new HashSet<Reservation>(), new HashSet<Maintenance>(), location, new ArrayList<String>(),
				new ArrayList<String>());

		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		for (MaintenanceDTO dates : eventDTO.getMaintenance()) {
			maintenanceStartDate = null;
			maintenanceStartDate = sdf.parse(dates.getStartDate());
			maintenanceEndDate = sdf.parse(dates.getEndDate());
			if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)) {
				throw new InvalidDate();
			}
			expiry.setTime(maintenanceStartDate);
			expiry.add(Calendar.DATE, -3);
			// long dateDifferenceHours = (maintenanceEndDate.getTime() -
			// maintenanceStartDate.getTime())/(60 * 60 * 1000);
			// long dateDifferenceMinutes = (maintenanceEndDate.getTime() -
			// maintenanceStartDate.getTime())/(60 * 1000) % 60;
			// if(dateDifferenceHours > 24 || dateDifferenceMinutes < 30){
			// throw new InvalidDate();
			// }
			Maintenance maintenance = new Maintenance(maintenanceStartDate, maintenanceEndDate, expiry.getTime(),
					new HashSet<LeasedZone>(), event);
			event.getMaintenances().add(maintenance);

			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate,
					maintenanceEndDate);
			if (locationEvents.isEmpty() == false) {
				throw new LocationNotAvailable();
			}

			for (LeasedZoneDTO lz : eventDTO.getLocationZones()) {
				LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
				if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
					throw new LocationZoneNotAvailable();
				}

				if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
					throw new InvalidPrice();
				}

				LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance, new HashSet<Ticket>());

				if (locationZone.isMatrix()) {
					for (int i = 1; i <= locationZone.getColNumber(); i++) {
						for (int j = 1; j <= locationZone.getRowNumber(); j++) {
							Ticket ticket = new Ticket(i, j, lz.getPrice(), false, null, newZone);
							newZone.getTickets().add(ticket);
						}
					}
				} else {
					for (int i = 0; i < locationZone.getCapacity(); i++) {
						Ticket ticket = new Ticket(0, 0, lz.getPrice(), false, null, newZone);
						newZone.getTickets().add(ticket);
					}
				}
				maintenance.getLeasedZones().add(newZone);
			}
		}
		return eventRepository.save(event);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Event update(EventDTO eventDTO) throws ParseException {
		Event event = eventRepository.getOne(eventDTO.getId());
		if (event == null) {
			throw new EventNotFound();
		}
		if (!(event.isStatus()) || !(eventIsActive(event.getId()))) {
			throw new EventNotChangeable();
		}
		if (eventDTO.getName() != null) {
			event.setName(eventDTO.getName());
		}

		event.setType(EventType.valueOf(eventDTO.getEventType()));
		if (event.getType() == null) {
			throw new InvalidEventType();
		}

		Location location = locationService.findById(eventDTO.getLocationId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}
		if (location.getId() == event.getLocationInfo().getId()) {
			return eventRepository.save(event);
		}

		if (ticketService.getEventReservedTickets(event.getId()).isEmpty() == false) {
			if (eventDTO.getLocationId() != event.getLocationInfo().getId()) {
				throw new LocationNotChangeable();
			} else {
				return eventRepository.save(event);
			}
		}
		Set<Maintenance> newMaintenances = new HashSet<Maintenance>();
		event.setLocationInfo(location);
		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		for (MaintenanceDTO dates : eventDTO.getMaintenance()) {
			maintenanceStartDate = null;
			maintenanceStartDate = sdf.parse(dates.getStartDate());
			maintenanceEndDate = sdf.parse(dates.getEndDate());
			if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)) {
				throw new InvalidDate();
			}
			expiry.setTime(maintenanceStartDate);
			expiry.add(Calendar.DATE, -3);
			// long dateDifferenceHours = (maintenanceEndDate.getTime() -
			// maintenanceStartDate.getTime())/(60 * 60 * 1000);
			// long dateDifferenceMinutes = (maintenanceEndDate.getTime() -
			// maintenanceStartDate.getTime())/(60 * 1000) % 60;
			// if(dateDifferenceHours > 24 || dateDifferenceMinutes < 30){
			// throw new InvalidDate();
			// }
			Maintenance maintenance = new Maintenance(maintenanceStartDate, maintenanceEndDate, expiry.getTime(),
					new HashSet<LeasedZone>(), event);
			newMaintenances.add(maintenance);

			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate,
					maintenanceEndDate);
			if (locationEvents.isEmpty() == false) {
				throw new LocationNotAvailable();
			}

			for (LeasedZoneDTO lz : eventDTO.getLocationZones()) {
				LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
				if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
					throw new LocationZoneNotAvailable();
				}

				if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
					throw new InvalidPrice();
				}

				LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance, new HashSet<Ticket>());

				if (locationZone.isMatrix()) {
					for (int i = 1; i <= locationZone.getColNumber(); i++) {
						for (int j = 1; j <= locationZone.getRowNumber(); j++) {
							Ticket ticket = new Ticket(i, j, lz.getPrice(), false, null, newZone);
							newZone.getTickets().add(ticket);
						}
					}
				} else {
					for (int i = 0; i < locationZone.getCapacity(); i++) {
						Ticket ticket = new Ticket(0, 0, lz.getPrice(), false, null, newZone);
						newZone.getTickets().add(ticket);
					}
				}
				maintenance.getLeasedZones().add(newZone);
			}
		}
		maintenanceService.removeByEventId(event.getId());
		event.setMaintenances(newMaintenances);
		return eventRepository.save(event);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(Long id) {
		Event event = eventRepository.getOne(id);
		if (event == null || event.isStatus() == false) {
			throw new EventNotFound();
		}
		if (!(getSoldTickets(event.getId()).isEmpty()) && eventIsActive(event.getId())) {
			throw new EventNotChangeable();
		}
		event.setStatus(false);
		eventRepository.save(event);
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

	@Override
	public boolean eventIsActive(long eventId) {
		Maintenance maintenance = maintenanceService.getLastMaintenanceOfEvent(eventId);
		if (maintenance.getMaintenanceDate().before(new Date()) || maintenance.getEvent().isStatus() == false) {
			return false;
		}
		return true;
	}

	@Override
	public EventReportDTO getEventReport(Long id) {
		Optional<Event> eventOpt = eventRepository.findById(id);
		if (eventOpt.isPresent()) {
			EventReportDTO retVal = new EventReportDTO();
			List<Reservation> res = this.reservationRepository.findAll();
			List<Reservation> ret = new ArrayList<Reservation>();
			for (Reservation r : res) {
				if (r.getEvent().getId() == id) {
					ret.add(r);
				}
			}
			long DAY_IN_MILI = 86400000;
			Date currentDate = new Date();
			DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat df2 = new SimpleDateFormat("yyyy-MM");
			Date today = null;
			try {
				today = df1.parse(df1.format(currentDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date thisMonth = null;
			try {
				thisMonth = df2.parse(df2.format(currentDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date workWith = null;
			Date workWith2 = null;
			Date startDate = null;
			// daily
			for (int i = 1; i < 8; i++) {
				int number = 0;
				workWith = new Date(today.getTime() - i * DAY_IN_MILI);
				retVal.getDailyLabels().add(df1.format(workWith));
				for (Reservation r : ret) {
					startDate = r.getDateOfReservation();
					if (df1.format(startDate).equals(df1.format(workWith))) {
						number += 1;
					}
				}
				retVal.getDailyValues().add(number);
			}
			// weekly
			for (int i = 0; i < 7; i++) {
				int number = 0;
				workWith = new Date(today.getTime() - (i * 7 + 1) * DAY_IN_MILI);
				workWith2 = new Date(today.getTime() - (7 * i + 7) * DAY_IN_MILI);
				retVal.getWeeklyLabels().add(df1.format(workWith2) + " to " + df1.format(workWith));
				for (Reservation r : ret) {
					startDate = r.getDateOfReservation();
					if (!startDate.after(workWith) && !startDate.before(workWith2)) {
						number += 1;
					}
				}
				retVal.getWeeklyValues().add(number);
			}
			// monthly
			for (int i = 0; i < 7; i++) {
				int number = 0;
				workWith = new Date(thisMonth.getTime() - DAY_IN_MILI);
				try {
					workWith2 = df2.parse(df2.format(workWith));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				retVal.getMonthlyLabels().add(df2.format(workWith2));
				for (Reservation r : ret) {
					startDate = r.getDateOfReservation();
					if (!startDate.after(workWith) && !startDate.before(workWith2)) {
						number += 1;
					}
				}
				retVal.getMonthlyValues().add(number);
				try {
					thisMonth = df2.parse(df2.format(new Date(thisMonth.getTime() - DAY_IN_MILI)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return retVal;
		} else {
			throw new EventNotFound();
		}
	}

	@Override
	public double getEventIncome(Long id) {
		Optional<Event> eventOpt = eventRepository.findById(id);
		if (eventOpt.isPresent()) {
			Event e=eventOpt.get();
			List<Reservation> reservations = this.reservationRepository.findByEvent(e);
			double income = 0;
			for (Reservation r : reservations) {
				if (r.isPaid() == true) {
					income += r.getTotalPrice();
				}
			}
			return income;
		} else {
			throw new EventNotFound();
		}
	}
}
