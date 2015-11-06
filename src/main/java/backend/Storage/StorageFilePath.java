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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class StorageFilePath {

	private static final String ERROR_READ_DATA = "An error occured when retrieving data from config file.";
	private static final String ERROR_TRANSFER_DATA = "An error occured when transfering data from config file.";
	private static final String ERROR_CLOSE_FILE = "An error occured when closing files.";
	
	private static final String FILE_CONFIGURATION = "config.properties";
	private static final String FILE_HEADING = "File path for data storage";
	private static final String FILE_KEY = "filepath";
	
	private static final String FRONTSLASH = "/";
	private static final String BACKSLASH_1 = "\\";
	private static final String BACKSLASH_2 = "\\\\";
	
	// TODO: Change default file path
	private static final String DEFAULT_PATH_LOCATION = System.getProperty("user.home") + "/Desktop" + "/filename.txt";
	private static final String DEFAULT_FILE_NAME = "/filename.txt";
	
	private FileReader reader;
	private FileWriter writer;
	private FileInputStream fileInput;
	private BufferedInputStream bufferedInput;
	private FileOutputStream fileOutput;
	private BufferedOutputStream bufferedOutput;
	
	private Properties properties;

	public StorageFilePath() {
		
		properties = new Properties();
	}
	
	//@@author A0121284N
	private void dataTransfer(String oldFilePath, String newFilePath) {
	
		byte[] buffer = new byte[10000];
		try {
			fileInput = new FileInputStream(oldFilePath);
			bufferedInput = new BufferedInputStream(fileInput);
			fileOutput = new FileOutputStream(newFilePath);
			bufferedOutput = new BufferedOutputStream(fileOutput);
			while(true) {
				int length = fileInput.read(buffer);
				if(length == -1) {
					break;
				} else {
					bufferedOutput.write(buffer);
					bufferedOutput.flush();
				}	
			}
			close();
			File oldFile = new File(oldFilePath);
			oldFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(ERROR_TRANSFER_DATA);
		}
		
	}
	
	//@@author A0126258A
	private void close() {
		try {
			fileInput.close();
			bufferedInput.close();
			fileOutput.close();
			bufferedOutput.close();
		} catch (IOException e) {
			System.out.println(ERROR_CLOSE_FILE);
		}
	}
	
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
			return DEFAULT_PATH_LOCATION;
		} else {
			return filePath;
		}
	}
	
	private String appendTextFile(String newFilePath) {
		
		if(!newFilePath.contains(".txt")) {
			return replaceSlash(newFilePath) + DEFAULT_FILE_NAME;
		} else {
			return newFilePath;
		}
	}
	
	private String replaceSlash(String newFilePath) {
		
		if(newFilePath.endsWith(FRONTSLASH) || newFilePath.endsWith(BACKSLASH_1)) {
			return newFilePath.substring(0, newFilePath.length() - 1);
		} else {
			return newFilePath;
		}
	}

	public boolean execute(String newFilePath) {

		String oldFilePath = retrieveFilePath();
		newFilePath = appendTextFile(newFilePath);
		
		if(isFilePathExist(newFilePath) && !oldFilePath.equals(newFilePath)) {
			try {
				writer = new FileWriter(FILE_CONFIGURATION);
				properties.setProperty(FILE_KEY, newFilePath);
				properties.store(writer, FILE_HEADING);
				writer.close();
				//Files.move(Paths.get(oldFilePath), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);
				dataTransfer(oldFilePath, newFilePath);
			} catch (IOException e) {
				return false;
			}	
		} else {
			return false;
		}
		
		return true;
	}

	public String retrieve() {
		
		try {
			reader = new FileReader(FILE_CONFIGURATION);
			properties.load(reader);
			reader.close();
		} catch (Exception e) {
			System.out.println(ERROR_READ_DATA);
		}
		
		return retrieveFilePath();
	}

}