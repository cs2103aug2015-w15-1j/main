package main.java.backend.Storage;

public class StorageException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_ERROR = "There is an error in storage.";
	
	public StorageException() {
		super(MESSAGE_ERROR);
	}
	
	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(Throwable cause) {
		super(cause);
	}
	
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}