package com.nvt.kts.team3.dto;

public class LeasedZoneDTO {
	private long zoneId;
	private double price;
	
	public LeasedZoneDTO(long zoneId, double price) {
		super();
		this.zoneId = zoneId;
		this.price = price;
	}
	public LeasedZoneDTO() {
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
}
