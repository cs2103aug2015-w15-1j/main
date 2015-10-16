package main.java.backend.Storage;

import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;;

public interface Storage {

	public void init(String fileName);
	
	public TreeMap<Integer, Task> load();
	
	public void save(TreeMap<Integer, Task> allData);
	
}