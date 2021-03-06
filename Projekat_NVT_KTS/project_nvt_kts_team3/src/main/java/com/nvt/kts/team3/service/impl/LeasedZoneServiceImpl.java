package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.LeasedZoneUpdatable;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotActive;
import exception.EventNotFound;
import exception.InvalidLocationZone;
import exception.InvalidPrice;
import exception.LeasedZoneNotChangeable;
import exception.LeasedZoneNotFound;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;
import exception.LocationZoneNotFound;
import exception.MaintenanceNotFound;

@Service
//@Transactional(readOnly = true)
public class LeasedZoneServiceImpl implements LeasedZoneService {

	@Autowired
	private LeasedZoneRepository leasedZoneRepository;

	@Autowired
	private MaintenanceService maintenanceService;

	@Autowired
	private EventService eventService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private LocationZoneService locationZoneService;

	@Override
	public LeasedZone findById(Long id) {
		Optional<LeasedZone> leasedZone = leasedZoneRepository.findById(id);
		if(leasedZone.isPresent()){
			return leasedZone.get();
		}
		return null;
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public LeasedZone save(LeasedZoneDTO lz) {
		System.out.println("USAO SAM");
		Maintenance maintenance = maintenanceService.findById(lz.getMaintenanceId());
		if (maintenance == null) {
			throw new MaintenanceNotFound();
		}
		if(eventService.eventIsActive(maintenance.getEvent().getId()) == false){
			throw new EventNotActive();
		}
		LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
		if(locationZone == null){
			throw new LocationZoneNotFound();
		}
		
		if (locationZone.getLocation().getId() != maintenance.getEvent().getLocationInfo().getId()) {
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
		return leasedZoneRepository.save(newZone);
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public LeasedZone update(LeasedZoneDTO lz) {
		LeasedZone newZone = findById(lz.getId());
		if (newZone == null) {
			throw new LeasedZoneNotFound();
		}
		
		Maintenance maintenance = newZone.getMaintenance();
		if(eventService.eventIsActive(maintenance.getEvent().getId()) == false){
			throw new EventNotActive();
		}
		
		LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
		if(locationZone == null){
			throw new LocationZoneNotFound();
		}
		
		if (locationZone.getLocation().getId() != maintenance.getEvent().getLocationInfo().getId()) {
			throw new LocationZoneNotAvailable();
		}
			
		if (ticketService.getLeasedZoneReservedTickets(lz.getId()).isEmpty() == false) {
			throw new LeasedZoneNotChangeable();
		}

		if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
			throw new InvalidPrice();
		}
		else{
			newZone.setSeatPrice(lz.getPrice());
		}
		
		for(Ticket t : newZone.getTickets()){
			t.setPrice(lz.getPrice());
		}
		
		newZone.setMaintenance(maintenance);
		maintenance.getLeasedZones().add(newZone);
		return leasedZoneRepository.save(newZone);
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(Long id) {
		LeasedZone newZone = findById(id);
		if (newZone == null) {
			throw new LeasedZoneNotFound();
		}
		Maintenance m = maintenanceService.findById(newZone.getMaintenance().getId());
		if(eventService.eventIsActive(m.getEvent().getId()) == false){
			throw new EventNotActive();
		}
		if (ticketService.getLeasedZoneReservedTickets(id).isEmpty() == false) {
			throw new LeasedZoneNotChangeable();
		}
		ticketService.deleteByZoneId(id);
		leasedZoneRepository.deleteById(newZone.getId());
	}

	@Override
	public List<LeasedZone> findAll() {
		return leasedZoneRepository.findAll();
	}

	@Override
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId) {
		Event event = eventService.findById(eventId);
		if(event == null){
			throw new EventNotFound();
		}
		return leasedZoneRepository.getEventLeasedZones(eventId);
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<LeasedZone> deleteByMaintenanceId(long maintenanceId) {
		Maintenance maintenance = maintenanceService.findById(maintenanceId);
		if(maintenance == null){
			throw new MaintenanceNotFound();
		}
		return leasedZoneRepository.deleteByMaintenanceId(maintenanceId);
	}

	@Override
	public ArrayList<LeasedZoneUpdatable> getEventLeasedZonesDto(long eventId) {
		ArrayList<LeasedZone> zones = leasedZoneRepository.getEventLeasedZones(eventId);
		ArrayList<LeasedZoneUpdatable> zonesDto = new ArrayList<LeasedZoneUpdatable>();
		for(LeasedZone zone : zones){
			LeasedZoneUpdatable lz = new LeasedZoneUpdatable();
			if (ticketService.getLeasedZoneReservedTickets(zone.getId()).isEmpty() == false) {
				lz.setUpdatable(false);
			}
			else{
				lz.setUpdatable(true);
			}
			lz.setId(zone.getId());
			lz.setMaintenanceId(zone.getMaintenance().getId());
			lz.setPrice(zone.getSeatPrice());
			lz.setZoneId(zone.getZone().getId());
			zonesDto.add(lz);
		}
		return zonesDto;
	}

}