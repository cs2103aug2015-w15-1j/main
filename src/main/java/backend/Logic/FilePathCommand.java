//@@author A0121284N
package main.java.backend.Logic;

import main.java.backend.Storage.Storage;

public class FilePathCommand extends Command {
	String filepath = "";
	Storage storageComponent;

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
		storageComponent.updateFilePath(filepath);
		return "File path changed to "+filepath;
	}

}
