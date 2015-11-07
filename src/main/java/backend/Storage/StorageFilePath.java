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
	
	private static final String FILE_CONFIGURATION = "config.properties";
	private static final String FILE_HEADING = "File path for data storage";
	private static final String FILE_KEY = "filepath";
	
	private static final String FRONTSLASH = "/";
	private static final String BACKSLASH_1 = "\\";
	private static final String BACKSLASH_2 = "\\\\";
	private static final String BACKSLASH_3 = "\\\\+";
	
	// TODO: Change default file path
	private static final String DEFAULT_PATH_LOCATION = System.getProperty("user.home") + "/Desktop" + "/filename.txt";
	//private static final String DEFAULT_PATH_LOCATION = new File(".").getAbsolutePath();
	private static final String DEFAULT_FILE_NAME = "/filename.txt";
	private static final String DEFAULT_FILE_EXTENTION = ".txt";
	
	private FileReader reader;
	private FileWriter writer;
	
	private Properties properties;

	//@@author A0126258A
	public StorageFilePath() {
		
		properties = new Properties();
	}
	
	//@@author A0121284N
	private void dataTransfer(String oldFilePath, String newFilePath) {
	
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
			bufferedInput.close();
			bufferedOutput.close();
			oldFile.delete();
		} catch (IOException e) {
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
	
	//@@author A0126258A
	private boolean isFilePathExist(String filePath) {
		
		filePath = filePath.replace(BACKSLASH_1, BACKSLASH_2);
		String tokenizeFrontSlash = removeFileName(filePath.split(FRONTSLASH), FRONTSLASH);
		String tokenizeBackSlash = removeFileName(filePath.split(BACKSLASH_2), 
				BACKSLASH_1).replaceAll(BACKSLASH_3, BACKSLASH_2);
		
		File filePathMac = new File(tokenizeFrontSlash);
		File filePathWindows = new File(tokenizeBackSlash);
		
		if (filePathMac.exists() || filePathWindows.exists()){
		    return true;
		} else {
			return false;
		}
	}
	
	//@@author A0126258A
	private String retrieveFilePath() {
		
		String filePath = properties.getProperty(FILE_KEY);
		
		if(filePath == null) {
			return DEFAULT_PATH_LOCATION;
		} else {
			return filePath;
		}
	}
	
	//@@author A0126258A
	private String appendTextFile(String newFilePath) {
		
		if(!newFilePath.contains(DEFAULT_FILE_EXTENTION)) {
			return newFilePath + addSlash(newFilePath);
		} else {
			return newFilePath;
		}
	}
	
	//@@author A0126258A
	private String getFileName() {
		
		String filePath = retrieve();
		String[] tokenizeFrontSlash = filePath.split(FRONTSLASH);
		String[] tokenizeBackSlash = filePath.split(BACKSLASH_2);
		
		System.out.println("FILEPATH: " + filePath);
		if(filePath.contains(FRONTSLASH)) {
			System.out.println("OHYEA: " + tokenizeFrontSlash[tokenizeFrontSlash.length - 1]);
			return tokenizeFrontSlash[tokenizeFrontSlash.length - 1];
		} else if(filePath.contains(BACKSLASH_1)) {
			return tokenizeBackSlash[tokenizeBackSlash.length - 1];
		}
		
		return DEFAULT_FILE_NAME;
	}
	
	//@@author A0126258A
	private String addSlash(String newFilePath) {
		
		if(newFilePath.contains(FRONTSLASH) 
				&& !newFilePath.endsWith(FRONTSLASH)) {
			return FRONTSLASH + getFileName();
		} else if(newFilePath.contains(FRONTSLASH) 
				&& newFilePath.endsWith(FRONTSLASH)) {
			return getFileName();
		} else if(newFilePath.contains(BACKSLASH_1) 
				&& !newFilePath.endsWith(BACKSLASH_1)) {
			return BACKSLASH_1 + getFileName();
		} else if(newFilePath.contains(BACKSLASH_1) 
				&& newFilePath.endsWith(BACKSLASH_1)) {
			return getFileName();
		} else {
			return newFilePath;
		}
	}
	
	//@@author A0126258A
	public String retrieve() {
		
		try {
			reader = new FileReader(FILE_CONFIGURATION);
			properties.load(reader);
		} catch (IOException e) {
			System.out.println(ERROR_READ_DATA);
		}
		
		return retrieveFilePath();
	}

	//@@author A0126258A
	public boolean execute(String newFilePath) {

		String oldFilePath = retrieve();
		newFilePath = appendTextFile(newFilePath);
		
		if(isFilePathExist(newFilePath) && !oldFilePath.equals(newFilePath)) {
			try {
				writer = new FileWriter(FILE_CONFIGURATION);
				dataTransfer(oldFilePath, newFilePath);
				properties.setProperty(FILE_KEY, newFilePath);
				properties.store(writer, FILE_HEADING);
				writer.close();
			} catch (IOException e) {
				return false;
			}	
		} else {
			return false;
		}
		
		return true;
	}

}