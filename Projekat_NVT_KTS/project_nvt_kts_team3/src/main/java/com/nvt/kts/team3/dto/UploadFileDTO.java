package com.nvt.kts.team3.dto;

public class UploadFileDTO {
	public String name;
	public String pathToFile;

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public UploadFileDTO(String name, String pathToFile) {
		super();
		this.name = name;
		this.pathToFile = pathToFile;
	}

	public UploadFileDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
