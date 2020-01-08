package com.nvt.kts.team3.repositoryTests;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
public class MaintenanceRepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public MaintenanceRepository maintenanceRepository;
	
	@Autowired
	public LocationRepository locationRepository;
	
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Autowired
	public LeasedZoneRepository leasedZoneRepository;
	
	@Autowired
	public TicketRepository ticketRepository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private static final long LOCATION_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static Event EVENT = null;
	
	//Expiry date je 23.5h od sadasnjeg trenutka
	private static final Date WARNING_START_DATE_2 = new Date(System.currentTimeMillis() + 90000000);
	private static final Date WARNING_END_DATE_2 = new Date(System.currentTimeMillis() + 92000000);
	private static final Date WARNING_EXPIRY_DATE_2 = new Date(System.currentTimeMillis() + 84600000);
	
	//Expiry date je 24.5h od sadasnjeg trenutka
	//Ovaj maintenance bi jedini trebao da bude detektovan kao "warning"
	private static final Date WARNING_START_DATE_1 = new Date(System.currentTimeMillis() + 93000000);
	private static final Date WARNING_END_DATE_1 = new Date(System.currentTimeMillis() + 95000000);
	private static final Date WARNING_EXPIRY_DATE_1 = new Date(System.currentTimeMillis() + 88200000);
	
	//Pocetni i krajnji datum za najkasniji maintenance eventa koji dodajemo u svrhu testiranja
	//Expiry date je 25.1h od sadasnjeg trenutka
	private static final Date WARNING_START_DATE_3 = new Date(System.currentTimeMillis() + 96000000);
	private static final Date WARNING_END_DATE_3 = new Date(System.currentTimeMillis() + 98000000);
	private static final Date WARNING_EXPIRY_DATE_3 = new Date(System.currentTimeMillis() + 90360000);
	
	private static final int WARNING_MAINTENANCES_NUM = 1;
	
	@Before
	public void prepareData() throws ParseException{
		Location location = locationRepository.getOne(LOCATION_ID);
		
		Event event = new Event();
		event.setName("EXIT 2020");
		event.setStatus(true);
		event.setType(EventType.ENTERTAINMENT);
		event.setLocationInfo(location);
		
		LocalDateTime startDate1 = LocalDateTime.parse(sdf.format(WARNING_START_DATE_1),df);
		LocalDateTime endDate1 = LocalDateTime.parse(sdf.format(WARNING_END_DATE_1),df);
		LocalDateTime expiryDate1 = LocalDateTime.parse(sdf.format(WARNING_EXPIRY_DATE_1),df);
		
		LocalDateTime startDate2 = LocalDateTime.parse(sdf.format(WARNING_START_DATE_2),df);
		LocalDateTime endDate2 = LocalDateTime.parse(sdf.format(WARNING_END_DATE_2),df);
		LocalDateTime expiryDate2 = LocalDateTime.parse(sdf.format(WARNING_EXPIRY_DATE_2),df);
		
		LocalDateTime startDate3 = LocalDateTime.parse(sdf.format(WARNING_START_DATE_3),df);
		LocalDateTime endDate3 = LocalDateTime.parse(sdf.format(WARNING_END_DATE_3),df);
		LocalDateTime expiryDate3 = LocalDateTime.parse(sdf.format(WARNING_EXPIRY_DATE_3),df);
		
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
		Maintenance maintenance3 = new Maintenance(
				startDate3,
				endDate3,
				expiryDate3,
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
		
		LeasedZone lz3 = new LeasedZone();
		lz3.setSeatPrice(30L);
		lz3.setZone(locationZone);
		lz3.setMaintenance(maintenance3);
		lz3.getTickets().add(new Ticket(1,1,30L,false,null,lz3));
		maintenance3.getLeasedZones().add(lz3);
		
		event.getMaintenances().add(maintenance1);
		event.getMaintenances().add(maintenance2);
		event.getMaintenances().add(maintenance3);
		
		EVENT = eventRepository.save(event);
	}
	
	@Test
	public void test_getLastMaintenanceOfEvent(){
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(WARNING_START_DATE_3),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(WARNING_END_DATE_3),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(WARNING_EXPIRY_DATE_3),df);
		
		Maintenance lastMaintenance = maintenanceRepository.getLastMaintenanceOfEvent(EVENT.getId());
		assertEquals(lastMaintenance.getMaintenanceDate(), startDate);
		assertEquals(lastMaintenance.getMaintenanceEndTime(), endDate);
		assertEquals(lastMaintenance.getReservationExpiry(), expiryDate);
	}
	
	@Test
	public void test_getWarningMaintenances(){
		Calendar next24hours = Calendar.getInstance();
		Calendar next25hours = Calendar.getInstance();
		next24hours.setTime(new Date());
		next24hours.add(Calendar.HOUR, 24);
		next25hours.setTime(new Date());
		next25hours.add(Calendar.HOUR, 25);
		
		List<Maintenance> warningMaintenances = maintenanceRepository.getWarningMaintenances(LocalDateTime.parse(sdf.format(next24hours.getTime()), df), LocalDateTime.parse(sdf.format(next25hours.getTime()), df));
		
		assertEquals(WARNING_MAINTENANCES_NUM, warningMaintenances.size());
		
		//Datumi maintenance-a koji bi trebao biti detektovan kao "warning"
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(WARNING_START_DATE_1),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(WARNING_END_DATE_1),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(WARNING_EXPIRY_DATE_1),df);
		
		for(Maintenance m : warningMaintenances){
			assertEquals(m.getMaintenanceDate(), startDate);
			assertEquals(m.getMaintenanceEndTime(), endDate);
			assertEquals(m.getReservationExpiry(), expiryDate);
		}
	}
	
	//Slucaj 1: Kada maintenance ima identicne datume kao one koje smo prosledili 
	@Test
	public void test_getMaintenancesForDate_1(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = (Maintenance) iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceDate(),
				maintenance.getMaintenanceEndTime());
		
		assertEquals(1, maintenances.size());
		assertEquals(maintenances.get(0).getMaintenanceDate(), maintenance.getMaintenanceDate());
		assertEquals(maintenances.get(0).getMaintenanceEndTime(), maintenance.getMaintenanceEndTime());
		assertEquals(maintenances.get(0).getReservationExpiry(), maintenance.getReservationExpiry());
	}
	
	//Slucaj 2: maintenanceDate < ?2 AND maintenanceEndTime > ?3
	@Test
	public void test_getMaintenancesForDate_2(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = (Maintenance) iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceDate().plusMinutes(10),
				maintenance.getMaintenanceEndTime().minusMinutes(10));
		
		assertEquals(1, maintenances.size());
		assertEquals(maintenances.get(0).getMaintenanceDate(), maintenance.getMaintenanceDate());
		assertEquals(maintenances.get(0).getMaintenanceEndTime(), maintenance.getMaintenanceEndTime());
		assertEquals(maintenances.get(0).getReservationExpiry(), maintenance.getReservationExpiry());
	}
	
	//Slucaj 3: maintenanceDate > ?2 AND maintenanceEndTime < ?3
	@Test
	public void test_getMaintenancesForDate_3(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceDate().minusMinutes(10),
				maintenance.getMaintenanceEndTime().plusMinutes(10));
		
		assertEquals(1, maintenances.size());
		assertEquals(maintenances.get(0).getMaintenanceDate(), maintenance.getMaintenanceDate());
		assertEquals(maintenances.get(0).getMaintenanceEndTime(), maintenance.getMaintenanceEndTime());
		assertEquals(maintenances.get(0).getReservationExpiry(), maintenance.getReservationExpiry());
	}
		
	//Slucaj 4: m.maintenanceDate > ?2 AND m.maintenanceEndTime < ?3
	@Test
	public void test_getMaintenancesForDate_4(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = (Maintenance) iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceDate().minusMinutes(10),
				maintenance.getMaintenanceDate().plusMinutes(10));
		
		assertEquals(1, maintenances.size());
		assertEquals(maintenances.get(0).getMaintenanceDate(), maintenance.getMaintenanceDate());
		assertEquals(maintenances.get(0).getMaintenanceEndTime(), maintenance.getMaintenanceEndTime());
		assertEquals(maintenances.get(0).getReservationExpiry(), maintenance.getReservationExpiry());
	}
	
	//Slucaj 4: m.maintenanceDate > ?2 AND m.maintenanceEndTime < ?3
	@Test
	public void test_getMaintenancesForDate_5(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = (Maintenance) iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceEndTime().minusMinutes(10),
				maintenance.getMaintenanceEndTime().plusMinutes(10));
		
		assertEquals(1, maintenances.size());
		assertEquals(maintenances.get(0).getMaintenanceDate(), maintenance.getMaintenanceDate());
		assertEquals(maintenances.get(0).getMaintenanceEndTime(), maintenance.getMaintenanceEndTime());
		assertEquals(maintenances.get(0).getReservationExpiry(), maintenance.getReservationExpiry());
	}
	
	@Test
	public void test_getMaintenancesForDate_NoMaintenances(){
		Iterator<Maintenance> iterator = EVENT.getMaintenances().iterator();
		Maintenance maintenance = (Maintenance) iterator.next();
		
		ArrayList<Maintenance> maintenances = maintenanceRepository.getMaintenancesForDate(
				maintenance.getEvent().getLocationInfo().getId(),
				maintenance.getMaintenanceDate().minusHours(10),
				maintenance.getMaintenanceDate().minusHours(9));
		
		assertEquals(0, maintenances.size());
	}
	
	@Test
	@Transactional
	public void test_deleteByEventId(){
		maintenanceRepository.deleteByEventId(EVENT.getId());
		int deletedMaintenances = 0;
		for(Maintenance m: EVENT.getMaintenances()){
			try {
				maintenanceRepository.getOne(m.getId());
			} catch (Exception e){
				deletedMaintenances++;
		    }
			int deletedZones = 0;
			for(LeasedZone lz : m.getLeasedZones()){
				try {
					leasedZoneRepository.getOne(lz.getId());
				} catch (Exception e){
					deletedZones++;
			    }
				int deletedTickets = 0;
				for(Ticket t : lz.getTickets()){
					try {
						ticketRepository.getOne(t.getId());
					} catch (Exception e){
						deletedTickets++;
				    }
				}
				assertEquals(lz.getTickets().size(), deletedTickets);
			}
			assertEquals(m.getLeasedZones().size(), deletedZones);
		}
		assertEquals(EVENT.getMaintenances().size(), deletedMaintenances);
	}
	
	@After
	public void removeTestedData() {
		eventRepository.deleteById(EVENT.getId());
	}
}
