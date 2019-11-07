package com.nvt.kts.team3.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LeasedZoneController {
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private LeasedZoneService leasedZoneService;
	
	@Autowired
	private LocationZoneService locationZoneService;
	
	@PostMapping(value = "/createLeasedZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createLeasedZone(@RequestBody LeasedZoneDTO lz){
		Maintenance maintenance = maintenanceService.findById(lz.getMaintenanceId());
		if(maintenance == null || eventIsActive(maintenance.getEvent().getId()) == false){
			return new ResponseEntity<>(new MessageDTO("Unable to create leased zone", "Maintenance with this ID does not exist or choosen event is not active."), HttpStatus.NOT_FOUND);
		}
		LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
		if(locationZone == null || locationZone.getLocation().getId() != maintenance.getEvent().getLocationInfo().getId()){
			return new ResponseEntity<>(new MessageDTO("Unable to create leased zone", "ID of location zone is not valid. Please make sure to choose location zone that exists and belongs to event's location."), HttpStatus.OK);
		}
		LeasedZone newZone = new LeasedZone();
		newZone.setZone(locationZone);
		if(lz.getPrice() < 1 || lz.getPrice() > 10000){
			return new ResponseEntity<>(new MessageDTO("Unable to create leased zone", "Please set a price of ticket that is between 1$ and 5000$."), HttpStatus.OK);
		}
		newZone.setSeatPrice(lz.getPrice());
		
		if(locationZone.isMatrix()){
			for(int i = 1; i <= locationZone.getColNumber(); i++){
				for(int j = 1; j <= locationZone.getRowNumber(); j++){
					Ticket ticket = new Ticket();
					ticket.setCol(i);
					ticket.setRow(j);
					ticket.setPrice(lz.getPrice());
					ticket.setReserved(false);
					ticket.setZone(newZone);
					newZone.getTickets().add(ticket);
				}
			}
		}
		else{
			for(int i = 0; i < locationZone.getCapacity(); i++){
				Ticket ticket = new Ticket();
				ticket.setCol(0);
				ticket.setRow(0);
				ticket.setPrice(lz.getPrice());
				ticket.setReserved(false);
				ticket.setZone(newZone);
				newZone.getTickets().add(ticket);
			}
		}
		newZone.setMaintenance(maintenance);
		maintenance.getLeasedZones().add(newZone);
		leasedZoneService.saveAndFlush(newZone);
		maintenanceService.saveAndFlush(maintenance);
		return new ResponseEntity<>(new MessageDTO("Success", "Leased zone successfuly added!"), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateLeasedZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateLeasedZone(@RequestBody LeasedZoneDTO lz){
		LeasedZone newZone = leasedZoneService.findById(lz.getId());
		if(newZone == null){
			return new ResponseEntity<>(new MessageDTO("Unable to update leased zone", "Leased zone with this ID does not exist."), HttpStatus.NOT_FOUND);
		}
		Maintenance maintenance = maintenanceService.findById(lz.getMaintenanceId());
		if(maintenance == null || eventIsActive(maintenance.getEvent().getId()) == false){
			return new ResponseEntity<>(new MessageDTO("Unable to update leased zone", "Maintenance with this ID does not exist or choosen event is not active."), HttpStatus.NOT_FOUND);
		}
		LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
		if(locationZone == null || locationZone.getLocation().getId() != maintenance.getEvent().getLocationInfo().getId()){
			return new ResponseEntity<>(new MessageDTO("Unable to update leased zone", "ID of location zone is not valid. Please make sure to choose location zone that exists and belongs to event's location."), HttpStatus.OK);
		}
		
		newZone.setZone(locationZone);
		if(lz.getPrice() < 1 || lz.getPrice() > 10000){
			return new ResponseEntity<>(new MessageDTO("Unable to update leased zone", "Please set a price of ticket that is between 1$ and 5000$."), HttpStatus.OK);
		}
		newZone.setSeatPrice(lz.getPrice());
		
		if(locationZone.isMatrix()){
			for(int i = 1; i <= locationZone.getColNumber(); i++){
				for(int j = 1; j <= locationZone.getRowNumber(); j++){
					Ticket ticket = new Ticket();
					ticket.setCol(i);
					ticket.setRow(j);
					ticket.setPrice(lz.getPrice());
					ticket.setReserved(false);
					ticket.setZone(newZone);
					newZone.getTickets().add(ticket);
				}
			}
		}
		else{
			for(int i = 0; i < locationZone.getCapacity(); i++){
				Ticket ticket = new Ticket();
				ticket.setCol(0);
				ticket.setRow(0);
				ticket.setPrice(lz.getPrice());
				ticket.setReserved(false);
				ticket.setZone(newZone);
				newZone.getTickets().add(ticket);
			}
		}
		newZone.setMaintenance(maintenance);
		maintenance.getLeasedZones().add(newZone);
		leasedZoneService.saveAndFlush(newZone);
		maintenanceService.saveAndFlush(maintenance);
		return new ResponseEntity<>(new MessageDTO("Success", "Leased zone successfuly updated!"), HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/getLeasedZone{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LeasedZone> getLeasedZone(@PathVariable(value = "id") Long id){
		LeasedZone lz = leasedZoneService.findById(id);
		if(lz == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(lz, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLeasedZones{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<LeasedZone>> getLeasedZones(@PathVariable(value = "maintenanceId") Long maintenanceId){
		Maintenance maintenance = maintenanceService.findById(maintenanceId);
		if(maintenance == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(maintenance.getLeasedZones(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventLeasedZones{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<LeasedZone>> getEventLeasedZones(@PathVariable(value = "eventId") Long eventId){
		return new ResponseEntity<>(leasedZoneService.getEventLeasedZones(eventId), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteLeasedZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteEvent(@PathVariable(value = "id") Long id){
		LeasedZone newZone = leasedZoneService.findById(id);
		if(newZone == null){
			return new ResponseEntity<>(new MessageDTO("Unable to delete leased zone", "Leased zone with this ID does not exist."), HttpStatus.NOT_FOUND);
		}
		if(ticketService.getLeasedZoneReservedTickets(id).isEmpty() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to delete leased zone", "There are tickets that are reserved for this zone, so it can not be deleted."), HttpStatus.OK);
		}
		for(Ticket ticket : newZone.getTickets()){
			ticketService.remove(ticket.getId());
		}
		newZone.setMaintenance(null);
		newZone.setZone(null);
		leasedZoneService.save(newZone);
		leasedZoneService.remove(newZone.getId());
		return new ResponseEntity<>(new MessageDTO("Success", "Leased zone successfuly deleted."), HttpStatus.OK);
	}
	
	private boolean eventIsActive(long eventId){
		Maintenance maintenance = maintenanceService.getLastMaintenanceOfEvent(eventId);
		if(maintenance.getMaintenanceDate().before(new Date()) || maintenance.getEvent().isStatus() == false){
			return false;
		}
		return true;
	}
}
