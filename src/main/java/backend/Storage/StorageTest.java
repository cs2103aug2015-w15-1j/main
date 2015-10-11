package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.Storage.Task.Task;

public class StorageTest {

	private static final String TEST_FILE_NAME = "test.txt";
	
	private static final String DESCRIPTION_TODO = "Find out how to code query in PHP";
	private static final String DESCRIPTION_EVENT = "Meet at East Coast Lagoon Food Village at 1pm";
	
	private static final String DATE_TODO_START = "Mon, 12 Oct 8:00am";
	private static final String DATE_FLOAT_START = "Tue, 3 Nov 12:00pm";
	private static final String DATE_TODO_END = "Sat, 3 Oct 8:00am";
	private static final String DATE_FLOAT_END = "Tue, 20 Oct 8:00am";
	
	private static final long DATE_TODO_STARTTIME = Storage.stringToMillisecond("Mon, 12 Oct 8:00am");
	private static final long DATE_FLOAT_STARTTIME = Storage.stringToMillisecond("Tue, 3 Nov 12:00pm");
	private static final long DATE_TODO_ENDTIME = Storage.stringToMillisecond("Sat, 3 Oct 8:00am");
	private static final long DATE_FLOAT_ENDTIME = Storage.stringToMillisecond("Tue, 20 Oct 8:00am");
	
	private static final int PRIORITY_3 = 3;
	private static final int PRIORITY_5 = 5;

	/* ======================== Category colors ========================= */
	private static final String COLOUR_BLUE = "#24c6d5";
	private static final String COLOUR_GREEN = "#57dd86";

	/* ======================== Categories =========================== */
	private static final String CATEGORY1 = "CS2102";
	private static final String CATEGORY2 = "CS2103";
	private static final String CATEGORY3 = "Personal";
	private static final String CATEGORY4 = "Outings";

	/* ======================== CS2102 Tasks ========================= */
	private Task CATEGORY1_TODO1;
	private Task CATEGORY1_TODO2;

	/* ======================== CS2103 Tasks ========================= */
	private Task CATEGORY2_TODO1;
	private Task CATEGORY2_FLOAT1;

	/* ======================== Personal Tasks ========================= */
	private Task CATEGORY3_TODO1;
	private Task CATEGORY3_FLOAT1;
	private Task CATEGORY3_EVENT1;
	private Task CATEGORY3_EVENT2;

	Storage storage;

	@Before
	public void initialize() {
		storage = new Storage(TEST_FILE_NAME);
		setUp();
	}
	
	@Before 
	public void setUp() {
		CATEGORY1_TODO1 = new Task(CATEGORY1, "Read up on PHP", "", 
				"Sat, 3 Oct 8:00am", Storage.stringToMillisecond("Sat, 3 Oct 8:00am"), 5, 
				"Fri, 2 Oct 6:00pm", Storage.stringToMillisecond("Fri, 2 Oct 6:00pm"),  true);
		CATEGORY1_TODO2 = new Task(CATEGORY1, "Revise SQL queries", 
				"Read up on ALL SQL queries including nested", 
				"Sat, 10 Oct 9:00am", Storage.stringToMillisecond("Sat, 10 Oct 9:00am"), 5, 
				"Fri, 9 Oct 6:00pm", Storage.stringToMillisecond("Fri, 9 Oct 6:00pm"), false);
		CATEGORY2_TODO1 = new Task(CATEGORY2, "Text Buddy CE2", "Change code to OOP style", 
				"Sat, 19 Oct 8:00am", Storage.stringToMillisecond("Sat, 19 Oct 8:00am"), 5, 
				"Mon, 21 Oct 10:00am", Storage.stringToMillisecond("Mon, 21 Oct 10:00am"), false);
		CATEGORY2_FLOAT1 = new Task(CATEGORY2, "Watch webcast", "Watch before November", -1, 
				"Tue, 20 Oct 8:00am", Storage.stringToMillisecond("Tue, 20 Oct 8:00am"), false);
		CATEGORY3_TODO1 = new Task(CATEGORY3, "Help mum buy groceries", "Every Saturday!", 
				"", -1, 3, "", -1, false);
		CATEGORY3_FLOAT1 = new Task(CATEGORY3, "Cut hair someday", "Cut before December", -1, 
				"Wed, 10 Oct 10:00am", Storage.stringToMillisecond("Wed, 10 Oct 10:00am"), false);
		CATEGORY3_EVENT1 = new Task(CATEGORY3, "Cycling @ East Coast", "", 
				"Sat, 17 Oct 3:00pm", "Sat, 17 Oct 10:00pm", Storage.stringToMillisecond("Sat, 17 Oct 3:00pm"),
				Storage.stringToMillisecond("Sat, 17 Oct 10:00pm"), -1, "Fri, 16 Oct 10:00am",
				Storage.stringToMillisecond("Fri, 16 Oct 10:00am"));
		CATEGORY3_EVENT2 = new Task(CATEGORY3, "3D'09 gathering @ Mr Teo's house", "", 
				"Sun, 27 Dec 1:00pm", "Sun, 27 Dec 9:00pm", Storage.stringToMillisecond("Sun, 27 Oct 1:00pm"),
				Storage.stringToMillisecond("Sun, 27 Dec 10:00pm"), -1, "Sat, 27 Oct 10:00am",
				Storage.stringToMillisecond("Sat, 27 Oct 10:00am"));
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
		assertEquals(CATEGORY3_FLOAT1.getTaskId(), storage.getCategoryList().get(2).
				getFloatTasks().get(CATEGORY3_FLOAT1.getTaskId()).getTaskId());
		
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
		assertEquals(CATEGORY3_TODO1.getName(), storage.getCategoryList().get(2).getTasks().
				get(CATEGORY3_TODO1.getTaskId()).getName());
		
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

		CATEGORY3_EVENT1.setIndex(5);
		CATEGORY3_EVENT2.setIndex(6);
		storage.addEvent(CATEGORY3_EVENT1);
		storage.addEvent(CATEGORY3_EVENT2);
		
		storage.setCategory(CATEGORY3_EVENT1.getIndex(), CATEGORY4);
		storage.setCategory(CATEGORY3_EVENT2.getIndex(), CATEGORY4);
		
		assertEquals(CATEGORY4, storage.getCategoryList().get(2).getEvents().
				get(CATEGORY3_EVENT1.getTaskId()).getCategory());
		assertEquals(CATEGORY4, storage.getCategoryList().get(2).getEvents().
				get(CATEGORY3_EVENT2.getTaskId()).getCategory());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetDescription() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		CATEGORY1_TODO1.setIndex(5);
		CATEGORY3_EVENT2.setIndex(6);
		storage.addTask(CATEGORY1_TODO1);
		storage.addEvent(CATEGORY3_EVENT2);
		
		storage.setDescription(CATEGORY1_TODO1.getIndex(), DESCRIPTION_TODO);
		storage.setDescription(CATEGORY3_EVENT2.getIndex(), DESCRIPTION_EVENT);
		
		assertEquals(DESCRIPTION_TODO, storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO1.getTaskId()).getDescription());
		assertEquals(DESCRIPTION_EVENT, storage.getCategoryList().get(2).getEvents().
				get(CATEGORY3_EVENT2.getTaskId()).getDescription());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetStartDate() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		CATEGORY1_TODO1.setIndex(5);
		CATEGORY2_FLOAT1.setIndex(6);
		storage.addTask(CATEGORY1_TODO1);
		storage.addFloatingTask(CATEGORY2_FLOAT1);
		
