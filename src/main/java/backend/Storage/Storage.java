package main.java.backend.Storage;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;;

public interface Storage {

	public void init();
	
	public ArrayList<Task> load();
	
	public void save(ArrayList<Task> allData);
	
	public void updateFilePath(String filePath);
	
}