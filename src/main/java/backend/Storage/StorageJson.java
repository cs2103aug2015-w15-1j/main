package main.java.backend.Storage;

import java.io.FileNotFoundException;
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

public class StorageJson extends StorageFile {
	
	public StorageJson() throws FileNotFoundException, IOException {
		super();
	}
	
	public StorageJson(String fileName) throws FileNotFoundException, IOException {
		super(fileName);
	}

	public HashMap<String, CategoryWrapper> getAllDataFromFile() 
			throws JsonParseException, JsonMappingException, IOException {
		
		if(isFileEmpty()) {
			return new HashMap<String, CategoryWrapper>();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			HashMap<String, CategoryWrapper> allTasks = 
					mapper.readValue(getAllTextsFromFile(), 
							new TypeReference<HashMap<String, CategoryWrapper>>() {});
			return allTasks;
		}
	}
	
	public HashMap<String, CategoryWrapper> setAllDataToFile
			(HashMap<String, CategoryWrapper> categoryWrapper) 
			throws JsonParseException, JsonMappingException, IOException {
		
		clearTextFromFile();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.writeValue(textFile, categoryWrapper);
		bufferedWriter.write(mapper.writeValueAsString(categoryWrapper));
		bufferedWriter.flush();
		
		return categoryWrapper;
	}

}
