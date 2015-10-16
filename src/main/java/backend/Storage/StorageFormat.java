package main.java.backend.Storage;

import java.io.IOException;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import main.java.backend.Storage.Task.Task;

public class StorageFormat {

	public StorageFormat() {

	}

	public String serialize(TreeMap<Integer, Task> allData) throws StorageException {
		
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

	public TreeMap<Integer, Task> deserialize(String plaintext) throws StorageException {

		TreeMap<Integer, Task> allData = new TreeMap<Integer, Task>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			allData = mapper.readValue(plaintext, 
					new TypeReference<TreeMap<Integer, Task>>() {});
		} catch (IOException e) {
			throw new StorageException();
		}

		return allData;
	}
}
