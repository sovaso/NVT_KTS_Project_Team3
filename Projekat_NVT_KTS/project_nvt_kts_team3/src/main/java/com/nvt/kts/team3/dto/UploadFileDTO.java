package com.nvt.kts.team3.dto;

import java.util.List;

public class UploadFileDTO {
	public List<String> pathToFile;

	public UploadFileDTO(List<String> pathToFile) {
		super();
		this.pathToFile = pathToFile;
	}

	public UploadFileDTO() {
		super();
	}

	public List<String> getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(List<String> pathToFile) {
		this.pathToFile = pathToFile;
	}

	
}
