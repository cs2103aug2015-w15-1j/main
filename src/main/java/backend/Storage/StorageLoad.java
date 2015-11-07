//@@author A0126258A

package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.File;
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

	private static final String ERROR_FILE_READ = "An error occured when reading data from file.";
	private static final String ERROR_FILE_CLOSE = "An error occured when closing file reader.";
	private static final String ERROR_FILE_LOAD = "An error occured when loading data from file.";
	
	private BufferedReader bufferedReader;
	private FileReader textFileReader;

	public StorageLoad() {

		storageFormat = new StorageFormat();
		storageFilePath = new StorageFilePath();
		filePath = storageFilePath.retrieve();
	}

	private void initReader() {

		try {
			textFile = new File(filePath);
			
			if(!textFile.exists()) {
				textFile.createNewFile();
			}
			
			textFileReader = new FileReader(textFile);
			bufferedReader = new BufferedReader(textFileReader);
		} catch (IOException e) {
			System.out.println(ERROR_FILE_READ);
		}
	}

	private void closeReader()  {
		
		try {
			textFileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println(ERROR_FILE_CLOSE);
		}
	}

	public ArrayList<Task> execute(ArrayList<Task> nullData) {

		ArrayList<Task> allData = new ArrayList<Task> ();
		initReader();

		try {
			if(!(bufferedReader.readLine() == null)) {
				allData = storageFormat.deserialize(new String(Files.readAllBytes
						(Paths.get(filePath)), StandardCharsets.UTF_8));
			} 
		} catch (IOException e) {
			System.out.println(ERROR_FILE_LOAD);
		}

		closeReader();
		return allData;
	}

}