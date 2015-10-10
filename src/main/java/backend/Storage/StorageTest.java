package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class StorageTest {

	private static final String TEST_FILE_NAME = "test.txt";

	/* ======================== Category colors ========================= */
	private static final String COLOUR_BLUE = "#24c6d5";
	private static final String COLOUR_GREEN = "#57dd86";

	/* ======================== Categories =========================== */
	private static final String CATEGORY1 = "CS2102";
	private static final String CATEGORY2 = "CS2103";
	private static final String CATEGORY3 = "Personal";
	private static final String CATEGORY4 = "Outings";

	/* ======================== CS2102 Tasks ========================= */
	private Task CATEGORY1_TODO1 = new Task(CATEGORY1, "Read up on PHP", "Find out how to code query in PHP", 
			"Sat, 3 Oct 8:00am", Storage.stringToMillisecond("Sat, 3 Oct 8:00am"), 5, 
			"Fri, 2 Oct 6:00pm", Storage.stringToMillisecond("Fri, 2 Oct 6:00pm"),  true);
	private Task CATEGORY1_TODO2 = new Task(CATEGORY1, "Revise SQL queries", 
			"Read up on ALL SQL queries including nested", 
			"Sat, 10 Oct 9:00am", Storage.stringToMillisecond("Sat, 10 Oct 9:00am"), 5, 
			"Fri, 9 Oct 6:00pm", Storage.stringToMillisecond("Fri, 9 Oct 6:00pm"), false);

	/* ======================== CS2103 Tasks ========================= */
	private Task CATEGORY2_TODO1 = new Task(CATEGORY2, "Text Buddy CE2", "Change code to OOP style", 
			"Sat, 19 Oct 8:00am", Storage.stringToMillisecond("Sat, 19 Oct 8:00am"), 5, 
			"Mon, 21 Oct 10:00am", Storage.stringToMillisecond("Mon, 21 Oct 10:00am"), false);
	private Task CATEGORY2_FLOAT1 = new Task(CATEGORY2, "Watch webcast", "Watch before November", -1, 
			"Tue, 20 Oct 8:00am", Storage.stringToMillisecond("Tue, 20 Oct 8:00am"), false);

	/* ======================== Personal Tasks ========================= */
	private Task CATEGORY3_TODO1 = new Task(CATEGORY3, "Help mum buy groceries", "Every Saturday!", 
			"", -1, 3, "", -1, false);
	private Task CATEGORY3_FLOAT1 = new Task(CATEGORY3, "Cut hair someday", "Cut before December", -1, 
			"Wed, 10 Oct 10:00am", Storage.stringToMillisecond("Wed, 10 Oct 10:00am"), false);
	private Task CATEGORY3_EVENT1 = new Task(CATEGORY3, "Cycling @ East Coast", "", 
			"Sat, 17 Oct 3:00pm", "Sat, 17 Oct 10:00pm", Storage.stringToMillisecond("Sat, 17 Oct 3:00pm"),
			Storage.stringToMillisecond("Sat, 17 Oct 10:00pm"), -1, "Fri, 16 Oct 10:00am",
			Storage.stringToMillisecond("Fri, 16 Oct 10:00am"));
	private Task CATEGORY3_EVENT2 = new Task(CATEGORY3, "3D'09 gathering @ Mr Teo's house", "", 
			"Sun, 27 Dec 1:00pm", "Sun, 27 Dec 9:00pm", Storage.stringToMillisecond("Sun, 27 Oct 1:00pm"),
			Storage.stringToMillisecond("Sun, 27 Dec 10:00pm"), -1, "Sat, 27 Oct 10:00am",
			Storage.stringToMillisecond("Sat, 27 Oct 10:00am"));

	Storage storage;

	@Before
	public void initialize() {
		storage = new Storage(TEST_FILE_NAME);
	}

	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/

	@Test
	public void testAddCategories() {

		ArrayList<String> categoryNames = new ArrayList<String> ();
		categoryNames.addAll(Arrays.asList(CATEGORY1, CATEGORY2, CATEGORY3));

		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);

		assertEquals(categoryNames, storage.getCategories());
		
		storage.deleteAll();
	}

	@Test
	public void testAddFloatingTask() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		storage.addFloatingTask(CATEGORY2_FLOAT1);
		storage.addFloatingTask(CATEGORY3_FLOAT1);

		assertEquals(CATEGORY2_FLOAT1.getTaskId(), storage.getCategoryList().get(1).
				getFloatTasks().get(CATEGORY2_FLOAT1.getTaskId()).getTaskId());
		//assertEquals(CATEGORY3_FLOAT1.getTaskId(), storage.getCategoryList().get(2).
			//	getFloatTasks().get(CATEGORY3_FLOAT1.getTaskId()).getTaskId());
		
		storage.deleteAll();
	}


	@Test
	public void testAddTask() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);

		storage.addTask(CATEGORY1_TODO1);
		storage.addTask(CATEGORY1_TODO2);
		storage.addTask(CATEGORY2_TODO1);
		storage.addTask(CATEGORY3_TODO1);

		assertEquals(CATEGORY1_TODO1.getName(), storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO1.getTaskId()).getName());
		assertEquals(CATEGORY1_TODO2.getName(), storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO2.getTaskId()).getName());
		assertEquals(CATEGORY2_TODO1.getName(), storage.getCategoryList().get(1).getTasks().
				get(CATEGORY2_TODO1.getTaskId()).getName());
		//assertEquals(CATEGORY3_TODO1.getName(), storage.getCategoryList().get(2).getTasks().
			//	get(CATEGORY3_TODO1.getTaskId()).getName());
		
		storage.deleteAll();
	}

	@Test
	public void testAddEvent() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);

		storage.addEvent(CATEGORY3_EVENT1);
		storage.addEvent(CATEGORY3_EVENT2);

		assertEquals(CATEGORY3_EVENT1.getName(), storage.getCategoryList().get(2).
				getEvents().get(CATEGORY3_EVENT1.getTaskId()).getName());
		assertEquals(CATEGORY3_EVENT2.getName(),storage.getCategoryList().get(2).
				getEvents().get(CATEGORY3_EVENT2.getTaskId()).getName());
		
		storage.deleteAll();
	}

	/****************************************************************************
	 * 									UPDATE
	 ***************************************************************************/
	
	@Test
	public void testSetCategoryColour() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);

		storage.setCategoryColour(CATEGORY1, COLOUR_BLUE);
		storage.setCategoryColour(CATEGORY2, COLOUR_GREEN);

		assertEquals(COLOUR_BLUE, storage.getCategoryList().get(0).getCategoryColour());
		assertEquals(COLOUR_GREEN, storage.getCategoryList().get(1).getCategoryColour());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetCategory() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		storage.addCategory(CATEGORY4);

		CATEGORY3_EVENT1.setIndex(6);
		CATEGORY3_EVENT2.setIndex(7);
		storage.addEvent(CATEGORY3_EVENT1);
		storage.addEvent(CATEGORY3_EVENT2);
		
		storage.setCategory(6, CATEGORY4);
		storage.setCategory(7, CATEGORY4);
		
		assertEquals(CATEGORY4, storage.getCategoryList().get(2).getEvents().
				get(CATEGORY3_EVENT1.getTaskId()).getCategory());
		assertEquals(CATEGORY4, storage.getCategoryList().get(2).getEvents().
				get(CATEGORY3_EVENT2.getTaskId()).getCategory());
		
		storage.deleteAll();
	}
	
	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/

	@Test
	public void testDeleteTask() {
		
		//storage.deleteTask(0);
	}
}