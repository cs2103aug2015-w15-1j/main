package main.java.backend.Storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.Task;


/**
 * This class parse Java object back to JSON format 
 * to be saved in the file as text when there is any 
 * update in the data by writing data to the text file.
 * 
 * @author A0126258A
 * 
 */

public class StorageSave extends StorageOperation {
	
	private BufferedWriter bufferedWriter;
	private FileWriter textFileWriter;
	
	public StorageSave() {
		this(DEFAULT_FILE_LOCATION);
	}
	
	public StorageSave(String fileName) {
		this.CURRENT_FILE_LOCATION = fileName;
	}

	private void initializeWriter() {
		
		textFile = new File(CURRENT_FILE_LOCATION);
		try {
			textFileWriter = new FileWriter(textFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bufferedWriter = new BufferedWriter(textFileWriter);
	}

	private void closeWriter() {
		
		try { 
			bufferedWriter.flush();
			textFileWriter.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void changeFileDirectory() {
		
		// TODO: Change to newFile(CURRENT_FILE_LOCATION) once bug resolved
		if(!CURRENT_FILE_LOCATION.equals(DEFAULT_FILE_LOCATION)) {
			File newFile = new File(DEFAULT_FILE_LOCATION);
			System.out.println(newFile.getAbsolutePath());
			textFile.renameTo(newFile);
		}
	}
	
	private void saveData(TreeMap<Integer, Task> allData) {
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			bufferedWriter.write(mapper.writeValueAsString(allData));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TreeMap<Integer, Task> execute(TreeMap<Integer, Task> allData) {
		
		initializeWriter();
		changeFileDirectory();
		
		if(allData != null) {
			saveData(allData);
		}
		
		closeWriter();
		
		return allData;
	}
}