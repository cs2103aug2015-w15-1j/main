package main.java.backend.Storage;

import java.io.File;
import java.util.HashMap;

import main.java.backend.Storage.Task.Category;

public abstract class DataOperation {
	
	protected String INPUT_FILE_NAME;

	protected HashMap<String, Category> allCategories;
	protected File textFile;
	
	abstract HashMap<String, Category> execute(HashMap<String, Category> allData);
}
