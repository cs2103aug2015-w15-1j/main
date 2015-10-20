package main.java.gui;

import static org.junit.Assert.*;


import org.junit.Test;

public class GUIControllerTest {
	GUIController controller = new GUIController();
	//white box testing
	@Test
	public void testExecuteCommand() {
		//add
		//add task
		assertEquals("Task submit proposal has been added", controller.executeCommand("add submit proposal deadline 31 dec"));
		
		//add event
		assertEquals("Task party has been added", controller.executeCommand("add party event 31 dec 7 pm"));
		
		//add float
		assertEquals("Task buy food has been added",controller.executeCommand("add buy food"));
		
		//add overdue
		assertEquals("Task overdue has been added", controller.executeCommand("add overdue deadline 10 oct"));
		
		//add date does not exist(don't know)
		//date becomes 1st march
		//assertEquals("Invalid Command. Please try again.", controller.executeCommand("add email deadline 29 feb"));
		
		//date is years ahead
		//assertEquals("Task taskname has been added",controller.executeCommand("add taskname deadline 1 jan 2020"));
		
		//delete
		//task that exists
		assertEquals("Task 1 has been deleted",controller.executeCommand("delete 1"));
		
		//task that doesn't exist
		assertEquals("Invalid Command. Please try again.",controller.executeCommand("delete 100"));
		
		//updating
		//priority 
		assertEquals("Invalid Command. Please try again.",controller.executeCommand("1 priority -5"));
		assertEquals("Task 1 has been set to priority 5",controller.executeCommand("1 priority 5"));
		assertEquals("Invalid Command. Please try again.",controller.executeCommand("1 priority 10"));
		
		//changing dates
		//date exist
		controller.executeCommand("add submit proposal deadline 31 dec 7 pm");
		assertEquals("Task 1 deadline has been set to Sun, 20 Dec 15 7pm",controller.executeCommand("1 deadline 20 dec 7 pm"));
		
		//date dne (fails. date becomes 1st dec...?)
		//assertEquals("Task 1 deadline has been set to Sun, 20 Dec 07:00PM",controller.executeCommand("1 deadline 32 dec 7 pm"));
		//date is blank (becomes a floating task)
		assertEquals("Invalid Command. Please try again.",controller.executeCommand("1 deadline "));
		
		//Description
		assertEquals("Description for task 1 has been set", controller.executeCommand("1 description hello"));
		
		// undo
		assertEquals("Undo successfully", controller.executeCommand("undo"));
		
		//reminder
		//reminder date after deadline/event dates
		//reminder before deadline/event dates
		assertEquals("Reminder for Task 1 has been set to be at Sun, 20 Dec 15 12pm",controller.executeCommand("1 reminder 20 dec"));
		//change name
		
	}

	@Test
	public void testGetTasksList() {
		
		controller.executeCommand("delete 1");
		controller.retrieveAllData();
		assertEquals("[]",controller.getTasksList().toString());
		
		controller.executeCommand("add taskname deadline 31 dec 7pm");
		controller.retrieveAllData();
		StringBuilder sb = new StringBuilder();
		sb.append("1. taskname "+System.getProperty("line.separator"));
		sb.append("Thu, 31 Dec 07:00PM 2015"+System.getProperty("line.separator"));
		String expected =sb.toString();
		assertEquals("["+expected+"]",controller.getTasksList().toString());
	}

/*	@Test
	public void testGetEventsList() {
		//no task is completed
		assertEquals("[]",controller.getEventsList().toString());
		
	}

	@Test
	public void testGetOverdueList() {
		//no task is completed
		assertEquals("[]",controller.getOverdueList().toString());
		
	}

	@Test
	public void testGetFloatList() {
		//no task is completed
		assertEquals("[]",controller.getFloatList().toString());
		
	}
*/
}
