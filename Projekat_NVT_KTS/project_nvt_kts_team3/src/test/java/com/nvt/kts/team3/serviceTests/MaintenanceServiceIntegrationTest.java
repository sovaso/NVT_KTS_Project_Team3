package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.service.MaintenanceService;

import exception.EventNotActive;
import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidLocationZone;
import exception.InvalidPrice;
import exception.LocationNotAvailable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;
import exception.MaintenanceNotChangeable;
import exception.MaintenanceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaintenanceServiceIntegrationTest {

	@Autowired
	public MaintenanceRepository maintenanceRepository;
	
	@Autowired
	public MaintenanceService maintenanceService;
	
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Autowired
	public TicketRepository ticketRepository;
	
	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public LeasedZoneRepository leasedZoneRepository;
	
	private static final long NONEXISTENT_EVENT_ID = 10000L;
	private static final long INACTIVE_EVENT_ID = 4L;
	private static final long INACTIVE_LOCATION_EVENT_ID = 10L;
	private static final long VALID_EVENT_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	private static final long ID = 0L;
	
	private static final String VALID_START_DATE = "2021-01-01 22:00";
	private static final String VALID_END_DATE = "2021-01-01 23:00";
	private static final String VALID_START_DATE_2 = "2022-01-01 22:00";
	private static final String VALID_END_DATE_2 = "2022-01-01 23:00";
	private static final String INVALID_START_DATE = "2021-01-15 21:00";
	private static final String INVALID_END_DATE = "2021-01-15 23:00";
	//private static final String VALID_EXPIRY_DATE = "2020-12-12 23:00";
	private static final String EXPIERED_START_DATE = "2018-01-01 22:00";
	private static final String EXPIERED_END_DATE = "2018-01-01 23:00";
	private static final String MAINTENANCE_25H_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_25H_END = "2021-01-02 23:00";
	private static final String MAINTENANCE_30MIN_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_30MIN_END = "2021-01-01 22:25";
	private static final String BAD_FORMAT_START_DATE = "2021.01.01, 22:00";
	private static final String BAD_FORMAT_END_DATE = "2021.01.01, 23:00";
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	@Test(expected = EventNotFound.class)
	@Transactional
	public void test_createMaintenance_EventNotFound() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(NONEXISTENT_EVENT_ID);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	@Transactional
	public void test_createMaintenance_EventNotChangeable() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(INACTIVE_EVENT_ID);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void test_createMaintenance_LocationNotFound() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(INACTIVE_LOCATION_EVENT_ID);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_createMaintenance_InvalidDate_1() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_createMaintenance_InvalidDate_2() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_createMaintenance_InvalidDate_3() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_createMaintenance_InvalidDate_4() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = ParseException.class)
	@Transactional
	public void test_createMaintenance_InvalidDate_5() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	@Transactional
	public void test_createMaintenance_LocationNotAvailable() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void test_createMaintenance_InvalidLocationZone() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				null);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	@Transactional
	public void test_createMaintenance_LocationZoneNotAvailable() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_createMaintenance_InvalidPrice_1() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_createMaintenance_InvalidPrice_2() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.save(request);
	}
	
	/*
	@Test
	@Transactional
	public void test_createMaintenance_Success() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE_2,
				VALID_END_DATE_2,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getMessage());
		assertEquals("Maintenance successfully created.", responseEntity.getBody().getHeader());
		
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE_2)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE_2)),df);
		
		ArrayList<Maintenance> savedMaintenance = maintenanceRepository.getMaintenancesForDate(1L, startDate, endDate);
		assertEquals(savedMaintenance.size(), 1);
		
		Maintenance m = savedMaintenance.get(0);
		assertEquals(startDate, m.getMaintenanceDate());
		assertEquals(endDate, m.getMaintenanceEndTime());
		assertEquals(m.getEvent().getId(), VALID_EVENT_ID);
		
		Iterator<LeasedZone> iterator = m.getLeasedZones().iterator();
		LeasedZone savedZone = iterator.next();
		assertEquals(LOCATION_ZONE_ID, savedZone.getZone().getId());
		assertEquals(savedZone.getTickets().size(), savedZone.getZone().getCapacity());
		
		test_remove_Success(m.getId());
	}
	*/
	
	@Test(expected = MaintenanceNotFound.class)
	@Transactional
	public void test_updateMaintenance_MaintenanceNotFound() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(NONEXISTENT_EVENT_ID);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	@Transactional
	public void test_updateMaintenance_EventNotChangeable() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(6L);
		request.setEventId(INACTIVE_EVENT_ID);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void test_updateMaintenance_LocationNotFound() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(14L);
		request.setEventId(INACTIVE_LOCATION_EVENT_ID);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_updateMaintenance_InvalidDate_1() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_updateMaintenance_InvalidDate_2() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_updateMaintenance_InvalidDate_3() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	@Transactional
	public void test_updateMaintenance_InvalidDate_4() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = ParseException.class)
	@Transactional
	public void test_updateMaintenance_InvalidDate_5() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = MaintenanceNotChangeable.class)
	@Transactional
	public void test_updateMaintenance_MaintenanceNotChangeable() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	@Transactional
	public void test_updateMaintenance_LocationNotAvailable() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void test_updateMaintenance_InvalidLocationZone() throws ParseException {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				null);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	@Transactional
	public void test_updateMaintenance_LocationZoneNotAvailable() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_updateMaintenance_InvalidPrice_1() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidPrice.class)
	@Transactional
	public void test_updateMaintenance_InvalidPrice_2() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = MaintenanceNotFound.class)
	@Transactional
	public void test_remove_MaintenanceNotFound() {
		maintenanceService.remove(NONEXISTENT_EVENT_ID);
		
	}
	
	@Test(expected = EventNotActive.class)
	@Transactional
	public void test_remove_EventNotActive() {
		maintenanceService.remove(6L);
	}
	
	@Test(expected = MaintenanceNotChangeable.class)
	@Transactional
	public void test_remove_MaintenanceNotChangeable() {
		maintenanceService.remove(1L);
	}
	
	/*
	@Test
	public void test_remove_Success(long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteMaintenance/"+id, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getMessage());
		assertEquals("Maintenance successfully deleted.", responseEntity.getBody().getHeader());
	}
	*/
	
	@Transactional
	public void test_getMaintenance_MaintenanceNotFound() {
		Maintenance maintenance = maintenanceService.findById(1000L);
		
		assertNull(maintenance);
	}
	
	@Transactional
	public void test_getMaintenance_Success() {
		Maintenance maintenance = maintenanceService.findById(1L);
		
		assertNotNull(maintenance);
		assertEquals(1L, maintenance.getId());
	}
}
