//@author A0126258A

package main.java.backend.Storage;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.Task;

public class StorageFormat {

	public StorageFormat() {

	}

	public String serialize(ArrayList<Task> allData) throws StorageException {
		
		String plaintext = new String();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			plaintext = mapper.writeValueAsString(allData);
		} catch (IOException e) {
			throw new StorageException();
		}
		
		return plaintext;
	}

	public ArrayList<Task> deserialize(String plaintext) throws StorageException {

		ArrayList<Task> allData = new ArrayList<Task>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			allData = mapper.readValue(plaintext, 
					new TypeReference<ArrayList<Task>>() {});
		} catch (IOException e) {
			throw new StorageException();
		}

		return allData;
	}
}
