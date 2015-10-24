/**
 * 
 */
package main.java.backend.Logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Tyson_V7
 *
 */
public class LogicFacadeTest {
	
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
		String result = testLogicFacade.execute("add Kill Tank");
		assertEquals(result, "Task Kill Tank has been added");
	}
	
	@Test
	public void testExecuteEdit() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 priority 5");
		assertEquals(result, "Task 1 has been set to priority 5");
	}
	
	@Test
	public void testExecuteUndo() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("undo");
		assertEquals(result, "Undo successfully");
	}
	
	@Test
	public void testExecuteRedo() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("undo");
		result = testLogicFacade.execute("redo");
		assertEquals(result, "Redo successfully");
	}
	
	@Test
	public void testConvertFromFloatToToDo() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 deadline 30 oct");
		assertEquals(result, "Task 1 deadline has been set to Fri, 30 Oct 15, 11:59pm" );
	}
	
	@Test
	public void testConvertFromFloatToEvents() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 event 18 oct to 19 oct");
		assertEquals(result, "Event 1 has been setted to Tue, 18 Oct 16, 12pm till Wed, 19 Oct 16, 12pm");
	}
	
	@Test
	public void testSetDescription() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 description Kill the Tank! DUH!!");
		assertEquals(result, "Description for task 1 has been set");
	}
	
	@Test
	public void testSetReminder() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 reminder 18 oct 10am");
		assertEquals(result, "Reminder for Task 1 has been set to be at Tue, 18 Oct 16, 10am");
	}
	
	//Tried to do equivalence partitioning here. Valid value is between
	//1- 5. So checking 0 which is below the min and 10 which is above
	//the max can already check for invalid inputs. Also applies for
	//Boundary Value Analysis
	@Test
	public void testSetPriorityValid() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 priority 1");
		assertEquals(result,"Task 1 has been set to priority 1");
	}
	
	@Test
	public void testSetPriorityInvalidLargerThanRange() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 priority 10");
		assertEquals(result, "InvalidPriorityError: '10' is not between 1 to 5");
	}
	
	@Test
	public void testSetPriorityInvalidSmallerThanRange() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("1 priority 0");
		assertEquals(result, "InvalidPriorityError: '0' is not between 1 to 5");
	}
	
	@Test
	public void testDelete() {
		String result = testLogicFacade.execute("add Kill Tank");
		result = testLogicFacade.execute("delete 1");
		assertEquals(result, "Task 1 has been deleted");
	}
	
	@Test
	public void testSetMultipleFieldsForTask() {
		String result = testLogicFacade.execute("add Kill Tank event 20oct to 21oct");
		result = testLogicFacade.execute("1 priority 5 description kill the tank reminder 19 oct");
		assertEquals(result,"Fields have been updated");
	}
	
	@Test
	public void testSearchFound(){
		String result = testLogicFacade.execute("add find beloved girlfriend");
		result = testLogicFacade.execute("Get something for beloved girlfriend deadline 24 dec");
		result = testLogicFacade.execute("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testLogicFacade.execute("search beloved girlfriend");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchNotFound() {
		String result = testLogicFacade.execute("search jet fighter");
		assertEquals(result,"Input not found");
	}

}
