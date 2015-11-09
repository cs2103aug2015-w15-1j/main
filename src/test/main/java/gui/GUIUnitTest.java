//@@author A0126125R
package main.java.gui;

import static org.junit.Assert.*;

//@@author A0126125R

import org.junit.Test;

public class GUIUnitTest {
	GuiController controller = new GuiController();
	//white box testing
	@Test
	public void testAddExecuteCommand() {
		assertEquals("Everything has been deleted", controller.executeCommand("deleteAll"));
		//add
		//add task
		assertEquals("Task submit proposal has been added", controller.executeCommand("add submit proposal deadline 31 dec"));
		
		//add event
		assertEquals("Task party has been added", controller.executeCommand("add party event 31 dec 7 pm"));
		
		//add float
		assertEquals("Task buy food has been added",controller.executeCommand("add buy food"));
		
		//add overdue
		assertEquals("Task overdue has been added", controller.executeCommand("add overdue deadline 10 oct"));
		
		//add date does not exist
		//date becomes 1st march
		assertEquals("Task email has been added", controller.executeCommand("add email deadline 29 feb"));
		
		//date is years ahead
		assertEquals("Task taskname has been added",controller.executeCommand("add taskname deadline 1 jan 2020"));
	}
	@Test
	public void testDeleteExecuteCommand() {
		assertEquals("Everything has been deleted", controller.executeCommand("deleteAll"));
		controller.executeCommand("add submit proposa deadline 31 dec 7pm");
		//delete
		//task that exists
		assertEquals("Task 1 has been deleted",controller.executeCommand("delete 1"));
		
		//task that doesn't exist
		assertEquals("Task index is invalid. Please try again.",controller.executeCommand("delete 100"));
	}
	
	@Test
	public void testUpdateExecuteCommand() {
		assertEquals("Everything has been deleted", controller.executeCommand("deleteAll"));
		controller.executeCommand("add submit proposal deadline 31 dec 7pm;");
		controller.executeCommand("add dance event 25 dec to 26 dec;");
		//updating
		//priority 
		assertEquals("InvalidPriorityError: '-5' is not between 1 to 5",controller.executeCommand("1 priority -5"));
		assertEquals("Task 1 has been set to priority 5",controller.executeCommand("1 priority 5"));
		assertEquals("InvalidPriorityError: '10' is not between 1 to 5",controller.executeCommand("1 priority 10"));
		
		//changing dates
		//date exist
		controller.executeCommand("add submit proposal deadline 31 dec 7pm");
		assertEquals("Task 1 deadline has been set to Sun, 20 Dec 15, 7pm",controller.executeCommand("1 deadline 20 dec 7 pm"));
		
		//date dne
		assertEquals("InvalidDayOfMonthError: The date '30 feb' does not exist (February only has 29 days!)",controller.executeCommand("1 deadline 30 feb 7 pm"));
		//date is blank
		assertEquals("EmptyFieldError: Please enter content for the command 'deadline'",controller.executeCommand("1 deadline "));
		
		assertEquals("Everything has been deleted",controller.executeCommand("deleteAll"));
		controller.executeCommand("add project meeting deadline 31 dec 3pm");
		//Description
		controller.executeCommand("add submit proposal deadline 31 dec 7pm");
		assertEquals("Description for task 1 has been set", controller.executeCommand("1 description hello"));
		
		// undo
		assertEquals("Undo successfully.", controller.executeCommand("undo"));
		
		//reminder
		//reminder date after deadline/event dates
		//reminder before deadline/event dates
		assertEquals("Reminder for Task 1 has been set to be at Sun, 20 Dec 15, 9am",controller.executeCommand("1 reminder 20 dec"));
		//change name
	}
}