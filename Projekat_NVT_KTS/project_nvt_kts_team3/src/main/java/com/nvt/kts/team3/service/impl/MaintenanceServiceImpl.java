package com.nvt.kts.team3.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidLocationZone;
import exception.LocationNotAvailable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;
import exception.MaintenanceNotChangeable;
import exception.MaintenanceNotFound;

@Service
@Transactional(readOnly = true)
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	private MaintenanceRepository maintenanceRepository;

	@Autowired
	private EventService eventService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private LeasedZoneService leasedZoneService;

	@Autowired
	private LocationZoneService locationZoneService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public Maintenance findById(Long id) {
		return maintenanceRepository.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Maintenance save(MaintenanceDTO maintenanceDTO) throws ParseException {
		Event event = eventService.findById(maintenanceDTO.getEventId());
		if (event == null || !(eventService.eventIsActive(event.getId()))) {
			throw new EventNotFound();
		}

		Location location = locationService.findById(event.getLocationInfo().getId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}
		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		maintenanceStartDate = sdf.parse(maintenanceDTO.getStartDate());
		maintenanceEndDate = sdf.parse(maintenanceDTO.getEndDate());
		if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)
				|| maintenanceEndDate.before(maintenanceStartDate)) {
			throw new InvalidDate();
		}
		expiry.setTime(maintenanceStartDate);
		expiry.add(Calendar.DATE, -3);
		long dateDifferenceHours = (maintenanceEndDate.getTime() - maintenanceStartDate.getTime()) / (60 * 60 * 1000);
		long dateDifferenceMinutes = (maintenanceEndDate.getTime() - maintenanceStartDate.getTime()) / (60 * 1000) % 60;
		if (dateDifferenceHours > 24 || dateDifferenceMinutes < 30) {
			throw new InvalidDate();
		}

		ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate,
				maintenanceEndDate);
		if (locationEvents.isEmpty() == false) {
			throw new LocationNotAvailable();
		}

		Maintenance maintenance = new Maintenance();
		maintenance.setMaintenanceDate(maintenanceStartDate);
		maintenance.setMaintenanceEndTime(maintenanceEndDate);
		maintenance.setEvent(event);
		if (maintenanceDTO.getLocationZones() == null || maintenanceDTO.getLocationZones().isEmpty()) {
			throw new InvalidLocationZone();
		}

		ArrayList<Long> choosenZones = new ArrayList<Long>();

		for (LeasedZoneDTO lz : maintenanceDTO.getLocationZones()) {
			LocationZone locationZone = locationZoneService.findById(lz.getZoneId());

			if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
				throw new LocationZoneNotAvailable();
			}

			if (choosenZones.contains(lz.getZoneId()) == false) {
				LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance, new HashSet<Ticket>());
				choosenZones.add(lz.getZoneId());
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
		return maintenanceRepository.save(maintenance);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Maintenance updateMaintenance(MaintenanceDTO maintenanceDTO) throws ParseException {
		Maintenance maintenance = findById(maintenanceDTO.getId());

		if (maintenance == null) {
			throw new MaintenanceNotFound();
		}

		Event event = maintenance.getEvent();

		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();

		maintenanceStartDate = sdf.parse(maintenanceDTO.getStartDate());
		maintenanceEndDate = sdf.parse(maintenanceDTO.getEndDate());
		if (maintenanceStartDate.before(today) || maintenanceEndDate.before(maintenanceStartDate)) {
			throw new InvalidDate();
		}
		expiry.setTime(maintenanceStartDate);
		expiry.add(Calendar.DATE, -3);
		long dateDifferenceHours = (maintenanceEndDate.getTime() - maintenanceStartDate.getTime()) / (60 * 60 * 1000);
		long dateDifferenceMinutes = (maintenanceEndDate.getTime() - maintenanceStartDate.getTime()) / (60 * 1000) % 60;
		if (dateDifferenceHours > 24 || dateDifferenceMinutes < 30) {
			throw new InvalidDate();
		}
		maintenance.setMaintenanceDate(maintenanceStartDate);
		maintenance.setMaintenanceEndTime(maintenanceEndDate);
		maintenance.setReservationExpiry(expiry.getTime());

		Location location = locationService.findById(event.getLocationInfo().getId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}

		if (location.getId() != maintenance.getEvent().getId()) {
			if (!(ticketService.getMaintenanceReservedTickets(maintenance.getId()).isEmpty())) {
				throw new MaintenanceNotChangeable();
			}
			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate,
					maintenanceEndDate);
			if (locationEvents.isEmpty() == false) {
				throw new LocationNotAvailable();
			}

			leasedZoneService.deleteByMaintenanceId(maintenance.getId());
			ArrayList<Long> choosenZones = new ArrayList<Long>();
			for (LeasedZoneDTO lz : maintenanceDTO.getLocationZones()) {
				LocationZone locationZone = locationZoneService.findById(lz.getZoneId());

				if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
					throw new LocationZoneNotAvailable();
				}

				if (choosenZones.contains(lz.getZoneId()) == false) {
					LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance,
							new HashSet<Ticket>());
					choosenZones.add(lz.getZoneId());
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
		}
		return maintenanceRepository.save(maintenance);
	}

	@Override
	public List<Maintenance> findAll() {
		return maintenanceRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(long id) {
		Maintenance maintenance = findById(id);
		if (maintenance == null) {
			throw new MaintenanceNotFound();
		}
		if (!(ticketService.getMaintenanceReservedTickets(id).isEmpty())) {
			throw new MaintenanceNotChangeable();
		}
		maintenanceRepository.deleteById(maintenance.getId());
	}

	@Override
	public Maintenance getLastMaintenanceOfEvent(long eventId) {
		return maintenanceRepository.getLastMaintenanceOfEvent(eventId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<Maintenance> removeByEventId(long eventId) {
		return maintenanceRepository.deleteByEventId(eventId);
	}
}
