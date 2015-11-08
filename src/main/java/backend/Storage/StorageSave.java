//@@author A0126258A

package main.java.backend.Storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

/**
 * This class parse Java object back to JSON format 
 * to be saved in the file as text when there is any 
 * update in the data by writing data to the text file.
 * 
 * @author A0126258A
 * 
 */

public class StorageSave extends StorageExecution {
	
	private static final String ERROR_FILE_WRITE = "An error occured when writing data from file.";
	private static final String ERROR_FILE_CLOSE = "An error occured when closing file writer.";
	private static final String ERROR_FILE_SAVE = "An error occured when saving data to file.";
	
	private BufferedWriter bufferedWriter;
	private FileWriter textFileWriter;
	
	public StorageSave() {
		
		storageFilePath = new StorageFilePath();
		storageFormat = new StorageFormat();
		filePath = storageFilePath.retrieve();
	}

	private void initWriter() {
		
		try {
			textFile = new File(filePath);
			textFileWriter = new FileWriter(textFile);
			bufferedWriter = new BufferedWriter(textFileWriter);
		} catch (IOException e) {
			System.out.println(ERROR_FILE_WRITE);
		}
	}

	private void closeWriter() {
		
		try { 
			bufferedWriter.flush();
			textFileWriter.close();
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println(ERROR_FILE_CLOSE);
		}
	}
	
	public ArrayList<Task> execute(ArrayList<Task> allData) {
		
		initWriter();
		
		try {
			if(allData != null) {
				bufferedWriter.write(storageFormat.serialize(allData));
			}
		} catch (IOException e) {
			System.out.println(ERROR_FILE_SAVE);
		}
		
		closeWriter();
		return allData;
	}
}