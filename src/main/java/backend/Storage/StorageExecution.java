//@@author A0126258A

package main.java.backend.Storage;

import java.io.File;
import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

public abstract class StorageExecution {

	public String filePath;
	public File textFile;
	
	public StorageFormat storageFormat;
	public StorageFilePath storageFilePath;

	public abstract ArrayList<Task> execute(ArrayList<Task> taskList);
}