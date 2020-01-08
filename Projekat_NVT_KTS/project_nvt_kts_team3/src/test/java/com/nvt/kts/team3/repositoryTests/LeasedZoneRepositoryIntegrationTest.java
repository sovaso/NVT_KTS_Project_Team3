package com.nvt.kts.team3.repositoryTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LeasedZoneRepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	public LeasedZoneRepository leasedZoneRepository;
	
	@Autowired
	public LocationRepository locationRepository;
	
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public TicketRepository ticketRepository;
	
	@Autowired
	public MaintenanceRepository maintenanceRepository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private static final long LOCATION_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static LeasedZone LEASED_ZONE_1 = null;
	private static LeasedZone LEASED_ZONE_2 = null;
	private static Event EVENT = null;
	
	private static final Date START_DATE_1 = new Date(System.currentTimeMillis() + 93000000);
	private static final Date END_DATE_1 = new Date(System.currentTimeMillis() + 95000000);
	private static final Date EXPIRY_DATE_1 = new Date(System.currentTimeMillis() + 88200000);
	
	private static final Date START_DATE_2 = new Date(System.currentTimeMillis() + 90000000);
	private static final Date END_DATE_2 = new Date(System.currentTimeMillis() + 92000000);
	private static final Date EXPIRY_DATE_2 = new Date(System.currentTimeMillis() + 84600000);
	
	@Before
	public void prepareTestData() throws ParseException{
		Location location = locationRepository.getOne(LOCATION_ID);
		
		Event event = new Event();
		event.setName("EXIT 2020");
		event.setStatus(true);
		event.setType(EventType.ENTERTAINMENT);
		event.setLocationInfo(location);
		
		LocalDateTime startDate1 = LocalDateTime.parse(sdf.format(START_DATE_1),df);
		LocalDateTime endDate1 = LocalDateTime.parse(sdf.format(END_DATE_1),df);
		LocalDateTime expiryDate1 = LocalDateTime.parse(sdf.format(EXPIRY_DATE_1),df);
		
		LocalDateTime startDate2 = LocalDateTime.parse(sdf.format(START_DATE_2),df);
		LocalDateTime endDate2 = LocalDateTime.parse(sdf.format(END_DATE_2),df);
		LocalDateTime expiryDate2 = LocalDateTime.parse(sdf.format(EXPIRY_DATE_2),df);
		
		Maintenance maintenance1 = new Maintenance(
				startDate1,
				endDate1,
				expiryDate1,
				new HashSet<LeasedZone>(),
				event);
		Maintenance maintenance2 = new Maintenance(
				startDate2,
				endDate2,
				expiryDate2,
				new HashSet<LeasedZone>(),
				event);
		
		LocationZone locationZone = locationZoneRepository.getOne(LOCATION_ZONE_ID); 
		
		LeasedZone lz1 = new LeasedZone();
		lz1.setSeatPrice(30L);
		lz1.setZone(locationZone);
		lz1.setMaintenance(maintenance1);
		lz1.getTickets().add(new Ticket(1,1,30L,false,null,lz1));
		maintenance1.getLeasedZones().add(lz1);
		
		LeasedZone lz2 = new LeasedZone();
		lz2.setSeatPrice(30L);
		lz2.setZone(locationZone);
		lz2.setMaintenance(maintenance2);
		lz2.getTickets().add(new Ticket(1,1,30L,false,null,lz2));
		maintenance2.getLeasedZones().add(lz2);
		
		
		event.getMaintenances().add(maintenance1);
		event.getMaintenances().add(maintenance2);
		
		EVENT = eventRepository.save(event);
		
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance m1 = (Maintenance) iterator.next();
		Maintenance m2 = (Maintenance) iterator.next();
		
		Iterator<LeasedZone> m1_iterator = m1.getLeasedZones().iterator();
		Iterator<LeasedZone> m2_iterator = m2.getLeasedZones().iterator();
		
		LEASED_ZONE_1 = m1_iterator.next();
		LEASED_ZONE_2 = m2_iterator.next();
	}
	
	@Test
	public void test_getEventLeasedZones(){
		ArrayList<LeasedZone> leasedZones = leasedZoneRepository.getEventLeasedZones(EVENT.getId());
		
		int numberOfLeasedZones = 0;
		for(Maintenance m : EVENT.getMaintenances()){
			numberOfLeasedZones+= m.getLeasedZones().size();
		}
		
		assertEquals(numberOfLeasedZones, leasedZones.size());
		assertEquals(LEASED_ZONE_1.getId(), leasedZones.get(0).getId());
		assertEquals(LEASED_ZONE_2.getId(), leasedZones.get(1).getId());
	}
	
	@Test
	@Transactional
	@Modifying
	public void test_deleteByMaintenanceId(){
		List<LeasedZone> deletedZones = leasedZoneRepository.deleteByMaintenanceId(LEASED_ZONE_1.getMaintenance().getId());
		assertEquals(deletedZones.size(), 1);
		assertEquals(LEASED_ZONE_1.getId(), deletedZones.get(0).getId());
		
		boolean deleted = false;
		try {
			leasedZoneRepository.getOne(LEASED_ZONE_1.getId());
		} catch (Exception e){
			deleted = true;
	    }
		assertTrue(deleted);
		
		int deletedTickets = 0;
		for(Ticket t : LEASED_ZONE_1.getTickets()){
			try {
				ticketRepository.getOne(t.getId());
			} catch (Exception e){
				deletedTickets++;
		    }
		}
		assertEquals(deletedTickets, deletedZones.get(0).getTickets().size());
	}
	
	@After
	public void removeTestData(){
		eventRepository.deleteById(EVENT.getId());
	}
}
