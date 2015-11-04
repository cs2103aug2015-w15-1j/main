//@@author A0126258A
package main.java.backend.Storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	
	private String filePathOld, filePathNew = "";
	
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
	
	
	//@@author A0121284N
	private void dataTransfer() {
		String oldFilePath = retrieveFilePath();
		byte[] buffer = new byte[10000];
		try {
			FileInputStream fileInput = new FileInputStream(oldFilePath);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
			FileOutputStream fileOutput = new FileOutputStream(this.filePathNew);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
			while(true) {
				int length = fileInput.read(buffer);
				if(length == -1) {
					break;
				} else {
					bufferedOutput.write(buffer);
					bufferedOutput.flush();
				}	
			}
			File oldFile = new File(oldFilePath);
			oldFile.delete();
			bufferedInput.close();
			bufferedOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//@@author A0126258A
	private String retrieveFilePath() {
		
		String filePath = properties.getProperty(FILE_KEY);
		
		if(filePath == null) {
			return PATH_LOCATION_DEFAULT;
		} else {
			return filePath;
		}
	}

	public void execute(String newFilePath) {
		
		filePathOld = retrieveFilePath();
		filePathNew = newFilePath;
		
		try {
			writer = new FileWriter(FILE_CONFIGURATION);
			textFile.renameTo(new File(newFilePath));
			dataTransfer();
			properties.setProperty(FILE_KEY, newFilePath);
			properties.store(writer, FILE_HEADING);
			writer.close();
		} catch (IOException e) {
			properties.setProperty(FILE_KEY, filePathOld);
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
