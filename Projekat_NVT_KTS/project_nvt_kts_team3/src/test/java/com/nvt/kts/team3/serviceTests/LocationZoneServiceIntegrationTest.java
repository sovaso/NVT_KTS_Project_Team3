package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.LocationNotFound;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationZoneServiceIntegrationTest {

	@Autowired
	private LocationZoneService locationZoneService;
	/*
	@Test
	@Transactional
	public void getActiveMaintenances_successfull() {
		ArrayList<Maintenance> maintenances = locationZoneService.getActiveMaintenances(1L);
		assertEquals(1, maintenances.size());
		assertEquals(1, maintenances.get(0).getId());
	}
	
	@Test
	@Transactional
	public void findAll_successfull() {
		List<LocationZone> maintenances = locationZoneService.findAll();
		assertEquals(1, maintenances.size());
		assertEquals(1, maintenances.get(0).getId());
	}
	*/
	@Test
	@Transactional
	public void findById_successfull() {
		LocationZone location = locationZoneService.findById(1L);
		assertEquals(200, location.getCapacity());
		assertEquals(20, location.getColNumber());
		assertEquals(10, location.getRowNumber());
		assertEquals("Name1", location.getName());
		assertEquals(1, location.getId());
	}
	

	@Transactional
	public void findById_null() {
		LocationZone location = locationZoneService.findById(1000L);
		assertNull(location);
	}
	
	@Test
	@Transactional
	public void save_locationNotFound(){
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setLocationId(1L);
		locationZoneDto.setMatrix(true);
		locationZoneDto.setCol(20);
		locationZoneDto.setRow(10);
		locationZoneDto.setName("SomeName");
		LocationZone lz = locationZoneService.save(locationZoneDto);
		assertEquals("SomeName", lz.getName());
	}
	
}
