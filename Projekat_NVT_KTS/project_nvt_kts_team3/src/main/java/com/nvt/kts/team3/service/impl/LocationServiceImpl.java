package com.nvt.kts.team3.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.LocationService;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotChangeable;
import exception.LocationNotFound;

@Service
public class LocationServiceImpl implements LocationService{

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Override
	public Location findById(Long id) {
		return locationRepository.getOne(id);
	}

	@Override
	public Location save(LocationDTO locationDTO) {
		if(locationService.findByNameAndAddress(locationDTO.getName(), locationDTO.getAddress()) != null){
			throw new LocationExists();
		}
		
		if(locationDTO.getLocationZone() == null || locationDTO.getLocationZone().isEmpty()){
			throw new InvalidLocationZone();
		}
		
		Location location = new Location(locationDTO.getName(),locationDTO.getAddress(),
				locationDTO.getDescription(),true,new HashSet<Event>(),new HashSet<LocationZone>());
		
		for(LocationZoneDTO lz : locationDTO.getLocationZone()){
			if(lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0){
				int capacity = lz.getCol() * lz.getRow();
				LocationZone newZone = new LocationZone(lz.getRow(),lz.getName(),
						capacity,true,lz.getCol(),new HashSet<LeasedZone>(),location);
				location.getLocationZones().add(newZone);
			}
			else if(lz.isMatrix() == false && lz.getCapacity() > 0){
				LocationZone newZone = new LocationZone(0,lz.getName(),
						lz.getCapacity(),false,0,new HashSet<LeasedZone>(),location);
				location.getLocationZones().add(newZone);
			}
			else{
				throw new InvalidLocationZone();
			}
		}
		return locationRepository.save(location);
	}
	
	@Override
	public Location update(LocationDTO locationDTO) {
		Location location = locationService.findById(locationDTO.getId());
		if(location == null || location.isStatus() == false){
			throw new LocationNotFound();
		}
		
		Location testName = locationService.findByNameAndAddress(locationDTO.getName(), locationDTO.getAddress());
		if(testName != null && testName.getId() != locationDTO.getId()){
			throw new LocationExists();
		}
		location.setDescription(locationDTO.getDescription());
		location.setName(locationDTO.getName());
		location.setAddress(locationDTO.getAddress());
		
		if(locationDTO.getLocationZone() == null || locationDTO.getLocationZone().isEmpty()){
			return locationRepository.save(location);
		}
		return locationRepository.save(location);
	}

	@Override
	public List<Location> findAll() {
		return locationRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		List<Event> activeEvents = getActiveEvents(id);
		Location location = findById(id);
		if(location == null || !(location.isStatus())){
			throw new LocationNotFound();
		}
		if(activeEvents != null && activeEvents.isEmpty() == false){
			throw new LocationNotChangeable();
		}
		
		location.setStatus(false);
		locationRepository.save(location);
	}

	@Override
	public Location findByNameAndAddress(String name, String address) {
		return locationRepository.findByNameAndAddress(name, address);
	}

	@Override
	public ArrayList<Event> getActiveEvents(long locationId) {
		return locationRepository.getActiveEvents(locationId);
	}

	@Override
	public ArrayList<Location> findAllActive() {
		return locationRepository.getActiveLocations();
	}

	@Override
	public ArrayList<Event> checkIfAvailable(Long locationId, Date startDate, Date endDate) {
		return locationRepository.checkIfAvailable(locationId, startDate, endDate);
	}
	
	@Override
	public LocationReportDTO getLocationReport(Long id) {
		LocationReportDTO retVal = new LocationReportDTO();
		Optional<Location> locationOpt = locationRepository.findById(id);
		if (locationOpt.isPresent()) {
			List<Reservation> res = this.reservationRepository.findAll();
			List<Reservation> ret = new ArrayList<Reservation>();
			for (Reservation r : res) {
				if (r.getEvent().getLocationInfo().getId() == id) {
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
			throw new LocationNotFound();
		}
	}

}
