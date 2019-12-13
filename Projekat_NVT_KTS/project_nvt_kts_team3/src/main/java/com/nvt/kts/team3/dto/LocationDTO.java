package com.nvt.kts.team3.dto;

import java.util.ArrayList;
import java.util.List;

public class LocationDTO {
	private long id;
	private String name;
	private String address;
	private String description;
	private List<LocationZoneDTO> locationZone = new ArrayList<LocationZoneDTO>();
	
	public LocationDTO(long id, String name, String address, String description) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
	}

	public LocationDTO(long id, String name, String address, String description, List<LocationZoneDTO> locationZone) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.locationZone = locationZone;
	}

	public LocationDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<LocationZoneDTO> getLocationZone() {
		return locationZone;
	}

	public void setLocationZone(List<LocationZoneDTO> locationZone) {
		this.locationZone = locationZone;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
