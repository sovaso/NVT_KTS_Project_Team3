package com.nvt.kts.team3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "location_zone")
public class LocationZone {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "number_row")
	private int rowNumber;

	@Column(name = "number_col")
	private int colNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "zone_type")
	private ZoneType zoneType;

	@JsonIgnore
	@OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<LeasedZone> leasedZone = new HashSet<>();

	public Set<LeasedZone> getLeasedZone() {
		return leasedZone;
	}

	public void setLeasedZone(Set<LeasedZone> leasedZone) {
		this.leasedZone = leasedZone;
	}

	// many to one...
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;

	public LocationZone(long id, int rowNumber, int colNumber, ZoneType zoneType, Location location) {
		super();
		this.id = id;
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.zoneType = zoneType;
		this.location = location;
	}

	public LocationZone() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ZoneType getZoneType() {
		return zoneType;
	}

	public void setZoneType(ZoneType zoneType) {
		this.zoneType = zoneType;
	}

}
