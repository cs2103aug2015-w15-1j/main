//@@author A0121284N
package main.java.backend.Logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogicUnitTest {
	
	private static LogicFacade testLogicFacade; 

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testLogicFacade = LogicFacade.getInstance();
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testLogicFacade.execute("deleteAll");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}
	
//	@Test
//	public void testExecuteUndoWhenCommandStackEmpty() {
//		String result = testLogicFacade.execute("undo");
//		assertEquals(result, "Invalid command. Please try again.");
//	}

	@Test
	public void testExecuteAdd() {
		String result = testLogicFacade.execute("add Call beloved girlfriend");
		assertEquals(result, "Task Call beloved girlfriend has been added");
	}
	
	@Test
	public void testExecuteEdit() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("1 priority 5");
		assertEquals(result, "Task 1 has been set to priority 5");
	}
	
	@Test
	public void testExecuteUndo() {
		String result = testLogicFacade.execute("add find beloved girlfriend");
		result = testLogicFacade.execute("undo");
		assertEquals(result, "Undo successfully.");
	}
	
	@Test
	public void testExecuteRedo() {
		String result = testLogicFacade.execute("add find beloved girlfriend");
		result = testLogicFacade.execute("undo");
		result = testLogicFacade.execute("redo");
		assertEquals(result, "Redo successfully.");
	}
	
	@Test
	public void testConvertFromFloatToToDo() {
		String result = testLogicFacade.execute("add get flowers beloved girlfriend");
		result = testLogicFacade.execute("1 deadline 30 oct");
		assertEquals(result, "Task 1 deadline has been set to Sun, 30 Oct 16, 11:59pm" );
	}
	
	@Test
	public void testConvertFromFloatToEvents() {
		String result = testLogicFacade.execute("add go on a date beloved girlfriend");
		result = testLogicFacade.execute("1 event 24 dec 12pm to 25 dec");
		assertEquals(result, "Event 1 has been setted to Thu, 24 Dec 15, 12pm till Fri, 25 Dec 15, 9pm");
	}
	
	@Test
	public void testSetDescription() {
		String result = testLogicFacade.execute("add get flowers beloved girlfriend");
		result = testLogicFacade.execute("1 description Get flowers from shop A");
		assertEquals(result, "InvalidDateError: 'shop A' is not an acceptable date format");
	}
	
	@Test
	public void testSetReminder() {
		String result = testLogicFacade.execute("add get flowers beloved girlfriend");
		result = testLogicFacade.execute("1 reminder 23 dec 10am");
		assertEquals(result, "Reminder for Task 1 has been set to be at Wed, 23 Dec 15, 10am");
	}
	
	//Tried to do equivalence partitioning here. Valid value is between
	//1- 5. So checking 0 which is below the min and 10 which is above
	//the max can already check for invalid inputs. Also applies for
	//Boundary Value Analysis
	@Test
	public void testSetPriorityValid() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("1 priority 1");
		assertEquals(result,"Task 1 has been set to priority 1");
	}
	
	@Test
	public void testSetPriorityInvalidLargerThanRange() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("1 priority 10");
		assertEquals(result, "InvalidPriorityError: '10' is not between 1 to 5");
	}
	
	@Test
	public void testSetPriorityInvalidSmallerThanRange() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("1 priority 0");
		assertEquals(result, "InvalidPriorityError: '0' is not between 1 to 5");
	}
	
	@Test
	public void testDelete() {
		String result = testLogicFacade.execute("add Impromptu Meeting with Boss");
		result = testLogicFacade.execute("delete 1");
		assertEquals(result, "Task 1 has been deleted");
	}
	
	@Test
	public void testSetMultipleFieldsForTask() {
		String result = testLogicFacade.execute("add Complete Project D Proposal deadline 22 dec");
		result = testLogicFacade.execute("1 priority 5 description Finish Subsections A to D reminder 19 dec");
		assertEquals(result,"Fields have been updated");
	}
	
	@Test
	public void testSearchFound(){
		String result = testLogicFacade.execute("add find beloved girlfriend");
		result = testLogicFacade.execute("add Get something for beloved girlfriend deadline 24 dec");
		result = testLogicFacade.execute("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testLogicFacade.execute("search beloved girlfriend");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchNotFound() {
		String result = testLogicFacade.execute("search picnic with beloved girlfriend");
		assertEquals(result,"Input not found");
	}
	
	@Test
	public void testDoneFloat() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneEvent() {
		String result = testLogicFacade.execute("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testLogicFacade.execute("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneToDo() {
		String result = testLogicFacade.execute("add Get something for beloved girlfriend deadline 24 dec");
		result = testLogicFacade.execute("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testUndoneFloat() {
		String result = testLogicFacade.execute("add Plan a date with beloved girlfriend");
		result = testLogicFacade.execute("done 1");
		result = testLogicFacade.execute("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test
	public void testUndoneEvent() {
		String result = testLogicFacade.execute("add Meeting with Supervisor B event 22 dec 10am to 22 dec 10am");
		result = testLogicFacade.execute("done 1");
		result = testLogicFacade.execute("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test
	public void testUndoneToDo() {
		String result = testLogicFacade.execute("add Get something for beloved girlfriend deadline 24 dec");
		result = testLogicFacade.execute("done 1");
		result = testLogicFacade.execute("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test 
	public void testShowFloatLong() {
		String result = testLogicFacade.execute("show Floating");
		assertEquals(result, "showF");
	}
	
	@Test 
	public void testDeleteAll() {
		testLogicFacade.execute("add Float Task A");
		testLogicFacade.execute("add Task B deadline 30 dec");
		testLogicFacade.execute("add Soemthing happening C event 30 dec 10am to 30 dec 12pm");
		String result = testLogicFacade.execute("deleteAll");
		assertEquals(result,"Everything has been deleted");
	}

}
