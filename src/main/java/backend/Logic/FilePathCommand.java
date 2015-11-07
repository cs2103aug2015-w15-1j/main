//@@author A0121284N
package main.java.backend.Logic;

import main.java.backend.Storage.Storage;

public class FilePathCommand extends Command {
	
	private static final String EXECUTION_FILEPATH_SUCCESSFUL = "File path is updated successfully.";
	private static final String EXECUTION_FILEPATH_UNSUCCESSFUL = "Invalid file path. Please try again";
	
	private String filepath;
	private Storage storageComponent;

	public FilePathCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
	}
	
	public void setFilePath(String path) {
		this.filepath = path;
	}
	
	public String getFilePath() {
		return this.filepath;
	}
	
	public String execute() {
		
		boolean isExecuted = storageComponent.updateFilePath(filepath);
		
		if (isExecuted) {
			return EXECUTION_FILEPATH_SUCCESSFUL;
		} else {
			return EXECUTION_FILEPATH_UNSUCCESSFUL;
		}
		
	}

}
