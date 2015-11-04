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

public class StorageSave extends StorageOperation {
	
	private BufferedWriter bufferedWriter;
	private FileWriter textFileWriter;
	
	public StorageSave() {
		
		storageFilePath = new StorageFilePath();
		storageFormat = new StorageFormat();
		filePath = storageFilePath.retrieve();
	}

	private void initializeWriter() throws StorageException {
		
		textFile = new File(filePath);
		try {
			textFileWriter = new FileWriter(textFile);
		} catch (IOException e) {
			throw new StorageException();
		}
		bufferedWriter = new BufferedWriter(textFileWriter);
	}

	private void closeWriter() throws StorageException {
		
		try { 
			bufferedWriter.flush();
			textFileWriter.close();
			bufferedWriter.close();
		} catch (IOException e) {
			throw new StorageException();
		}
	}
	
	public ArrayList<Task> execute(ArrayList<Task> allData) {
		
		try {
			initializeWriter();
			
			if(allData != null) {
				bufferedWriter.write(storageFormat.serialize(allData));
			}
			
			closeWriter();
		} catch (IOException | StorageException e) {
			e.getMessage();
		}
		
		return allData;
	}
}