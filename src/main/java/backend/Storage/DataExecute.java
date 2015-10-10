package main.java.backend.Storage;

import java.io.File;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Category;

public abstract class DataExecute {
	
	protected String INPUT_FILE_NAME;
	protected File textFile;
	
	abstract TreeMap<String, Category> execute(TreeMap<String, Category> allData);
}