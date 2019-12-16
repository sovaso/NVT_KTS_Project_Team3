package com.nvt.kts.team3.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.UploadFileDTO;
import com.nvt.kts.team3.model.DriveQuickstart;
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
import exception.WrongPath;
import org.springframework.data.domain.Sort;

@Service
//@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

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

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Override
	public Optional<Event> findById(Long id) {
		return eventRepository.findById(id);
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Event save(EventDTO eventDTO) throws ParseException {
		// Ne sme da se ne unese tip dogadjaja
		if (EventType.valueOf(eventDTO.getEventType()) == null) {
			throw new InvalidEventType();
		}
		
		//Mora da se unese postojeca lokacija
		Location location = locationService.findById(eventDTO.getLocationId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}

		//Napravi event
		Event event = new Event(eventDTO.getName(), true, EventType.valueOf(eventDTO.getEventType()),
				new HashSet<Reservation>(), new HashSet<Maintenance>(), location, new ArrayList<String>(),
				new ArrayList<String>());

		//Posto svaki dogajdaj moze na primer da se odrzava dva vikenda, moras i ovim da se pozabavis.
		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		
		//Znaci prosledila sam i maintainace, ono ore sam samo napravila praznu lisu u konsturkotru za njegove potrebe
		for (MaintenanceDTO dates : eventDTO.getMaintenance()) {
			maintenanceStartDate = null;
			maintenanceStartDate = sdf.parse(dates.getStartDate());
			maintenanceEndDate = sdf.parse(dates.getEndDate());
			
			//Ako smo uneli da se odrzava pre danas
			if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)) {
			//	throw new InvalidDate();
			}
			
			//Postavljas da je expiry tri dana pre pocetka rezervacije
			expiry.setTime(maintenanceStartDate);
			expiry.add(Calendar.DATE, -3);
			long diff = maintenanceEndDate.getTime() - maintenanceStartDate.getTime();
			long hours = TimeUnit.MILLISECONDS.toHours(diff);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
			if (hours > 24 || minutes < 30) {
				throw new InvalidDate();
			}
			
			//Napravili smo maintainance
			Maintenance maintenance = new Maintenance(LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
					LocalDateTime.parse( sdf.format(maintenanceEndDate),df ),
					LocalDateTime.parse( sdf.format(expiry.getTime()),df ),
					new HashSet<LeasedZone>(), event);
			//U onu praznu listu maintainenance-a smo dodali maintaince
			event.getMaintenances().add(maintenance);

			//Proverila si da li lokacija postoji, sad proveravas da li je slobodna u tom periodu
			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), LocalDateTime.parse(sdf.format(maintenanceStartDate),df),
					LocalDateTime.parse(sdf.format(maintenanceEndDate),df));
			if (locationEvents.isEmpty() == false) {
				throw new LocationNotAvailable();
			}

			//U EventDTO klasi imas leased zone imas leasedZoneDTO set
			for (LeasedZoneDTO lz : eventDTO.getLocationZones()) {
				LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
				
				//Ako smo prosledili nevalidan id za zonu ili leasedZone lokacija nije isto sto i uneta lokacija
				if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
					throw new LocationZoneNotAvailable();
				}

				//Moras i validnu cenu uneti
				if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
					throw new InvalidPrice();
				}

				//Napravili smo leasedZone
				LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance, new HashSet<Ticket>());

				//Ako je matrica svaka karta ce imati tacno svoje mesto a ako nije onda su svi na primer u parteru.
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
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Event update(EventDTO eventDTO) throws ParseException {
		Event event = eventRepository.getOne(eventDTO.getId());
		if (event == null) {
			throw new EventNotFound();
		}
		
		//Dogadjaj nije aktivan i nema sta da se menja
		if (!(event.isStatus()) || !(eventIsActive(event.getId()))) {
			throw new EventNotChangeable();
		}
		
		//Ime dogadjaja mora da se unese
		if (eventDTO.getName() != null) {
			event.setName(eventDTO.getName());
		}

		//Tip dogadjaja mora da se unese
		event.setType(EventType.valueOf(eventDTO.getEventType()));
		if (event.getType() == null) {
			throw new InvalidEventType();
		}

		//Mora da se unese lokacija koja postoji i koja je aktivna
		Location location = locationService.findById(eventDTO.getLocationId());
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}
		
		//Ako je lokacija dogadjaja ista nema potrebe da menjas karte, jer su one pravljene po lokaciji
		if (location.getId() == event.getLocationInfo().getId()) {
			return eventRepository.save(event);
		}

		//Ako ima prodatih tiketa ne sme da se menja lokacija
		if (ticketService.getEventReservedTickets(event.getId()).isEmpty() == false) {
			if (eventDTO.getLocationId() != event.getLocationInfo().getId()) {
				throw new LocationNotChangeable();
			} else {
				return eventRepository.save(event);
			}
		}
		
		//Sacuvala si novu lokaciju sad ides odrzavanja
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
			long diff = maintenanceEndDate.getTime() - maintenanceStartDate.getTime();
			long hours = TimeUnit.MILLISECONDS.toHours(diff);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
			if (hours > 24 || minutes < 30) {
				throw new InvalidDate();
			}
			
			
			Maintenance maintenance = new Maintenance(LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
					LocalDateTime.parse( sdf.format(maintenanceEndDate),df ),
					LocalDateTime.parse( sdf.format(expiry.getTime()),df ),
					new HashSet<LeasedZone>(), event);
			newMaintenances.add(maintenance);

			
			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), LocalDateTime.parse( sdf.format(maintenanceStartDate),df),
					LocalDateTime.parse( sdf.format(maintenanceEndDate),df));
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

				//Kreiras LeasedZone
				LeasedZone newZone = new LeasedZone(lz.getPrice(), locationZone, maintenance, new HashSet<Ticket>());

				//Pravis karte u zavisnosti od toga jel matrica ili ne
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
		//Cuvas izmene
		maintenanceService.removeByEventId(event.getId());
		event.setMaintenances(newMaintenances);
		return eventRepository.save(event);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(Long id) {
		System.out.println(id);
		System.out.println("11111");
		Event event = eventRepository.getOne(id);
		System.out.println(event.getName());
		System.out.println(event.isStatus());
		System.out.println("2222");
		//Ne mozes da uklonis dogadjaj koji ne postoji ili koji nije aktivan
		if (event == null || event.isStatus() == false) {
			System.out.println("3333");
			throw new EventNotFound();
		}
		if (!(getSoldTickets(event.getId()).isEmpty()) && eventIsActive(event.getId())) {
			System.out.println("4444");
			throw new EventNotChangeable();
		}
		event.setStatus(false);
		System.out.println("55555");
		eventRepository.save(event);
		System.out.println("66666");
	}

	@Override
	public ArrayList<Event> getReservedTickets(Long eventId) {
		return eventRepository.getReservedTickets(eventId);
	}

	@Override
	public ArrayList<Ticket> getSoldTickets(Long eventId) {
		return eventRepository.getSoldTickets(eventId);
	}

	@Override
	public List<Event> getActiveEvents() {
		return eventRepository.getActiveEvents();
	}

	@Override
	public boolean eventIsActive(long eventId) {
		Maintenance maintenance = maintenanceService.getLastMaintenanceOfEvent(eventId);
		//Ako je odrzavanje u proslosti ili ako je maintainance otkazan onda je false
		if (maintenance.getMaintenanceDate().isBefore(LocalDateTime.now()) || maintenance.getEvent().isStatus() == false) {
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
		//Cena svih prodatih karata
		Optional<Event> eventOpt = eventRepository.findById(id);
		if (eventOpt.isPresent()) {
			Event e = eventOpt.get();
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

	@Override
	public String uploadFile(UploadFileDTO uploadFileDTO) throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		File item = new File();

		Permission permission = new Permission();
		permission.setRole("reader");
		permission.setType("anyone");
		List<Permission> permis = new ArrayList<Permission>();
		// permis.add(permission);
		File fileMetadata = new File();
		fileMetadata.setName(uploadFileDTO.getName());
		java.io.File filePath = new java.io.File(uploadFileDTO.getPathToFile());
		if (!filePath.exists()) {
			throw new WrongPath();
		} else {
			FileContent mediaContent = new FileContent("image/jpeg", filePath);
			Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					DriveQuickstart.getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();

			item = service.files().create(fileMetadata, mediaContent).setFields("id,webViewLink").execute();
			Permission perm = service.permissions().create(item.getId(), permission).execute();
			permis.add(perm);
			System.out.println("File ID: " + item.getWebViewLink());
			item.setPermissions(permis);
			return item.getWebViewLink();
		}
	}
	
	
	@Override
	public List<Event> searchEvent(String field, String start, String end){;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		if (start.equals("***") == false) {
			start = start.split("T")[0]+" "+start.split("T")[1];
		}
		if (end.equals("***")==false) {
			end = end.split("T")[0]+" "+end.split("T")[1];
		}
		List<Event> events = new ArrayList<Event>();
		if (start.equals("***")==true) {
			events = eventRepository.searchEventOnlyField(field);
		}else if (start.equals("***")==false && end.equals("***")==true && field.equals("***")==true){
			LocalDateTime startDate = LocalDateTime.parse(start, formatter);
			startDate = startDate.plusHours(1);
			events = eventRepository.searchEventSpecDate(startDate);
		}else if (start.equals("***")==false && end .equals("***")==false && field.equals("***")==true ){
			LocalDateTime startDate = LocalDateTime.parse(start, formatter);
			startDate = startDate.plusHours(1);
			LocalDateTime endDate = LocalDateTime.parse(end, formatter);
			endDate = endDate.plusHours(1);
			events = eventRepository.searchEventPeriod(startDate, endDate);
		}else if (field.equals("***")==false  && start.equals("***")==false && end.equals("***")==true) {
			LocalDateTime startDate = LocalDateTime.parse(start, formatter);
			startDate = startDate.plusHours(1);
			events = eventRepository.searchEventFieldSpecDate(field, startDate);
		}else  {
			LocalDateTime startDate = LocalDateTime.parse(start, formatter);
			startDate = startDate.plusHours(1);
			LocalDateTime endDate = LocalDateTime.parse(end, formatter);
			endDate = endDate.plusHours(1);
			events = eventRepository.searchEventFieldPeriod(field, startDate, endDate);
		}
		
		return events;
	}
	
	public List<Event> findAllSortedName(){
		return eventRepository.findAll(sortByIdAsc());
	}
	
	private Sort sortByIdAsc() {
		return new Sort(Sort.Direction.ASC, "name");
	}
	
	public List<Event> findAllSortedDateAcs(){
		return eventRepository.findAllSortedDateAcs();
	}
	
	public List<Event> findAllSortedDateDesc(){
		return eventRepository.findAllSortedDateDesc();
	}

	
	

	
}
