package com.nvt.kts.team3.dto;

import java.util.ArrayList;

public class EventDTO {
	private String name;
	private String description;
	private String eventType;
	private ArrayList<String> maintenance;
	private ArrayList<LeasedZoneDTO> locationZones;
	private long locationId;

	public EventDTO(String name, String description, String eventType, ArrayList<String> maintenance,
			ArrayList<LeasedZoneDTO> locationZones, long locationId) {
		super();
		this.name = name;
		this.description = description;
		this.eventType = eventType;
		this.maintenance = maintenance;
		this.locationZones = locationZones;
		this.locationId = locationId;
	}

	public EventDTO() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public ArrayList<String> getMaintenance() {
		return maintenance;
	}
	public void setMaintenance(ArrayList<String> maintenance) {
		this.maintenance = maintenance;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public ArrayList<LeasedZoneDTO> getLocationZones() {
		return locationZones;
	}

	public void setLocationZones(ArrayList<LeasedZoneDTO> locationZones) {
		this.locationZones = locationZones;
	}
}
