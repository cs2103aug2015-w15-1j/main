package main.java.backend.Storage;

import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;

/**
 * This class loads from and saves data to text file.
 * 
 * @author A0126258A
 *
 */

public class StorageFacade implements Storage {

	private StorageLoad load;
	private StorageSave save;
	
	public void init(String filePath) {
		load = new StorageLoad(filePath);
		save = new StorageSave(filePath);
	}
	
	public TreeMap<Integer, Task> load() {
		return load.execute(null);
	}
	
	public void save(TreeMap<Integer, Task> allData) {
		save.execute(allData);
	}
	
 }