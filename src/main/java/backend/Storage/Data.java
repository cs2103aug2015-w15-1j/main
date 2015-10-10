package main.java.backend.Storage;

import java.util.HashMap;

import main.java.backend.Storage.Task.Category;

public class Data {

	private DataLoad load;
	private DataSave save;
	
	public Data(String fileName) {
		load = new DataLoad(fileName);
		save = new DataSave(fileName);
	}
	
	public HashMap<String, Category> load() {
		return load.execute(null);
	}
	
	public void save(HashMap<String, Category> allData) {
		save.execute(allData);
	}
 }
