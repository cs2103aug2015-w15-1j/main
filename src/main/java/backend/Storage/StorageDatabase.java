package main.java.backend.Storage;

import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;

/**
 * This class loads from and saves data to text file.
 * 
 * @author A0126258A
 *
 */

public class StorageDatabase implements Storage {

	private StorageLoad load;
	private StorageSave save;
	
	public void init(String fileName) {
		load = new StorageLoad(fileName);
		save = new StorageSave(fileName);
	}
	
	public TreeMap<Integer, Task> load() {
		return load.execute();
	}
	
	public void save(TreeMap<Integer, Task> allData) {
		save.execute(allData);
	}
	
 }