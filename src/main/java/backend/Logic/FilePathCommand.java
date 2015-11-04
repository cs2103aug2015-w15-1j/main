//@@author A0121284N
package main.java.backend.Logic;

public class FilePathCommand extends Command {
	String filepath = "";

	public FilePathCommand(Type typeInput) {
		super(typeInput);
		// TODO Auto-generated constructor stub
	}

	public FilePathCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public void setFilePath(String path) {
		this.filepath = path;
	}
	
	public String getFilePath() {
		return this.filepath;
	}
	
	public String execute() {
		return "File path changed to "+filepath;
	}

}
