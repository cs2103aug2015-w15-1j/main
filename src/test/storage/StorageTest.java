//@author A0126258A
package storage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageFacade;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;

public class StorageTest {
	
	/* ======================== Categories =========================== */
	private static final String CATEGORY1 = "CS2102";
	private static final String CATEGORY2 = "CS2103";
	private static final String CATEGORY3 = "Personal";
	
	/* ======================== CS2102 Tasks ========================= */
	private Task CATEGORY1_TODO1 = new Task(TaskType.TODO, RecurrenceType.DAY, 2, 5, CATEGORY1, "Read up on PHP", "", 
			"Sat, 3 Oct 8:00am", "", "Fri, 2 Oct 6:00pm");
	private Task CATEGORY1_TODO2 = new Task(TaskType.TODO, RecurrenceType.DAY, 2, 5, CATEGORY1, "Revise SQL queries", 
			"Read up on ALL SQL queries including nested", 
			"Sat, 10 Oct 9:00am", "", "Fri, 9 Oct 6:00pm");

	/* ======================== CS2103 Tasks ========================= */
	private Task CATEGORY2_TODO1 = new Task(TaskType.TODO, RecurrenceType.DAY, 2, 4, CATEGORY2, "Text Buddy CE2", 
			"Change code to OOP style", "Sat, 19 Oct 8:00am", 
			"", "Mon, 21 Oct 10:00am");
	private Task CATEGORY2_FLOAT1 = new Task(TaskType.FLOATING, RecurrenceType.DAY, 2, -1, CATEGORY2, "Watch webcast", 
			"Watch before November", "", "Tue, 20 Oct 8:00am", "");

	/* ======================== Personal Tasks ========================= */
	private Task CATEGORY3_TODO1 = new Task(TaskType.TODO, RecurrenceType.DAY, 2, 5, CATEGORY3, "Help mum buy groceries", 
			"Every Saturday!", "", "", "");
	private Task CATEGORY3_FLOAT1 = new Task(TaskType.FLOATING, RecurrenceType.DAY, 2, -1, CATEGORY3, "Cut hair someday", 
			"Cut before December", "", "", "Wed, 10 Oct 10:00am");
	private Task CATEGORY3_EVENT1 = new Task(TaskType.EVENT, RecurrenceType.DAY, 2, 3, CATEGORY3, "Cycling @ East Coast", "", 
			"Sat, 17 Oct 3:00pm", "Sat, 17 Oct 10:00pm", "Fri, 16 Oct 10:00am");
	private Task CATEGORY3_EVENT2 = new Task(TaskType.EVENT, RecurrenceType.DAY, 2, 3, CATEGORY3, "3D'09 gathering @ Mr Teo's house", "", 
			"Sun, 27 Dec 1:00pm", "Sun, 27 Dec 9:00pm", "Sat, 27 Oct 10:00am");
	
	private ArrayList<Task> taskList;
	private int taskId = 0;
	
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
		taskList.add(CATEGORY3_EVENT1);
		taskList.add(CATEGORY3_EVENT2);
	}
	
	@Test
	public void testLoad() {
		
		storage.save(taskList);
		assertEquals(taskList.toString(), storage.load().toString());
	}
	
}
