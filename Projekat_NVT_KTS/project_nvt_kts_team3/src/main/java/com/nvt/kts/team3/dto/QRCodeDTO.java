package com.nvt.kts.team3.dto;

public class QRCodeDTO {
	private byte[] qrBytes;

	public byte[] getQrBytes() {
		return qrBytes;
	}

	public void setQrBytes(byte[] qrBytes) {
		this.qrBytes = qrBytes;
	}

	public QRCodeDTO(byte[] qrBytes) {
		super();
		this.qrBytes = qrBytes;
	}

	public QRCodeDTO() {
		super();
	}
	
	

}
