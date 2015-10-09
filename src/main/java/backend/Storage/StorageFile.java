package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import main.java.backend.Storage.Task.Category;


/**
 * This class creates new text file to read user's data from the 
 * file and write the user's data in JSON text format to the file. 
 * 
 * @author A0126258A
 *
 */

public class StorageFile {

	public static String INPUT_FILE_NAME;
	private static final String DEFAULT_FILE_NAME = "mytasklist.txt";
	
	private File textFile;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	private FileReader textFileReader;
	private FileWriter textFileWriter;
	
	private StorageJson storageJson;
	
	private boolean isInit = false;
	
	public StorageFile() throws FileNotFoundException, IOException { 
		this(DEFAULT_FILE_NAME);
	}
	
	public StorageFile(String fileName) 
			throws FileNotFoundException, IOException {
		storageJson = new StorageJson();
		setFileName(fileName);
		initializeFile();
	}
	
	private void initializeFile() 
			throws IOException, FileNotFoundException {
		
		textFile = new File(INPUT_FILE_NAME);
		createFile(textFile);
	}

	private void initializeReader(File inputTextFile) 
			throws IOException, FileNotFoundException {
		
		textFileReader = new FileReader(inputTextFile);
		bufferedReader = new BufferedReader(textFileReader);
	}
	
	private void createFile(File file) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
			initializeReader(textFile);
			initializeWriter(textFile);
		}
	}

	private void initializeWriter(File inputTextFile) 
			throws IOException, FileNotFoundException {
		
		textFileWriter = new FileWriter(inputTextFile);
		bufferedWriter = new BufferedWriter(textFileWriter);
		isInit = true;
	}

	private void closeReader() throws IOException {
		textFileReader.close();
		bufferedReader.close();
	}

	private void closeWriter() throws IOException {
		bufferedWriter.flush();
		textFileWriter.close();
		bufferedWriter.close();
	}
	
	public String getFileName() {
		return INPUT_FILE_NAME;
	}
	
	public void setFileName(String fileName) {
		this.INPUT_FILE_NAME = fileName;
	}
	
	public void clearTextFromFile() throws IOException {
		initializeWriter(textFile);
	}
	
	public void exitProgram() throws IOException {
		if(isInit) {
			closeReader();
			closeWriter();
		}
		System.exit(0);
	}
	
	private boolean isFileEmpty() throws IOException {    
		initializeFile();
		initializeReader(textFile);
		
		if (bufferedReader.readLine() == null) {
		    return true;
		}
		
		return false;
	}
	
	public HashMap<String, Category> getAllDataFromFile() 
			throws IOException {
		
		if(isFileEmpty()) {
			return new HashMap<String, Category>();
		} else {
			return storageJson.getAllDataFromFile();
		}
	}
	
	public HashMap<String, Category> setAllDataToFile
			(HashMap<String, Category> category) 
			throws IOException {
		
		clearTextFromFile();
		bufferedWriter.write(storageJson.setAllDataToString(category));
		bufferedWriter.flush();
		
		return category;
	}
}