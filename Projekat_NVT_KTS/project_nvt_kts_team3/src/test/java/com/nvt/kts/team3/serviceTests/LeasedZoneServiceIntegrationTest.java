package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.service.LeasedZoneService;

import exception.EventNotActive;
import exception.EventNotFound;
import exception.InvalidPrice;
import exception.LeasedZoneNotChangeable;
import exception.LeasedZoneNotFound;
import exception.LocationZoneNotAvailable;
import exception.LocationZoneNotFound;
import exception.MaintenanceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LeasedZoneServiceIntegrationTest {
	
	@Autowired
	public MaintenanceRepository maintenanceRepository;
	
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Autowired
	public TicketRepository ticketRepository;
	
	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public LeasedZoneRepository leasedZoneRepository;
	
	@Autowired
	public LeasedZoneService leasedZoneService;
	
	private static final long NONEXISTENT_ID = 10000L;
	private static final long INACTIVE_EVENT_MAINTENANCE_ID = 6L;
	private static final long VALID_MAINTENANCE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	
	@Test(expected = MaintenanceNotFound.class)
	@Transactional
	public void test_create_MaintenanceNotFound(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(NONEXISTENT_ID);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = EventNotActive.class)
	@Transactional
	public void test_create_EventNotActive(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(INACTIVE_EVENT_MAINTENANCE_ID);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	@Transactional
	public void test_create_LocationZoneNotFound(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(NONEXISTENT_ID);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	@Transactional
	public void test_create_LocationZoneNotAvailable(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(INVALID_LOCATION_ZONE_ID);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_create_InvalidPrice_1(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(1L);
		request.setPrice(INVALID_HIGH_PRICE);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_create_InvalidPrice_2(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(1L);
		request.setPrice(INVALID_LOW_PRICE);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LeasedZoneNotFound.class)
	@Transactional
	public void test_update_LeasedZoneNotFound(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(NONEXISTENT_ID);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = EventNotActive.class)
	@Transactional
	public void test_update_EventNotActive(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(9L);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	@Transactional
	public void test_update_LocationZoneNotFound(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(NONEXISTENT_ID);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	@Transactional
	public void test_update_LocationZoneNotAvailable(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(2L);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LeasedZoneNotChangeable.class)
	@Transactional
	public void test_update_LeasedZoneNotChangeable(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(1L);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_update_InvalidPrice_1(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(8L);
		request.setZoneId(8L);
		request.setPrice(INVALID_HIGH_PRICE);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_update_InvalidPrice_2(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(8L);
		request.setZoneId(8L);
		request.setPrice(INVALID_LOW_PRICE);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LeasedZoneNotFound.class)
	@Transactional
	public void test_remove_LeasedZoneNotFound(){
		leasedZoneService.remove(NONEXISTENT_ID);
	}
	
	@Test(expected = EventNotActive.class)
	@Transactional
	public void test_remove_EventNotActive(){
		leasedZoneService.remove(9L);
	}
	
	@Test(expected = LeasedZoneNotChangeable.class)
	@Transactional
	public void test_remove_LeasedZoneNotChangeable(){
		leasedZoneService.remove(1L);
	}
	
	@Transactional
	@Test
	public void test_getLeasedZone_NotFound(){
		LeasedZone lz = leasedZoneService.findById(NONEXISTENT_ID);
		
		assertNull(lz);
	}
	
	@Transactional
	@Test
	public void test_getLeasedZone_Success(){
		LeasedZone lz = leasedZoneService.findById(1L);
		
		assertNotNull(lz);
		assertEquals(1L, lz.getId());
	}
	
	@Test(expected = EventNotFound.class)
	@Transactional
	public void test_getEventLeasedZones_EventNotFound(){
		leasedZoneService.getEventLeasedZones(NONEXISTENT_ID);
	}
	
	@Transactional
	@Test
	public void test_getEventLeasedZones_Success(){
		ArrayList<LeasedZone> zones = leasedZoneService.getEventLeasedZones(1L);
		
		Optional<Event> event = eventRepository.findById(1L);
		
		List<Long> ids = new ArrayList<Long>();
		
		int leasedZonesNum = 0;
		for(Maintenance m : event.get().getMaintenances()){
			leasedZonesNum += m.getLeasedZones().size();
			for(LeasedZone lz : m.getLeasedZones()){
				ids.add(lz.getId());
			}
		}
		
		assertEquals(leasedZonesNum, zones.size());
		for(LeasedZone lz : zones){
			assertTrue(ids.contains(lz.getId()));
		}
	}
	
	@Test(expected = MaintenanceNotFound.class)
	@Transactional
	public void test_deleteByMaintenanceId_MaintenanceNotFound(){
		leasedZoneService.deleteByMaintenanceId(NONEXISTENT_ID);
	}
	
}