		storage.setStartDate(CATEGORY1_TODO1.getIndex(), DATE_TODO_STARTTIME, DATE_TODO_START);
		storage.setStartDate(CATEGORY2_FLOAT1.getIndex(), DATE_FLOAT_STARTTIME, DATE_FLOAT_START);
		
		assertEquals(DATE_TODO_START, storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO1.getTaskId()).getStartDate());
		assertEquals(DATE_FLOAT_START, storage.getCategoryList().get(1).getFloatTasks().
				get(CATEGORY2_FLOAT1.getTaskId()).getStartDate());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetEndDate() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		CATEGORY1_TODO1.setIndex(5);
		CATEGORY2_FLOAT1.setIndex(6);
		storage.addTask(CATEGORY1_TODO1);
		storage.addFloatingTask(CATEGORY2_FLOAT1);
		
		storage.setDeadline(CATEGORY1_TODO1.getIndex(), DATE_TODO_ENDTIME, DATE_TODO_END);
		storage.setDeadline(CATEGORY2_FLOAT1.getIndex(), DATE_FLOAT_ENDTIME, DATE_FLOAT_END);
		
		assertEquals(DATE_TODO_END, storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO1.getTaskId()).getEndDate());
		assertEquals(DATE_FLOAT_END, storage.getCategoryList().get(1).getFloatTasks().
				get(CATEGORY2_FLOAT1.getTaskId()).getEndDate());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetReminder() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		CATEGORY1_TODO1.setIndex(5);
		CATEGORY2_FLOAT1.setIndex(6);
		storage.addTask(CATEGORY1_TODO1);
		storage.addFloatingTask(CATEGORY2_FLOAT1);
		
		storage.setReminder(CATEGORY1_TODO1.getIndex(), DATE_TODO_STARTTIME, DATE_TODO_START);
		storage.setReminder(CATEGORY2_FLOAT1.getIndex(), DATE_FLOAT_STARTTIME, DATE_FLOAT_START);
		
		assertEquals(DATE_TODO_START, storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO1.getTaskId()).getReminderDate());
		assertEquals(DATE_FLOAT_START, storage.getCategoryList().get(1).getFloatTasks().
				get(CATEGORY2_FLOAT1.getTaskId()).getReminderDate());
		
		storage.deleteAll();
	}
	
	@Test
	public void testSetPriority() {
		
		storage.addCategory(CATEGORY1);
		storage.addCategory(CATEGORY2);
		storage.addCategory(CATEGORY3);
		
		CATEGORY1_TODO2.setIndex(5);
		CATEGORY3_FLOAT1.setIndex(6);
		storage.addTask(CATEGORY1_TODO2);
		storage.addFloatingTask(CATEGORY3_FLOAT1);
		
		storage.setPriority(CATEGORY1_TODO2.getIndex(), PRIORITY_5);
		storage.setPriority(CATEGORY3_FLOAT1.getIndex(), PRIORITY_3);
		
		assertEquals(PRIORITY_5, storage.getCategoryList().get(0).getTasks().
				get(CATEGORY1_TODO2.getTaskId()).getPriority());
		assertEquals(PRIORITY_3, storage.getCategoryList().get(2).getFloatTasks().
				get(CATEGORY3_FLOAT1.getTaskId()).getPriority());
		
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