package com.nvt.kts.team3.dto;

import java.util.ArrayList;

import com.nvt.kts.team3.model.Event;

public class ReservationDTO {
	private Event event;
	private ArrayList<TicketDTO> tickets;
	private String qrCode;
	public ReservationDTO(Event event, ArrayList<TicketDTO> tickets, String qrCode) {
		super();
		this.event = event;
		this.tickets = tickets;
		this.qrCode = qrCode;
	}
	public ReservationDTO() {
		super();
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public ArrayList<TicketDTO> getTickets() {
		return tickets;
	}
	public void setTickets(ArrayList<TicketDTO> tickets) {
		this.tickets = tickets;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	
	
	
	

}
