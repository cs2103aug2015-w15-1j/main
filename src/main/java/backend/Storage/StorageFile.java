package main.java.backend.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StorageFile {

	private static final String DEFAULT_FILE_NAME = "mytasklist.txt";
	private String INPUT_FILE_NAME;
	
	protected File textFile;
	protected BufferedReader bufferedReader;
	protected BufferedWriter bufferedWriter;
	
	private FileReader textFileReader;
	private FileWriter textFileWriter;
	
	public StorageFile() throws FileNotFoundException, IOException { 
		this(DEFAULT_FILE_NAME);
	}
	
	public StorageFile(String fileName) 
			throws FileNotFoundException, IOException {
		setFileName(fileName);
		initializeFile();
	}
	
	protected void initializeFile() 
			throws IOException, FileNotFoundException {
		
		textFile = new File(INPUT_FILE_NAME);
		createFile(textFile);
	}

	protected void initializeReader(File inputTextFile) 
			throws IOException, FileNotFoundException {
		
		textFileReader = new FileReader(inputTextFile);
		bufferedReader = new BufferedReader(textFileReader);
	}
	
	protected void createFile(File file) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
			initializeReader(textFile);
			initializeWriter(textFile);
		}
	}

	protected void initializeWriter(File inputTextFile) 
			throws IOException, FileNotFoundException {
		
		textFileWriter = new FileWriter(inputTextFile);
		bufferedWriter = new BufferedWriter(textFileWriter);
	}

	protected void closeReader() throws IOException {
		textFileReader.close();
		bufferedReader.close();
	}

	protected void closeWriter() throws IOException {
		bufferedWriter.flush();
		textFileWriter.close();
		bufferedWriter.close();
	}
	
	protected String getFileName() {
		return INPUT_FILE_NAME;
	}
	
	protected void setFileName(String fileName) {
		this.INPUT_FILE_NAME = fileName;
	}
	
	protected void clearTextFromFile() throws IOException {
		initializeWriter(textFile);
	}
	
	protected void exitProgram() throws IOException {
		closeReader();
		closeWriter();
	}
	
	protected boolean isFileEmpty() throws IOException {    
		initializeFile();
		initializeReader(textFile);
		
		if (bufferedReader.readLine() == null) {
		    return true;
		}
		
		return false;
	}
	
	protected String getAllTextsFromFile() throws IOException {
		return new String(Files.readAllBytes
				(Paths.get(getFileName())), StandardCharsets.UTF_8);
	}
}