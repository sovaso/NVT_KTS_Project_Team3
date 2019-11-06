package com.nvt.kts.team3.dto;

import java.util.ArrayList;

public class MaintenanceDTO {
	private String startDate;
	private String endDate;
	private long id;
	private long eventId;
	private ArrayList<LeasedZoneDTO> locationZones;

	public MaintenanceDTO(String startDate, String endDate, long id, long eventId,
			ArrayList<LeasedZoneDTO> locationZones) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		this.eventId = eventId;
		this.locationZones = locationZones;
	}

	public MaintenanceDTO() {
		super();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public ArrayList<LeasedZoneDTO> getLocationZones() {
		return locationZones;
	}

	public void setLocationZones(ArrayList<LeasedZoneDTO> locationZones) {
		this.locationZones = locationZones;
	}
}

