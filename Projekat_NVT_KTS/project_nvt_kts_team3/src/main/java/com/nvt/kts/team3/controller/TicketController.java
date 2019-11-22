package com.nvt.kts.team3.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.TicketService;

import exception.TicketNotFound;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {
	
	@Autowired
	private TicketService ticketService;

	@GetMapping(value = "/getQRCodeImage/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getQRCodeImage(@PathVariable(value = "id") Long ticketId) throws WriterException, IOException {
		// TODO Proveriti da li karta ima vlasnika
		// TODO Provera da li je vlasnik karte poslao zahtev
		Ticket ticket = ticketService.findById(ticketId);
		if(ticket == null){
			throw new TicketNotFound();
		}

	    String code = String.valueOf(ticket.getId());
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 350, 350);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bao);
		return bao.toByteArray();
    }
	
	@GetMapping(value = "/getEventTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventTickets(@PathVariable(value = "id") Long eventId) {
		return new ResponseEntity<>(ticketService.getEventTickets(eventId), HttpStatus.OK);
	}

	@GetMapping(value = "/getEventReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventReservedTickets(@PathVariable(value = "id") Long eventId) {
		return new ResponseEntity<>(ticketService.getEventReservedTickets(eventId), HttpStatus.OK);
	}

	@GetMapping(value = "/getEventSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventSoldTickets(@PathVariable(value = "id") Long eventId) {
		return new ResponseEntity<>(ticketService.getEventSoldTickets(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceReservedTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceReservedTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceSoldTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceSoldTickets(maintenanceId), HttpStatus.OK);
	}
}
