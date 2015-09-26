package main.java.backend.Storage;

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

/**
 * This class parse all texts in the file from JSON format to Java object 
 * to retrieve user's data and parse Java object back to JSON format to 
 * be saved in the file as text when there is any update in the data.
 * 
 * @author A0126258A
 * 
 */

public class StorageJson {
	
	public StorageJson() {
		
	}

	public String getAllTextsFromFile() throws IOException {
		return new String(Files.readAllBytes
				(Paths.get(StorageFile.INPUT_FILE_NAME)), StandardCharsets.UTF_8);
	}

	public HashMap<String, CategoryWrapper> getAllDataFromFile() 
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HashMap<String, CategoryWrapper> allTasks = 
				mapper.readValue(getAllTextsFromFile(), 
						new TypeReference<HashMap<String, CategoryWrapper>>() {});
		return allTasks;
	}
	
	public String setAllDataToString (HashMap<String, CategoryWrapper> categoryWrapper) 
			throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper.writeValueAsString(categoryWrapper);
	}
}