package main.java.backend.Storage;

import java.util.TreeMap;

import main.java.backend.Storage.Task.Category;

public interface Storage {

	public void init(String fileName);
	
	public TreeMap<String, Category> load();
	
	public void save(TreeMap<String, Category> allData);
	
}