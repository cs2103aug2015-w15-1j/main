//@author A0126258A
package main.java.backend.Storage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageFacade;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;

public class StorageUnitTest { 
	
	/* ======================== CS2102 Tasks ========================= */
	private Task CATEGORY1_TODO1 = new Task(TaskType.TODO, RecurrenceType.NONE , 2, 5, "Read up on PHP", "", 
			"Sat, 3 Oct 8:00am", "", "Fri, 2 Oct 6:00pm");
	private Task CATEGORY1_TODO2 = new Task(TaskType.TODO, RecurrenceType.NONE, 2, 5, "Revise SQL queries", 
			"Read up on ALL SQL queries including nested", 
			"Sat, 10 Oct 9:00am", "", "Fri, 9 Oct 6:00pm");

	/* ======================== CS2103 Tasks ========================= */
	private Task CATEGORY2_TODO1 = new Task(TaskType.TODO, RecurrenceType.NONE, 2, 4, "Text Buddy CE2", 
			"Change code to OOP style", "Sat, 19 Oct 8:00am", 
			"", "Mon, 21 Oct 10:00am");
	private Task CATEGORY2_FLOAT1 = new Task(TaskType.FLOATING, RecurrenceType.NONE, 2, -1, "Watch webcast", 
			"Watch before November", "", "Tue, 20 Oct 8:00am", "");

	/* ======================== Personal Tasks ========================= */
	private Task CATEGORY3_TODO1 = new Task(TaskType.TODO, RecurrenceType.NONE, 2, 5, "Help mum buy groceries", 
			"Every Saturday!", "", "", "");
	private Task CATEGORY3_FLOAT1 = new Task(TaskType.FLOATING, RecurrenceType.DAY, 2, -1, "Cut hair someday", 
			"Cut before December", "", "", "Wed, 10 Oct 10:00am");
	
	private ArrayList<Task> taskList;
	
	private Storage storage;
	
	@Before
	public void init() {
		
		storage = new StorageFacade();
		storage.init();
	}
	
	@Before
	public void setUp() {
		
		taskList = new ArrayList<Task> ();
		
		taskList.add(CATEGORY1_TODO1);
		taskList.add(CATEGORY1_TODO2);
		taskList.add(CATEGORY2_TODO1);
		taskList.add(CATEGORY2_FLOAT1);
		taskList.add(CATEGORY3_TODO1);
		taskList.add(CATEGORY3_FLOAT1);
	}
	
	@Test
	public void testLoad() {
		
		storage.save(taskList);
		assertEquals(taskList.toString(), storage.load().toString());
	}
	
	@Test
	public void testUpdateFile() {
		String fileName = System.getProperty("user.home") + "/Desktop" + "/test.txt";
		String result = storage.updateFilePath(fileName);
		assertEquals(result, String.format("File path is updated successfully to %1$s", fileName));
	}
	
	@Test
	public void testUpdateFileDuplicate() {
		String fileName = System.getProperty("user.home") + "/Desktop" + "/filename.txt";
		storage.updateFilePath(fileName);
		String result = storage.updateFilePath(fileName);
		assertEquals(result, String.format("File is already in %1$s. Please try again with a new file path.", fileName));
	}
	
	@Test
	public void testUpdateFileError() {
		String fileName = "filepath " + "/Desktop";
		String result = storage.updateFilePath(fileName);
		assertEquals(result, "Invalid file path. Please try again");
	}
	
}
