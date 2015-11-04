//@author A0126258A

package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

		storageFormat = new StorageFormat();
		storageFilePath = new StorageFilePath();
		filePath = storageFilePath.retrieve();
		initFile();
	}

	private void initFile() {

		textFile = new File(filePath);
		try {
			createFile();
			initReader();
		} catch (StorageException e) {
			e.getMessage();
		}
	}

	private void createFile() throws StorageException {
		if(!textFile.exists()) {
			try {
				textFile.createNewFile();
			} catch (IOException e) {
				throw new StorageException();
			}
		}
	}

	private void initReader() throws StorageException {
		try {
			textFileReader = new FileReader(textFile);
		} catch (FileNotFoundException e) {
			throw new StorageException();
		}
		bufferedReader = new BufferedReader(textFileReader);
	}

	private void closeReader() throws StorageException {
		try {
			textFileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			throw new StorageException();
		}
	}

	private boolean isFileEmpty() throws StorageException {    
		initFile();
		initReader();

		try {
			if (bufferedReader.readLine() == null) {
				return true;
			}
		} catch (IOException e) {
			throw new StorageException();
		}

		return false;
	}

	public String getAllTextsFromFile() throws StorageException {

		String plainText = new String();
		try {
			plainText = new String(Files.readAllBytes
					(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new StorageException();
		}
		
		return plainText;
	}

	public ArrayList<Task> execute(ArrayList<Task> nullData) {
		
		ArrayList<Task> allData = new ArrayList<Task> ();
		
		try {
			if(!isFileEmpty()) {
				allData = storageFormat.deserialize(getAllTextsFromFile());
			}
			
			closeReader();
		} catch (StorageException e) {
			e.getMessage();
		}
		
		return allData;

	}

}