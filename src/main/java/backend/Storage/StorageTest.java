package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.Storage.Task.Category;

public class StorageTest {
	
	/* ======================== Categories =========================== */
	private static final String CATEGORY1 = "CS2102";
	private static final String CATEGORY2 = "CS2103";
	private static final String CATEGORY3 = "Personal";
	
	/* ======================== CS2102 tasks ========================= */
	private static final String CATEGORY1_TASK1 = "Read up on PHP";
	private static final String CATEGORY1_TASK2 = "Revise nested queries";
	
	/* ======================== CS2103 tasks ========================= */
	private static final String CATEGORY2_TASK1 = "TextBuddy CE2";
	private static final String CATEGORY2_TASK2 = "Post lecture quiz 5";
	private static final String CATEGORY2_TASK3 = "Tutorial 5";
	private static final String CATEGORY2_FLOAT1 = "Watch webcast";
	
	/* ======================== Personal tasks ========================= */
	private static final String CATEGORY3_TASK1 = "Help mum buy groceries";
	private static final String CATEGORY3_FLOAT1 = "Get haircut someday";
	private static final String CATEGORY3_EVENT1 = "Cycling @ East Coast";
	private static final String CATEGORY3_EVENT2 = "3D'09 gathering @ Mr Teo's house";
	
	private static final String TEST_FILE_NAME = "test.txt";
	
	Storage storage;
	
	@Before
	public void initialize() throws IOException {
		storage = new Storage(TEST_FILE_NAME);
	}
	
	@Test
	public void testAddCategories() {
		
		ArrayList<String> categoryNames = new ArrayList<String> ();
		categoryNames.addAll(Arrays.asList(CATEGORY1, CATEGORY2, CATEGORY3));

		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		assertEquals(categoryNames, storage.getCategories());
	}

}
