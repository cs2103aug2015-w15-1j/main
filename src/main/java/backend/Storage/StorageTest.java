package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class StorageTest {

	/* ======================== Categories =========================== */
	private static final String CATEGORY1 = "CS2102";
	private static final String CATEGORY2 = "CS2103";
	private static final String CATEGORY3 = "Personal";

	/* ======================== CS2102 Tasks ========================= */
	private Task CATEGORY1_Task1 = new Task("CS2102", "Read up on PHP", "Find out how to code query in PHP", 
					"Sat, 3 Oct 8:00am", Storage.stringToMillisecond("Sat, 3 Oct 8:00am"), 5, 
					"Fri, 2 Oct 6:00pm", Storage.stringToMillisecond("Fri, 2 Oct 6:00pm"),  true);
	private Task CATEGORY1_Task2 = new Task("CS2102", "Revise SQL queries", "Read up on ALL SQL queries including nested", 
					"Sat, 10 Oct 9:00am", Storage.stringToMillisecond("Sat, 10 Oct 9:00am"), 5, 
					"Fri, 9 Oct 6:00pm", Storage.stringToMillisecond("Fri, 9 Oct 6:00pm"), false);

	/* ======================== CS2103 Tasks ========================= */
	private Task CATEGORY2_Task1 = new Task("CS2103", "Text Buddy CE2", "Change code to OOP style", 
					"Sat, 19 Oct 8:00am", Storage.stringToMillisecond("Sat, 19 Oct 8:00am"), 5, 
					"Mon, 21 Oct 10:00am", Storage.stringToMillisecond("Mon, 21 Oct 10:00am"), false);
	private Task CATEGORY2_FLOAT1 = new Task("CS2103", "Watch webcast", "Watch before November", -1, 
			 "Tue, 20 Oct 8:00am", Storage.stringToMillisecond("Tue, 20 Oct 8:00am"), false);
	
	/* ======================== Personal Tasks ========================= */
	private Task CATEGORY3_Task1 = new Task("Personal", "Help mum buy groceries", "Every Saturday!", "", -1, 3, "", -1, false);
	private Task CATEGORY3_FLOAT1 = new Task("Personal", "Cut hair someday", "Cut before December", -1, 
			 "Wed, 10 Oct 10:00am", Storage.stringToMillisecond("Wed, 10 Oct 10:00am"), false);
	private Task CATEGORY3_EVENT1 = new Task("Personal", "Cycling @ East Coast", "Meet at bike rental at 3pm", 
					"Sat, 17 Oct 3:00pm", "Sat, 17 Oct 10:00pm", Storage.stringToMillisecond("Sat, 17 Oct 3:00pm"),
					Storage.stringToMillisecond("Sat, 17 Oct 10:00pm"), -1, "Fri, 16 Oct 10:00am",
					Storage.stringToMillisecond("Fri, 16 Oct 10:00am"));
	private Task CATEGORY3_EVENT2 = new Task("Personal", "3D'09 gathering @ Mr Teo's house", "Meet at bike rental at 3pm", 
					"Sun, 27 Dec 1:00pm", "Sun, 27 Dec 9:00pm", Storage.stringToMillisecond("Sun, 27 Oct 1:00pm"),
					Storage.stringToMillisecond("Sun, 27 Dec 10:00pm"), -1, "Sat, 27 Oct 10:00am",
					Storage.stringToMillisecond("Sat, 27 Oct 10:00am"));

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
	
	@Test
	public void testAddFloatingTask() {

		ArrayList<Category> data = storage.getCategoryList();
		TreeMap<Integer, Task> floatCS2103 = data.get(1).getFloatTasks();
		TreeMap<Integer, Task> floatPersonal = data.get(2).getFloatTasks();

		floatCS2103.put(0, storage.addFloatingTask(CATEGORY2_FLOAT1));
		floatPersonal.put(1, storage.addFloatingTask(CATEGORY3_FLOAT1));

		data.get(1).setFloatTasks(floatCS2103);
		data.get(2).setFloatTasks(floatPersonal);

		assertEquals(data.get(1).getFloatTasks().get(0),
				storage.getCategoryList().get(1).getFloatTasks().get(0));
		assertEquals(data.get(2).getFloatTasks().get(0), 
				storage.getCategoryList().get(2).getFloatTasks().get(0));
	}

}
