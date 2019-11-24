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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.controller.EMailController;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
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
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EMailController emailController;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


	@Override
	public Maintenance findById(Long id) {
		return maintenanceRepository.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Maintenance save(MaintenanceDTO maintenanceDTO) throws ParseException {
		Event event = eventService.findById(maintenanceDTO.getEventId());
		
		//Mora biti postojeci event i mora biti aktivan
		if (event == null || !(eventService.eventIsActive(event.getId()))) {
			throw new EventNotFound();
		}

		//Mora biti postojeca lokacija i mora biti aktivna
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
				LocalDateTime.parse( sdf.format(maintenanceEndDate) ));
		if (locationEvents.isEmpty() == false) {
			throw new LocationNotAvailable();
		}

		Maintenance maintenance = new Maintenance();
		
		maintenance.setMaintenanceDate(LocalDateTime.parse( sdf.format(maintenanceStartDate),df ));
		maintenance.setMaintenanceEndTime(LocalDateTime.parse( sdf.format(maintenanceEndDate),df ));
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

		//Ne mozes updateovat nepostojeci maintenance
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

		Location location = locationService.findById(event.getLocationInfo().getId());
		//Ne mozes da postvis nepostojecu ili neaktivnu lokaciju
		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}

		if (location.getId() != maintenance.getEvent().getId()) {
			//Ne mozes da menjas lokaciju ako imas prodatih karata vec
			if (!(ticketService.getMaintenanceReservedTickets(maintenance.getId()).isEmpty())) {
				throw new MaintenanceNotChangeable();
			}
			
			//Cak i ako nema prodatih karata i mozes da promenis lokaciju, moras da proveris da li je ta lokacija slobodna
			ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), LocalDateTime.parse( sdf.format(maintenanceStartDate),df ),
					LocalDateTime.parse( sdf.format(maintenanceEndDate),df ));
			if (locationEvents.isEmpty() == false) {
				throw new LocationNotAvailable();
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
		
		//Ne mozes da obrises ako ima rezervisanih karata
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

	@Override
	public List<Maintenance> save(List<Maintenance> maintenances) {
		return maintenanceRepository.save(maintenances);
	}

	@Override
	public void checkForExpieredTickets() throws AddressException, MessagingException {
		Calendar hourAgo = Calendar.getInstance();
		hourAgo.setTime(new Date());
		hourAgo.add(Calendar.HOUR, -1);
		List<Maintenance> maintenances = maintenanceRepository.getExpieredMaintenances(LocalDateTime.parse(sdf.format(hourAgo.getTime()), df));
		int expieredTickets = 0;
		if(maintenances != null && !(maintenances.isEmpty())){
			List<Long> reservationsForDelete = new ArrayList<Long>();
			List<Long> ticketsForUpdate = new ArrayList<Long>();
			for(Maintenance m : maintenances){
				Set<String> emails = new HashSet<String>();
				for(LeasedZone lz : m.getLeasedZones()){
					for(Ticket t : lz.getTickets()){
						if(t.isReserved() && t.getReservation() != null && t.getReservation().isPaid() == false){
							expieredTickets++;
							Reservation r = t.getReservation();
							if(r.getReservedTickets().size() == 1){
								reservationsForDelete.add(r.getId());
							}
							emails.add(t.getReservation().getUser().getEmail());
							ticketsForUpdate.add(t.getId());
							t.setReservation(null);
							t.setReserved(false);
							ticketService.save(t);
						}
					}
				}
				String subject = "Ticket reservation has expiered";
				String body = "<html><head></head><body><p>Dear User,<br><br>You have reserved tickets for "+m.getEvent().getName()+ " that holds at "+ m.getMaintenanceDate().format(df) +
						", but you didn't pay them before reservation expiry date. Therefore, we inform you that you no longer own these tickets.</p>" +
						"<p>If you want to attend this event, you still have the opportunity to buy tickets. </p><p>Best regards, <br>Your Events!</p></body></html>";
				emailController.sendEmails(emails, subject, body);
				for(Long id : reservationsForDelete){
					reservationService.remove(id);
				}
			}
		}
		System.out.println("[INFO] Reservation expiry handled at: " + sdf2.format(new Date())+",  "+expieredTickets+" unpaid ticked proccessed.");
	}

	@Override
	public void warnUsersAboutExpiry() throws AddressException, MessagingException {
		List<Maintenance> maintenances2 = maintenanceRepository.findAll();
		for(Maintenance m : maintenances2){
			System.out.println(sdf2.format(m.getReservationExpiry()));
		}
		
		Calendar next24hours = Calendar.getInstance();
		Calendar next25hours = Calendar.getInstance();
		next24hours.setTime(new Date());
		next24hours.add(Calendar.HOUR, -24);
		next25hours.setTime(new Date());
		next25hours.add(Calendar.HOUR, -25);
		List<Maintenance> maintenances = maintenanceRepository.getWarningMaintenances(LocalDateTime.parse(sdf.format(next24hours.getTime()), df), LocalDateTime.parse(sdf.format(next25hours.getTime()), df));
		int warnings = 0;
		if(maintenances != null && !(maintenances.isEmpty())){
			for(Maintenance m : maintenances){
				Set<String> emails = new HashSet<String>();
				for(LeasedZone lz : m.getLeasedZones()){
					for(Ticket t : lz.getTickets()){
						if(t.isReserved() && t.getReservation() != null && t.getReservation().isPaid() == false){
							warnings++;
							emails.add(t.getReservation().getUser().getEmail());
						}
					}
				}
				String subject = "Ticket reservation expiereres in 24 hours";
				String body = "Dear User,\n\nYou have reserved tickets for "+m.getEvent().getName()+ " that holds at "+ m.getMaintenanceDate().format(df) +
						". If you want to hold your tickets, please make a payment within the next 24 hours." +
						"\n\nBest regards,\nYour Events!";
				emailController.sendEmails(emails, subject, body);
			}
		}
		System.out.println("[INFO] Users warned about reservation expiry at: " + sdf2.format(new Date())+",  "+warnings+" emails sent.");
	}
}
