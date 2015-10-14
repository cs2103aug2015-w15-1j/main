package storage;

import org.junit.Test;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageDatabase;

public class StorageTest {

	private static final String TEST_FILE_NAME = "test.txt";
	
	Storage storage;
	
	@Test
	public void init() {
		storage = new StorageDatabase();
		storage.init(TEST_FILE_NAME);
	}

}
