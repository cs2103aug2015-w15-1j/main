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
	
	private Task FLOAT = new Task(TaskType.FLOATING, RecurrenceType.NONE, 2, -1, "Watch webcast", 
			"Watch before November", "", "Tue, 20 Oct 8:00am", "");
	private Task TODO = new Task(TaskType.TODO, RecurrenceType.NONE , 0, 5, "Read up on PHP", "", 
			"Sat, 3 Oct 8:00am", "", "Fri, 2 Oct 6:00pm");
	private Task TODO_RECUR = new Task(TaskType.TODO, RecurrenceType.DAY, 2, -1, "Water plants", 
			"", "", "", "Wed, 10 Oct 10:00am");
	private Task EVENT = new Task(TaskType.FLOATING, RecurrenceType.NONE, 2, -1, "Watch webcast", 
			"Watch before November", "", "Tue, 20 Oct 8:00am", "");
	
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
		
		taskList.add(FLOAT);
		taskList.add(TODO);
		taskList.add(TODO_RECUR);
		taskList.add(EVENT);
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
