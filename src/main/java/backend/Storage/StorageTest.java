package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;

public class StorageTest {
	
	private static final String TEST_FILE_NAME = "test.txt";
	
	Storage storage;
	StorageFile storageFile;
	
	@Before
	private void initialize() throws IOException {
		storage = new StorageData();
		storageFile = new StorageFile(TEST_FILE_NAME);
	}

}
