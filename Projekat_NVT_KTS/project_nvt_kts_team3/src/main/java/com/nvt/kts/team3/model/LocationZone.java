package com.nvt.kts.team3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "location_zone")
public class LocationZone {

	@Id
	@GenericGenerator(name="generator" , strategy="increment")
	@GeneratedValue(generator="generator")
	private long id;

	@Column(name = "number_row")
	private int rowNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "capacity")
	private int capacity;

	@Column(name = "matrix")
	private boolean matrix;

	@Column(name = "number_col")
	private int colNumber;

	@Version
	private Long version;

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
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "location_id")
	private Location location;

	public LocationZone() {
		super();
	}

	public LocationZone(long id, int rowNumber, String name, int capacity, boolean matrix, int colNumber,
			Set<LeasedZone> leasedZone, Location location) {
		super();
		this.id = id;
		this.rowNumber = rowNumber;
		this.name = name;
		this.capacity = capacity;
		this.matrix = matrix;
		this.colNumber = colNumber;
		this.leasedZone = leasedZone;
		this.location = location;
	}

	public LocationZone(int rowNumber, String name, int capacity, boolean matrix, int colNumber,
			Set<LeasedZone> leasedZone, Location location) {
		super();
		this.rowNumber = rowNumber;
		this.name = name;
		this.capacity = capacity;
		this.matrix = matrix;
		this.colNumber = colNumber;
		this.leasedZone = leasedZone;
		this.location = location;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isMatrix() {
		return matrix;
	}

	public void setMatrix(boolean matrix) {
		this.matrix = matrix;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
