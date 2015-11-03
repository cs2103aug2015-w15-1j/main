package main.java.backend.Storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class StorageFilePath {

	private static final String ERROR_READ_DATA = "An error occured when retrieving data from config file.";
	private static final String ERROR_WRITE_DATA = "An error occured when writing data from config file.";
	
	private static final String FILE_CONFIGURATION = "config.properties";
	private static final String FILE_HEADING = "File path for data storage";
	private static final String FILE_KEY = "filepath";
	
	// TODO: Change default file path
	private static final String PATH_LOCATION_DEFAULT = System.getProperty("user.home") + "/Desktop" + "/filename.txt";
	
	private File textFile;
	private FileReader reader;
	private FileWriter writer;
	
	private Properties properties;

	public StorageFilePath() {
		
		properties = new Properties();
		textFile = new File(retrieveFilePath());
	}
	
	private String retrieveFilePath() {
		
		String filePath = properties.getProperty(FILE_KEY);
		
		if(filePath == null) {
			return PATH_LOCATION_DEFAULT;
		} else {
			return filePath;
		}
	}

	public void execute(String newFilePath) {
		
		try {
			writer = new FileWriter(FILE_CONFIGURATION);
			textFile.renameTo(new File(newFilePath));
			properties.setProperty(FILE_KEY, newFilePath);
			properties.store(writer, FILE_HEADING);
			writer.close();
		} catch (IOException e) {
			System.out.println(ERROR_WRITE_DATA);
		}	
	}
	
	public String retrieve() {
		
		try {
			reader = new FileReader(FILE_CONFIGURATION);
			properties.load(reader);
		} catch (Exception e) {
			System.out.println(ERROR_READ_DATA);
		}
		
		System.out.println("RETRIEVE: " + retrieveFilePath());
		return retrieveFilePath();
	}

}
