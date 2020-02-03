package com.nvt.kts.team3.service.impl;

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
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.controller.EMailController;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.Mail;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;

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
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EMailController emailController;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df2 = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

	@Override
	public Maintenance findById(Long id) {
		Optional<Maintenance> maintenance = maintenanceRepository.findById(id);
		if(maintenance.isPresent()){
			return maintenance.get();
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Maintenance save(MaintenanceDTO maintenanceDTO) throws ParseException {
		Event event = eventService.findById(maintenanceDTO.getEventId());
		
		//Mora biti postojeci event i mora biti aktivan
		if(event == null){
			throw new EventNotFound();
		}
		
		if(!(eventService.eventIsActive(event.getId()))){
			throw new EventNotChangeable();
		}

		//Mora biti postojeca lokacija i mora biti aktivna
		Location location = event.getLocationInfo();
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
		
		//Proveravas validnost datuma
		if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)
				|| maintenanceEndDate.before(maintenanceStartDate)) {
			throw new InvalidDate();
		}
		//Postvljas expiry date
		expiry.setTime(maintenanceStartDate);
		expiry.add(Calendar.DATE, -3);
		
		//Neka provera pocetka i kraja odrzavanja
		long diff = maintenanceEndDate.getTime() - maintenanceStartDate.getTime();
		long hours = TimeUnit.MILLISECONDS.toHours(diff);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
		if (hours > 24 || minutes < 30) {
			throw new InvalidDate();
		}

		//Proveravas da li je lokacija na kojoj zelimo odrzavanje dostupna u zeljenom periodu.
		ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
				LocalDateTime.parse( sdf.format(maintenanceEndDate),df ));
		if (locationEvents.isEmpty() == false) {
			throw new LocationNotAvailable();
		}

		Maintenance maintenance = new Maintenance();
		
		maintenance.setMaintenanceDate(LocalDateTime.parse( sdf.format(maintenanceStartDate),df ));
		maintenance.setMaintenanceEndTime(LocalDateTime.parse( sdf.format(maintenanceEndDate),df ));
		maintenance.setReservationExpiry(LocalDateTime.parse( sdf.format(expiry.getTime()),df ));
		maintenance.setEvent(event);
		
		//Moras da navedes koje zone zelis da zakupis za dogadjaj
		if (maintenanceDTO.getLocationZones() == null || maintenanceDTO.getLocationZones().isEmpty()) {
			throw new InvalidLocationZone();
		}

		ArrayList<Long> choosenZones = new ArrayList<Long>();

		for (LeasedZoneDTO lz : maintenanceDTO.getLocationZones()) {
			LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
			//Proveravas da li postoji locationZone
			if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
				throw new LocationZoneNotAvailable();
			}

			if (choosenZones.contains(lz.getZoneId()) == false) {//Jednom mozes zakupiti jednu locationZone
				if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
					throw new InvalidPrice();
				}
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
				
				//Dodajes u maintenance u zakupljene zone tu zonu
				maintenance.getLeasedZones().add(newZone);
			}
		}
		return maintenanceRepository.save(maintenance);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Maintenance updateMaintenance(MaintenanceDTO maintenanceDTO) throws ParseException {
		Maintenance maintenance = findById(maintenanceDTO.getId());
		if(maintenance == null){
			throw new MaintenanceNotFound();
		}
		
		Event event = maintenance.getEvent();
		
		if(event == null){
			throw new EventNotFound();
		}
		
		if(!(eventService.eventIsActive(event.getId()))){
			throw new EventNotChangeable();
		}
		
		Location location = event.getLocationInfo();
		if(location == null || location.isStatus() == false){
			throw new LocationNotFound();
		}

		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();

		maintenanceStartDate = sdf.parse(maintenanceDTO.getStartDate());
		maintenanceEndDate = sdf.parse(maintenanceDTO.getEndDate());
		//Provervas validnost posledjenog vremena
		if (maintenanceStartDate.before(today) || maintenanceEndDate.before(maintenanceStartDate)) {
			throw new InvalidDate();
		}
		
		//Setujes expiry date
		expiry.setTime(maintenanceStartDate);
		expiry.add(Calendar.DATE, -3);
		
		//Neka provera izmedju pocetka i kraja
		long diff = maintenanceEndDate.getTime() - maintenanceStartDate.getTime();
		long hours = TimeUnit.MILLISECONDS.toHours(diff);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
		if (hours > 24 || minutes < 30) {
			throw new InvalidDate();
		}
		maintenance.setMaintenanceDate(LocalDateTime.parse(sdf.format(maintenanceStartDate), df));
		maintenance.setMaintenanceEndTime(LocalDateTime.parse(sdf.format(maintenanceEndDate), df));
		maintenance.setReservationExpiry(LocalDateTime.parse(sdf.format(expiry.getTime()), df));

		//Ne mozes da menjas lokaciju ako imas prodatih karata vec
		if (!(ticketService.getMaintenanceReservedTickets(maintenance.getId()).isEmpty())) {
			throw new MaintenanceNotChangeable();
		}
		
		//Cak i ako nema prodatih karata i mozes da promenis lokaciju, moras da proveris da li je ta lokacija slobodna
		ArrayList<Maintenance> maintenances = getMaintenancesForDate(location.getId(), LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
				LocalDateTime.parse( sdf.format(maintenanceEndDate),df));
		if (!(maintenances.isEmpty())) {
			if(!(maintenances.size() == 1 && maintenances.get(0).getId() == maintenance.getId())){
				throw new LocationNotAvailable();
			}
		}
		
		if (maintenanceDTO.getLocationZones() == null || maintenanceDTO.getLocationZones().isEmpty()) {
			throw new InvalidLocationZone();
		}

		leasedZoneService.deleteByMaintenanceId(maintenance.getId());//Oslobodile smo zone koje su bile zakupljena
		ArrayList<Long> choosenZones = new ArrayList<Long>();
		for (LeasedZoneDTO lz : maintenanceDTO.getLocationZones()) {
			LocationZone locationZone = locationZoneService.findById(lz.getZoneId());

			//Specificirala si zone koje hoces da zakupis i sad to sredjujes
			if (locationZone == null || locationZone.getLocation().getId() != location.getId()) {
				throw new LocationZoneNotAvailable();
			}

			if (choosenZones.contains(lz.getZoneId()) == false) {
				if (lz.getPrice() < 1 || lz.getPrice() > 10000) {
					throw new InvalidPrice();
				}
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
		if(maintenance == null){
			throw new MaintenanceNotFound();
		}
		Event event = maintenance.getEvent();
		if(!(eventService.eventIsActive(event.getId()))){
			throw new EventNotActive();
		}
		if (!(ticketService.getMaintenanceReservedTickets(id).isEmpty())) {
			throw new MaintenanceNotChangeable();
		}
		for(LeasedZone lz : maintenance.getLeasedZones()){
			ticketService.deleteByZoneId(lz.getId());
			leasedZoneService.deleteByMaintenanceId(id);
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

	@Override
	@Transactional
	@Modifying
	public void checkForExpieredTickets() throws AddressException, MessagingException {
		Calendar hourAgo = Calendar.getInstance();
		hourAgo.setTime(new Date());
		hourAgo.add(Calendar.HOUR, -1);
		List<Long> reservationsForDelete = new ArrayList<Long>();
		List<Mail> emails = new ArrayList<Mail>();
		List<Ticket> tickets = ticketService.getExpieredUnpaidTickets(LocalDateTime.parse(sdf.format(hourAgo.getTime()), df),LocalDateTime.parse(sdf.format(new Date()), df));
		List<String> checkMails = new ArrayList<String>();
		int expieredTickets = 0;
		if(tickets != null && !(tickets.isEmpty())){
			for(Ticket t : tickets){
				if(t.isReserved() && t.getReservation() != null && t.getReservation().isPaid() == false){
					expieredTickets++;
					String subject = "Ticket reservation has expiered";
					String body = "<html><head></head><body><p>Dear "+t.getReservation().getUser().getName()+",<br><br>You have reserved tickets for "+t.getZone().getMaintenance().getEvent().getName()+ " that holds at "+ t.getZone().getMaintenance().getMaintenanceDate().format(df2) +
							", but you didn't pay them before expiry date. Therefore, we inform you that you no longer own these tickets.</p>" +
							"<p>If you want to attend this event, you still have the opportunity to buy tickets on our site. </p><p>Best regards, <br>Your Events!</p></body></html>";
					Mail email = new Mail(t.getReservation().getUser().getEmail(), subject, body);
					Reservation r = t.getReservation();
					if(r.getReservedTickets().size() == 1){
						reservationsForDelete.add(r.getId());
					}
					Ticket updatableTicket = ticketService.findById(t.getId());
					updatableTicket.setReservation(null);
					updatableTicket.setReserved(false);
					ticketService.save(updatableTicket);
					if(!(checkMails.contains(email.getEmailAddress()))){
						emails.add(email);
						checkMails.add(email.getEmailAddress());
					}
				}
			}
			emailController.sendEmails(emails);
			for(Long id : reservationsForDelete){
				reservationService.delete(id);
			}
		}
		System.out.println("[INFO] Reservation expiry handled at: " + sdf2.format(new Date())+",  "+expieredTickets+" unpaid ticked proccessed.");
	}

	@Override
	public void warnUsersAboutExpiry() throws AddressException, MessagingException {
		Calendar next24hours = Calendar.getInstance();
		Calendar next25hours = Calendar.getInstance();
		next24hours.setTime(new Date());
		next24hours.add(Calendar.HOUR, 24);
		next25hours.setTime(new Date());
		next25hours.add(Calendar.HOUR, 25);
		List<Mail> emails = new ArrayList<Mail>();
		List<String> checkMails = new ArrayList<String>();
		List<Maintenance> maintenances = maintenanceRepository.getWarningMaintenances(LocalDateTime.parse(sdf.format(next24hours.getTime()), df), LocalDateTime.parse(sdf.format(next25hours.getTime()), df));
		int warnings = 0;
		if(maintenances != null && !(maintenances.isEmpty())){
			for(Maintenance m : maintenances){
				for(LeasedZone lz : m.getLeasedZones()){
					for(Ticket t : lz.getTickets()){
						if(t.isReserved() && t.getReservation() != null && t.getReservation().isPaid() == false){
							warnings++;
							String subject = "Ticket reservation expiereres in 24 hours";
							String body = "<html><head></head><body><p>Dear  "+t.getReservation().getUser().getName()+",<br><br> You have reserved tickets for "+m.getEvent().getName()+ " that holds at "+ m.getMaintenanceDate().format(df2) +
									".<br><br> We remind you to make payment in next 24 hours, before reservation expiry date." +
									"<br><br>Best regards,<br>Your Events!</p></body></html>";
							Mail email = new Mail(t.getReservation().getUser().getEmail(), subject, body);
							if(!(checkMails.contains(email.getEmailAddress()))){
								emails.add(email);
								checkMails.add(email.getEmailAddress());
							}
						}
					}
				}
			}
		}
		emailController.sendEmails(emails);
		System.out.println("[INFO] Users warned about reservation expiry at: " + sdf2.format(new Date())+",  "+warnings+" emails sent.");
	}

	@Override
	public ArrayList<Maintenance> getMaintenancesForDate(Long locationId, LocalDateTime startDate,
			LocalDateTime endDate) {
		return maintenanceRepository.getMaintenancesForDate(locationId, startDate, endDate);
	}

	@Override
	public void checkDates(MaintenanceDTO maintenanceDTO) throws ParseException {
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
		
		//Proveravas validnost datuma
		if (maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)
				|| maintenanceEndDate.before(maintenanceStartDate)) {
			throw new InvalidDate();
		}
		//Postvljas expiry date
		expiry.setTime(maintenanceStartDate);
		expiry.add(Calendar.DATE, -3);
		
		//Neka provera pocetka i kraja odrzavanja
		long diff = maintenanceEndDate.getTime() - maintenanceStartDate.getTime();
		long hours = TimeUnit.MILLISECONDS.toHours(diff);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
		if (hours > 24 || minutes < 30) {
			throw new InvalidDate();
		}

		//Proveravas da li je lokacija na kojoj zelimo odrzavanje dostupna u zeljenom periodu.
		ArrayList<Event> locationEvents = locationService.checkIfAvailable(maintenanceDTO.getId(), LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
				LocalDateTime.parse( sdf.format(maintenanceEndDate),df ));
		if (locationEvents.isEmpty() == false) {
			throw new LocationNotAvailable();
		}
		
	}
}
