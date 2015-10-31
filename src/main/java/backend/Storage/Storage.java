package main.java.backend.Storage;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;;

public interface Storage {

	public void init(String fileName);
	
	public ArrayList<Task> load();
	
	public void save(ArrayList<Task> allData);
	
}