package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import main.java.backend.Storage.Task.CategoryWrapper;

public class StorageFile {

	public static String INPUT_FILE_NAME;
	private static final String DEFAULT_FILE_NAME = "mytasklist.txt";
	
	private File textFile;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	private FileReader textFileReader;
	private FileWriter textFileWriter;
	
	private StorageJson storageJson;
	
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
		closeReader();
		closeWriter();
	}
	
	private boolean isFileEmpty() throws IOException {    
		initializeFile();
		initializeReader(textFile);
		
		if (bufferedReader.readLine() == null) {
		    return true;
		}
		
		return false;
	}
	
	public HashMap<String, CategoryWrapper> getAllDataFromFile() 
			throws IOException {
		
		if(isFileEmpty()) {
			return new HashMap<String, CategoryWrapper>();
		} else {
			return storageJson.getAllDataFromFile();
		}
	}
	
	public HashMap<String, CategoryWrapper> setAllDataToFile
			(HashMap<String, CategoryWrapper> categoryWrapper) 
			throws IOException {
		
		clearTextFromFile();
		bufferedWriter.write(storageJson.setAllDataToString(categoryWrapper));
		bufferedWriter.flush();
		
		return categoryWrapper;
	}
}