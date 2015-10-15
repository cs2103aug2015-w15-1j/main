package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.backend.Storage.Task.Task;

/**
 * This class parse all texts in the file from JSON 
 * format to Java object to retrieve user's data
 * by reading data from the text file.
 * 
 * @author A0126258A
 * 
 */

public class StorageLoad extends StorageOperation {
	
	private BufferedReader bufferedReader;
	private FileReader textFileReader;
	
	public StorageLoad() {
		this(DEFAULT_FILE_LOCATION);
	}
	
	public StorageLoad(String fileName) {

		this.CURRENT_FILE_LOCATION = fileName;
		initFile();
	}
	
	private void initFile() {
		
		// TODO: Change default location to current location once bug resolved
		textFile = new File(DEFAULT_FILE_LOCATION);
		createFile();
		initReader();
	}
	
	private void createFile() {
		if(!textFile.exists()) {
			try {
				textFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initReader() {
		try {
			textFileReader = new FileReader(textFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bufferedReader = new BufferedReader(textFileReader);
	}

	private void closeReader() {
		try {
			textFileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isFileEmpty() {    
		initFile();
		initReader();

		try {
			if (bufferedReader.readLine() == null) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getAllTextsFromFile() throws IOException {
		
		//TODO: Change to CURRENT_FILE_LOCATION once bug resolved
		return new String(Files.readAllBytes
				(Paths.get(DEFAULT_FILE_LOCATION)), StandardCharsets.UTF_8);
	}
	
	public TreeMap<Integer, Task> execute(TreeMap<Integer, Task> nullData) {

		TreeMap<Integer, Task> allData = new TreeMap<Integer, Task>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		if(!isFileEmpty()) {
			try {
				allData = mapper.readValue(getAllTextsFromFile(), 
						new TypeReference<TreeMap<Integer, Task>>() {});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		closeReader();

		return allData;
	}
	
}