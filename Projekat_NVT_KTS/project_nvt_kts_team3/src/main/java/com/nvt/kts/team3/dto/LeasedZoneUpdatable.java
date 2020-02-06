package com.nvt.kts.team3.dto;

public class LeasedZoneUpdatable {

	private long id;
	private long zoneId;
	private long maintenanceId;
	private double price;
	private boolean updatable;
	
	public LeasedZoneUpdatable(long id, long zoneId, long maintenanceId, double price) {
		super();
		this.id = id;
		this.zoneId = zoneId;
		this.maintenanceId = maintenanceId;
		this.price = price;
	}
	public LeasedZoneUpdatable(long id, long zoneId, long maintenanceId, double price, boolean updatable) {
		super();
		this.id = id;
		this.zoneId = zoneId;
		this.maintenanceId = maintenanceId;
		this.price = price;
		this.updatable = updatable;
	}

	public LeasedZoneUpdatable() {
		super();
	}
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMaintenanceId() {
		return maintenanceId;
	}
	public void setMaintenanceId(long maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	public boolean isUpdatable() {
		return updatable;
	}
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
}
