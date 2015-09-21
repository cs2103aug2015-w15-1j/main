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
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.CategoryWrapper;

public class StorageFile {

	private static final String DEFAULT_FILE_NAME = "mytasklist.txt";
	private static String INPUT_FILE_NAME;
	
	private File textFile;

	private FileReader textFileReader;
	private BufferedReader bufferedReader;

	private FileWriter textFileWriter;
	private BufferedWriter bufferedWriter;
	
	public StorageFile() throws FileNotFoundException, IOException { 
		this(DEFAULT_FILE_NAME);
	}
	
	public StorageFile(String fileName) 
			throws FileNotFoundException, IOException {
		setFileName(fileName);
		initializeFile();
	}
	
	public HashMap<String, CategoryWrapper> getAllCategoriesFromFile() 
			throws JsonParseException, JsonMappingException, IOException {
		
		if(isFileEmpty()) {
			return new HashMap<String, CategoryWrapper>();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			HashMap<String, CategoryWrapper> allTasks = 
					mapper.readValue(getAllTextsFromFile(), new TypeReference<HashMap<String, CategoryWrapper>>() {});
			return allTasks;
		} 
	}
	
	public void setAllCategoriesToFile(HashMap<String, CategoryWrapper> categoryWrapper) 
			throws JsonParseException, JsonMappingException, IOException {
		clearTextFromFile();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.writeValue(textFile, categoryWrapper);
		bufferedWriter.write(mapper.writeValueAsString(categoryWrapper));
		bufferedWriter.flush();
		System.out.println(mapper.writeValueAsString(categoryWrapper));
	}
	
	public void clearTextFromFile() throws IOException {
		initializeWriter(textFile);
	}
	
	public void exitProgram() throws IOException {
		closeReader();
		closeWriter();
	}
	
	private void setFileName(String fileName) {
		INPUT_FILE_NAME = fileName;
	}
	
	private void createFile(File file) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
			initializeReader(textFile);
			initializeWriter(textFile);
		}
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

	private boolean isFileEmpty() throws IOException {    
		initializeFile();
		initializeReader(textFile);
		
		if (bufferedReader.readLine() == null) {
		    return true;
		}
		
		return false;
	}
	
	private String getAllTextsFromFile() throws IOException {
		return new String(Files.readAllBytes
				(Paths.get(INPUT_FILE_NAME)), StandardCharsets.UTF_8);
	}
}
