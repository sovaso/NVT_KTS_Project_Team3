package com.nvt.kts.team3.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Event findById(Long id) {
		return eventRepository.getOne(id);
	}

	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public ArrayList<Event> getReservedTickets(Long eventId) {
		return eventRepository.getReservedTickets(eventId);
	}

	@Override
	public ArrayList<Event> getSoldTickets(Long eventId) {
		return eventRepository.getSoldTickets(eventId);
	}

	@Override
	public List<Event> getActiveEvents() {
		return eventRepository.getActiveEvents();
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
		}else {
			return null;
		}
	}

}
