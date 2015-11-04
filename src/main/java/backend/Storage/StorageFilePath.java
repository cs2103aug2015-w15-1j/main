//@@author A0126258A
package main.java.backend.Storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class StorageFilePath {

	private static final String ERROR_READ_DATA = "An error occured when retrieving data from config file.";
	private static final String ERROR_TRANSFER_DATA = "An error occured when transfering data from config file.";
	private static final String ERROR_WRITE_DATA = "An error occured when writing data from config file.";
	
	private static final String FILE_CONFIGURATION = "config.properties";
	private static final String FILE_HEADING = "File path for data storage";
	private static final String FILE_KEY = "filepath";
	
	private static final String FRONTSLASH = "/";
	private static final String BACKSLASH_1 = "\\";
	private static final String BACKSLASH_2 = "\\\\";
	
	// TODO: Change default file path
	private static final String PATH_LOCATION_DEFAULT = System.getProperty("user.home") + "/Desktop" + "/filename.txt";
	
	private FileReader reader;
	private FileWriter writer;
	
	private Properties properties;

	public StorageFilePath() {
		
		properties = new Properties();
	}
	
	
	//@@author A0121284N
	private void dataTransfer(String newFilePath) {
		String oldFilePath = retrieveFilePath();
		byte[] buffer = new byte[10000];
		try {
			FileInputStream fileInput = new FileInputStream(oldFilePath);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
			FileOutputStream fileOutput = new FileOutputStream(newFilePath);
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
			e.printStackTrace();
			System.out.println(ERROR_TRANSFER_DATA);
		}
		
	}
	
	//@@author A0126258A
	private String removeFileName(String[] tokenize, String slash) {
		
		String filePath = new String();
		int size = tokenize.length - 1;
		
		for(int i = 0; i < size; i++) {
			filePath += slash + tokenize[i];
		}
		
		return filePath;
	}
	
	private boolean isFilePathExist(String filePath) {
		
		filePath = filePath.replace("\\", "\\\\");
		String tokenizeFrontSlash = removeFileName(filePath.split(FRONTSLASH), FRONTSLASH);
		String tokenizeBackSlash = removeFileName(filePath.split(BACKSLASH_2), BACKSLASH_1).replaceAll("\\\\+","\\\\");
		
		File filePathMac = new File(tokenizeFrontSlash);
		File filePathWindows = new File(tokenizeBackSlash);
		
		if (filePathMac.exists() || filePathWindows.exists()){
		    return true;
		} else {
			return false;
		}
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

		if(newFilePath.contains(".txt") && isFilePathExist(newFilePath)) {
			try {
				writer = new FileWriter(FILE_CONFIGURATION);
				dataTransfer(newFilePath);
				properties.setProperty(FILE_KEY, newFilePath);
				properties.store(writer, FILE_HEADING);
				writer.close();
			} catch (IOException e) {
				System.out.println(ERROR_WRITE_DATA);
			}	
		}
	}

	public String retrieve() {
		
		try {
			reader = new FileReader(FILE_CONFIGURATION);
			properties.load(reader);
		} catch (Exception e) {
			System.out.println(ERROR_READ_DATA);
		}
		
		return retrieveFilePath();
	}

}