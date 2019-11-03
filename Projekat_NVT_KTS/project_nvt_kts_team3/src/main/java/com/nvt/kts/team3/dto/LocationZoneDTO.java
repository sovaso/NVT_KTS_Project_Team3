package com.nvt.kts.team3.dto;

public class LocationZoneDTO {
	private long id;
	private boolean matrix;
	private String name;
	private int row;
	private int col;
	private int capacity;

	public LocationZoneDTO(long id, boolean matrix, String name, int row, int col, int capacity) {
		super();
		this.id = id;
		this.matrix = matrix;
		this.name = name;
		this.row = row;
		this.col = col;
		this.capacity = capacity;
	}

	public LocationZoneDTO() {
		super();
	}

	public boolean isMatrix() {
		return matrix;
	}

	public void setMatrix(boolean matrix) {
		this.matrix = matrix;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
