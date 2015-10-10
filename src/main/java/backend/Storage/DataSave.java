package main.java.backend.Storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.Category;

/**
 * This class parse Java object back to JSON format 
 * to be saved in the file as text when there is any 
 * update in the data.
 * 
 * @author A0126258A
 * 
 */

public class DataSave extends DataExecute {
	
	private BufferedWriter bufferedWriter;
	private FileWriter textFileWriter;
	
	public DataSave(String fileName) {
		this.INPUT_FILE_NAME = fileName;
	}

	private void initializeWriter() 
			throws IOException, FileNotFoundException {
		
		textFile = new File(INPUT_FILE_NAME);
		textFileWriter = new FileWriter(textFile);
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
	
	@Override
	public HashMap<String, Category> execute(HashMap<String, Category> category) {
		
		try {
			initializeWriter();
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			bufferedWriter.write(mapper.writeValueAsString(category));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		closeWriter();
		
		return category;
	}
}
