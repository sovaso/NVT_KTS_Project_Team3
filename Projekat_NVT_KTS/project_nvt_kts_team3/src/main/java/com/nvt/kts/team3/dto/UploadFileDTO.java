package com.nvt.kts.team3.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.model.File;

public class UploadFileDTO {
	public String pathToFile;

	public UploadFileDTO(String pathToFile) {
		super();
		this.pathToFile = pathToFile;
	}

	public UploadFileDTO() {
		super();
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	
}
