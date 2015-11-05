//@@author A0126258A

package main.java.backend.Storage;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

/**
 * This class loads from and saves data to text file.
 * 
 */

public class StorageFacade implements Storage {

	private StorageLoad load;
	private StorageSave save;
	private StorageFilePath filePath;
	
	private void reinit() {
		load = new StorageLoad();
		save = new StorageSave();
	}
	
	@Override
	public void init() {
		load = new StorageLoad();
		save = new StorageSave();
		filePath = new StorageFilePath();
	}
	
	@Override
	public ArrayList<Task> load() {
		return load.execute(null);
	}
	
	@Override
	public void save(ArrayList<Task> allData) {
		save.execute(allData);
	}
	
	@Override
	public boolean updateFilePath(String newFilePath) {
		boolean isSuccess = filePath.execute(newFilePath);
		reinit();
		return isSuccess;
	}
 }