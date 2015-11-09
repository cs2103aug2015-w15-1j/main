package main.java.backend.Storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class manipulates the file path of the text file where all 
 * data are saved. It allows users to change the file path of the text 
 * file by simply providing the file path with or without file text name.
 * 
 * @author A0126258A
 *
 */

public class StorageFilePath {

	private static final String EXECUTION_FILEPATH_SUCCESSFUL = "File path is updated successfully to %1$s";
	private static final String EXECUTION_FILEPATH_UNSUCCESSFUL = "Invalid file path. Please try again";
	private static final String EXECUTION_FILEPATH_DUPLICATE = "File is already in %1$s. Please try again with a new file path.";
	
	private static final String ERROR_READ_DATA = "An error occured when retrieving data from config file.";
	private static final String ERROR_TRANSFER_DATA = "An error occured when transfering data from config file.";
	
	private static final String FILE_CONFIGURATION = "config.properties";
	private static final String FILE_HEADING = "File path for data storage";
	private static final String FILE_KEY = "filepath";
	
	private static final String FRONTSLASH = "/";
	private static final String BACKSLASH_1 = "\\";
	private static final String BACKSLASH_2 = "\\\\";
	private static final String BACKSLASH_3 = "\\\\+";
	
	private static final String DEFAULT_FILE_NAME = "/filename.txt";
	private static final String DEFAULT_FILE_EXTENTION = ".txt";
	private static final String DEFAULT_PATH_LOCATION = new File(".").getAbsolutePath().replace(".", "") 
			+ DEFAULT_FILE_NAME.replace(FRONTSLASH, "");
	
	private FileReader reader;
	private FileWriter writer;
	
	private Properties properties;

	//@@author A0126258A
	public StorageFilePath() {
		
		properties = new Properties();
	}
	
	//@@author A0121284N
	/**
	 * Transfers all data from current file to new file.
	 * 
	 * @param oldFilePath		File path of current file
	 * @param newFilePath		File path of new file
	 */
	private boolean dataTransfer(String oldFilePath, String newFilePath) {
	
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
			return true;
		} catch (IOException e) {
			System.out.println(ERROR_TRANSFER_DATA);
			return false;
		}
	}
	
	//@@author A0126258A
	/**
	 * Removes file name to check if new file path exist
	 * before transferring data over to the new file path.
	 * 
	 * @param tokenize		Tokenize the file path to remove last token (file name)
	 * @param slash			Token to represent front or back slash to distinguish 
	 * 						mac or windows file path
	 * @return				Returns filepath with file name removed	
	 */
	private String removeFileName(String[] tokenize, String slash) {
		
		String filePath = new String();
		int size = tokenize.length - 1;
		
		for(int i = 0; i < size; i++) {
			filePath += slash + tokenize[i];
		}
		
		return filePath;
	}
	
	//@@author A0126258A
	/**
	 * Check if file path exist before transferring data 
	 * over from current file to new file
	 * 
	 * @param filePath		New file path provided by user.
	 * @return				Returns true if exist, else false.
	 */
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
	/**
	 * Retrieves current file path of the text file
	 * where all data is saved.
	 * 
	 * @return		Current file path
	 */
	private String retrieveFilePath() {
		
		String filePath = properties.getProperty(FILE_KEY);
		
		if(filePath == null) {
			return DEFAULT_PATH_LOCATION;
		} else {
			return filePath;
		}
	}
	
	//@@author A0126258A
	/**
	 * Append the previous set text file name to the 
	 * new file path provided by the user if the user
	 * did not specify the name of the text file for
	 * the new file path.
	 * 
	 * @param newFilePath		New file path to transfer data over.
	 * @return					New file path with name of text file attached.
	 */
	private String appendTextFile(String newFilePath) {
		
		if(!newFilePath.contains(DEFAULT_FILE_EXTENTION)) {
			return newFilePath + addSlash(newFilePath);
		} else {
			return newFilePath;
		}
	}
	
	//@@author A0126258A
	/**
	 * Retrieves the file name of current file path 
	 * if the user did not specify file name in the
	 * new file path.
	 * 
	 * @return			Current name of text file
	 */
	private String getFileName() {
		
		String filePath = retrieve();
		String[] tokenizeFrontSlash = filePath.split(FRONTSLASH);
		String[] tokenizeBackSlash = filePath.split(BACKSLASH_2);
		
		if(filePath.contains(FRONTSLASH)) {
			return tokenizeFrontSlash[tokenizeFrontSlash.length - 1];
		} else if(filePath.contains(BACKSLASH_1)) {
			return tokenizeBackSlash[tokenizeBackSlash.length - 1];
		}
		
		return DEFAULT_FILE_NAME;
	}
	
	//@@author A0126258A
	/**
	 * Append frontslash or backslash if the
	 * new file path provided by the user does not
	 * have file name specified, and no slashes typed 
	 * at the last character of the file path.
	 * 
	 * @param newFilePath		New file path provided by user.
	 * @return					Proper file path with all required slashes.
	 */
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
	/**
	 * Retrieves the current file path of data storage.
	 * 
	 * @return		Current file path
	 */
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
	public HashMap<Boolean, String> execute(String newFilePath) {

		HashMap<Boolean, String> result = new HashMap<Boolean, String>();
		String oldFilePath = retrieve();
		newFilePath = appendTextFile(newFilePath);
		boolean isTransferred = false;

		if(oldFilePath.equals(newFilePath)) {
			result.put(false, String.format(EXECUTION_FILEPATH_DUPLICATE, newFilePath));
			return result;
		} else if(isFilePathExist(newFilePath)) {
			try {
				writer = new FileWriter(FILE_CONFIGURATION);
				isTransferred = dataTransfer(oldFilePath, newFilePath);
				properties.setProperty(FILE_KEY, newFilePath);
				properties.store(writer, FILE_HEADING);
				writer.close();
			} catch (IOException e) {
				result.put(false, EXECUTION_FILEPATH_UNSUCCESSFUL);
				return result;
			}	
		} else if(!isTransferred) {
			result.put(false, EXECUTION_FILEPATH_UNSUCCESSFUL);
			return result;
		} 
		result.put(true, String.format(EXECUTION_FILEPATH_SUCCESSFUL, newFilePath));
		return result;
	}

}