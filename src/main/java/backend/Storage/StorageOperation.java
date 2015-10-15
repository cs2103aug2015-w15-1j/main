package main.java.backend.Storage;

import java.io.File;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;

public abstract class StorageOperation {

	public static final String DEFAULT_DESKTOP_LOCATION = 
			System.getProperty("user.home") + "/Desktop";
	public static final String DEFAULT_FILE_LOCATION = 
			DEFAULT_DESKTOP_LOCATION + "/filename.txt";
	public String CURRENT_FILE_LOCATION = new String();
	public File textFile;

	public abstract TreeMap<Integer, Task> execute(TreeMap<Integer, Task> taskList);
}
