//@@author A0126258A

package main.java.backend.Storage;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.Task;

/**
 * This class serialize Task objects to JSON format
 * or deserialize JSON format to Task objects.
 * 
 * @author A0126258A
 *
 */

public class StorageFormat {
	
	private static final String ERROR_SERIALIZE = "An error occured when serializing Task objects to text.";
	private static final String ERROR_DESERIALIZE = "An error occured when deserializing text to Task objects.";

	public StorageFormat() {

	}

	/**
	 * Serialize all Task objects to JSON text format.
	 * 
	 * @param allData			All data in Task objects form.
	 * @return					Task objects in JSON format.
	 */
	public String serialize(ArrayList<Task> allData) {
		
		String plaintext = new String();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			plaintext = mapper.writeValueAsString(allData);
		} catch (IOException e) {
			System.out.println(ERROR_SERIALIZE);
		}
		
		return plaintext;
	}

	/**
	 * Deserialize plaintext format (JSON) back to Task objects.
	 * 
	 * @param plaintext			Task objects in JSON format.
	 * @return					All data in Task object form.
	 */
	public ArrayList<Task> deserialize(String plaintext) {

		ArrayList<Task> allData = new ArrayList<Task>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			allData = mapper.readValue(plaintext, 
					new TypeReference<ArrayList<Task>>() {});
		} catch (IOException e) {
			System.out.println(ERROR_DESERIALIZE);
		}

		return allData;
	}
}
