//@@author A0121284N
package main.java.backend.Logic;

public class ErrorCommand extends Command {
	private String errorMessage = "";

	public ErrorCommand(Type typeInput) {
		super(typeInput);
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
