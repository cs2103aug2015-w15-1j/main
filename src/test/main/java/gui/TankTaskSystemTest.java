package main.java.gui;
//@@author A0121284N

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TankTaskSystemTest {
	private static GuiController testTankTask = new GuiController();
	private static String result = "";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testTankTask = new GuiController();
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testTankTask.executeCommand("deleteAll");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testTankTask.executeCommand("deleteAll");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	// ================================================================
    // Input Validation Test
    // ================================================================

	@Test
	public void testInvalidCommandError() {
		testTankTask.executeCommand("add Call beloved girlfriend");
		result = testTankTask.executeCommand("1 5");
		assertEquals(result, "InvalidCommandError: '5' is not recognised as a command");
	}
	
	@Test
	public void testNoCommandError() {
		result = testTankTask.executeCommand("5 ");
		assertEquals(result, "NoCommandError: Please enter a command after the task index '5'");
	}
	
//	@Test
//	public void testDuplicateCommand() {
//		testTankTask.executeCommand("add test to do something from today to tomorrow");
//		result = testTankTask.executeCommand("1 every every week");
//		assertEquals(result, "Task 1 recurring has been set to 1 week");
//	}
	
    // ================================================================
    // Add Operations Test
    // ================================================================
	
	@Test
	public void testAddFloat() {
		result = testTankTask.executeCommand("add Call beloved girlfriend");
		assertEquals(result, "Task Call beloved girlfriend has been added");
	}
	
	@Test
	public void testAddAllCaps() {
		result = testTankTask.executeCommand("ADD TAKE THIS");
		assertEquals(result, "Task TAKE THIS has been added");
	}
	
	@Test
	public void testAddWithOtherCommandsIgnored() {
		result = testTankTask.executeCommand("add Call beloved girlfriend search delete done undone");
		assertEquals(result, "Task Call beloved girlfriend search delete done undone has been added");
	}
	
	@Test
	public void testAddWithOtherCommandsIgnoredTwo() {
		result = testTankTask.executeCommand("add addcat delete search reset sortp exit showf");
		assertEquals(result, "Task addcat delete search reset sortp exit showf has been added");
	}
	
	@Test
	public void testAddWithOtherCommandsIgnoredThree() {
		result = testTankTask.executeCommand("add something delete");
		assertEquals(result, "Task something delete has been added");
	}
	
	@Test
	public void testMultipleAdd() {
		result = testTankTask.executeCommand("add add Call beloved girlfriend");
		assertEquals(result, "Task add Call beloved girlfriend has been added");
	}
	
	@Test
	public void testMultipleAddWithOtherCommands() {
		result = testTankTask.executeCommand("add add Call beloved girlfriend priority 5");
		assertEquals(result, "Task add Call beloved girlfriend has been added");
	}
		
	@Test 
	public void testAddCommandMissingAddError() {
		result = testTankTask.executeCommand("Call beloved girlfriend");
		assertEquals(result, "InvalidWordError: 'Call' is not recognised as a command or index");
	}
	
	@Test
	public void testAddDeadline() {
		result = testTankTask.executeCommand("add Plan to do something deadline december 23");
		assertEquals(result, "Task Plan to do something has been added");
	}
	
	@Test
	public void testAddDeadlineShortForm() {
		result = testTankTask.executeCommand("add Plan to do something by December 23");
		assertEquals(result, "Task Plan to do something has been added");
	}
	
	@Test
	public void testAddDeadlineShortFormTwo() {
		result = testTankTask.executeCommand("add Plan to do something dea next tue");
		assertEquals(result, "Task Plan to do something has been added");
	}
	
	@Test 
	public void testAddOldDeadline() {
		result = testTankTask.executeCommand("add old stuff deadline 24/12/1995");
		assertEquals(result, "Task old stuff has been added");
	}
	
	@Test 
	public void testAddOldDeadlineTwo() {
		result = testTankTask.executeCommand("add old stuff deadline 24 dec 95");
		assertEquals(result, "Task old stuff has been added");
	}
	
	@Test
	public void testMultipleDeadlineError() {
		result = testTankTask.executeCommand("add something deadline today deadline today");
		assertEquals(result, "DuplicateCommandError: Duplicate command 'deadline'");
	}
	
	@Test
	public void testAddEvent() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25/12/15 12:00 to 25/12/15 23:00");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithEndTimeEarlierThanStartTime() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25 dec 12:00 to 08:00");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithEndTimeEarlierThanStartTimeTwo() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25december 12:30 to 12:10");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithEndTimeEarlierThanStartTimeThree() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 12:30 25/12/15 to 12:30");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithNoEndTime() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25/12/15 12:30 ");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test 
	public void testAddEventWithOnlyStartDay() {
		result = testTankTask.executeCommand("add Celebrate New Year With Beloved Girlfriend event 1Jan");
		assertEquals(result, "Task Celebrate New Year With Beloved Girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithEndDateButNoEndTime() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25/12/15 1230 to 26/12/15 ");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddEventWithEndDateButNoEndTimeTwo() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend event 25/12/15 1230 to ");
		assertEquals(result, "NoEndDateError: Please enter an end date after the command word 'to'");
	}
	
	@Test
	public void testAddEventWithDateConflictError() {
		result = testTankTask.executeCommand("add something deadline tomorrow from today to tomorrow");
		assertEquals(result, "ConflictingDatesError: Task cannot have both deadline and event date");
	}
	
	@Test
	public void testAddEventShortForm() {
		result = testTankTask.executeCommand("add Go on a date with beloved girlfriend from dec 25 12pm to 25 dec 11pm");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddToDoAFewDaysLater() {
		result = testTankTask.executeCommand("Add Go on a date with beloved girlfriend by 3 days later");
		assertEquals(result, "Task Go on a date with beloved girlfriend has been added");
	}
	
	@Test
	public void testAddToDoAFewSaturdaysLater() {
		result = testTankTask.executeCommand("Add Help beloved girlfriend clean house by 2 Saturdays later");
		assertEquals(result, "Task Help beloved girlfriend clean house has been added");
	}
	
	@Test
	public void testAddToDoAFewMonthsLater() {
		result = testTankTask.executeCommand("Add Have a dental appointment by 3 months later");
		assertEquals(result, "Task Have a dental appointment has been added");
	}
	
	@Test
	public void testAddToDoFewYearsLater() {
		result = testTankTask.executeCommand("Add Propose to beloved girlfriend by 6 years later");
		assertEquals(result, "Task Propose to beloved girlfriend has been added");
	}
	
	@Test
	public void testAddRecurrenceToDoDay() {
		testTankTask.executeCommand("add Call beloved girlfriend deadline 10pm today");
		result = testTankTask.executeCommand("1 every day");
		assertEquals(result, "Task 1 recurring has been set to 1 day");
	}
	
	@Test
	public void testAddRecurrenceToDoEveryFortyDays() {
		testTankTask.executeCommand("add Cut hair deadline 10pm");
		result = testTankTask.executeCommand("1 every 40 days");
		assertEquals(result, "Task 1 recurring has been set to 40 day");
	}
	
	@Test
	public void testAddRecurrenceToDoDayAnotherWay() {
		testTankTask.executeCommand("add Call beloved girlfriend deadline 10pm");
		result = testTankTask.executeCommand("1 recur day");
		assertEquals(result, "Task 1 recurring has been set to 1 day");
	}
	
	@Test
	public void testAddRecurrenceToDoWeek() {
		testTankTask.executeCommand("add Watch movie with beloved girlfriend from Sunday 6pm to 10pm");
		result = testTankTask.executeCommand("1 every week");
		assertEquals(result, "Task 1 recurring has been set to 1 week");
	}
	
	@Test
	public void testAddRecurrenceToDoEveryTwoWeek() {
		testTankTask.executeCommand("add Clean house from Sunday 6am to 10am");
		result = testTankTask.executeCommand("1 every 2 weeks");
		assertEquals(result, "Task 1 recurring has been set to 2 week");
	}
	
	@Test
	public void testAddRecurrenceToDoWeekAnotherWay() {
		testTankTask.executeCommand("add Watch movie with beloved girlfriend from Sunday 6pm to 10pm");
		result = testTankTask.executeCommand("1 recur week");
		assertEquals(result, "Task 1 recurring has been set to 1 week");
	}
	
	@Test
	public void testAddRecurrenceToDoMonth() {
		testTankTask.executeCommand("add Celebrate Monthsary with beloved girlfriend from 16 nov");
		result = testTankTask.executeCommand("1 every month");
		assertEquals(result, "Task 1 recurring has been set to 1 month");
	}
	
	@Test
	public void testAddRecurrenceFloatingMonthFail() {
		testTankTask.executeCommand("add Celebrate Monthsary with beloved girlfriend 16 nov");
		result = testTankTask.executeCommand("1 every month");
		assertEquals(result, "Unable to recur floating tasks");
	}
	
	@Test
	public void testAddRecurrenceToDoEverySixMonth() {
		testTankTask.executeCommand("add Visit mum's grave to tidy it deadline 22 dec");
		result = testTankTask.executeCommand("1 every 6 months");
		assertEquals(result, "Task 1 recurring has been set to 6 month");
	}
	
	@Test
	public void testAddRecurrenceToDoMonthAnotherWay() {
		testTankTask.executeCommand("add Celebrate Monthsary with beloved girlfriend deadline 16 nov");
		result = testTankTask.executeCommand("1 recur month");
		assertEquals(result, "Task 1 recurring has been set to 1 month");
	}
	
	@Test
	public void testAddRecurrenceToDoYear() {
		testTankTask.executeCommand("add Celebrate Anniversary with beloved girlfriend deadline 16 nov");
		result = testTankTask.executeCommand("1 every year");
		assertEquals(result, "Task 1 recurring has been set to 1 year");
	}
	
	@Test
	public void testAddRecurrenceToDoYearAnotherWay() {
		testTankTask.executeCommand("add Celebrate Anniversary with beloved girlfriend deadline 16 nov");
		result = testTankTask.executeCommand("1 recur year");
		assertEquals(result, "Task 1 recurring has been set to 1 year");
	}
	
	@Test
	public void testAddRecurrenceToDoTwoYears() {
		testTankTask.executeCommand("add Get new laptop deadline 15 nov");
		result = testTankTask.executeCommand("1 every 2 year");
		assertEquals(result, "Task 1 recurring has been set to 2 year");
	}
	
	@Test
	public void testAddRecurrenceToDoTwoYearsWithS() {
		testTankTask.executeCommand("add Get new laptop deadline 15 nov");
		result = testTankTask.executeCommand("1 every 2 years");
		assertEquals(result, "Task 1 recurring has been set to 2 year");
	}
	
	@Test
	public void testAddRecurrenceToDoWithFrequencyError() {
		testTankTask.executeCommand("add Go to bed");
		result = testTankTask.executeCommand("1 every 2am");
		assertEquals(result, "InvalidFrequencyError: Please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency");
	}
	
	@Test
	public void testAddRecurrenceEventDay() {
		testTankTask.executeCommand("add Send a Sweet Dreams SMS to beloved girlfriend from 11:55PM to 11:59PM");
		result = testTankTask.executeCommand("1 every day");
		assertEquals(result, "Task 1 recurring has been set to 1 day");
	}
	
	@Test
	public void testAddRecurrenceEventDayWithDuplicate() {
		testTankTask.executeCommand("add Send a Sweet Dreams SMS to beloved girlfriend from 11:55PM to 11:59PM");
		result = testTankTask.executeCommand("1 every day des Must do this every day");
		assertEquals(result, "Fields have been updated");
	}
	
	@Test
	public void testAddRecurrenceEventThreeDay() {
		testTankTask.executeCommand("add Update Supervisor on work progress from wed 9am to 10am");
		result = testTankTask.executeCommand("1 every 3 days");
		assertEquals(result, "Task 1 recurring has been set to 3 day");
	}
	
	@Test
	public void testAddRecurrenceEventWeek() {
		testTankTask.executeCommand("add Have a homecooked dinner with beloved girlfriend from saturday 6pm to 10pm");
		result = testTankTask.executeCommand("1 every week");
		assertEquals(result, "Task 1 recurring has been set to 1 week");
	}
	
	@Test
	public void testAddRecurrenceEventEveryTwoWeek() {
		testTankTask.executeCommand("add Have a team meeting from 0800 to 10");
		result = testTankTask.executeCommand("1 every 2 week");
		assertEquals(result, "Task 1 recurring has been set to 2 week");
	}
	
	@Test
	public void testAddRecurrenceEventMonth() {
		testTankTask.executeCommand("add Have monthsary date with beloved girlfriend from 16/11/15 12:00 to 10:00");
		result = testTankTask.executeCommand("1 every month");
		assertEquals(result,"Task 1 recurring has been set to 1 month");
	}
	
	@Test
	public void testAddRecurrenceEventEveryThreeMonth() {
		testTankTask.executeCommand("add Have team bonding activities from sat 1200 to 1000");
		result = testTankTask.executeCommand("1 every 3 month");
		assertEquals(result,"Task 1 recurring has been set to 3 month");
	}
	
	@Test
	public void testAddRecurrenceEventYear() {
		testTankTask.executeCommand("add Celebrate Anniversary with beloved girlfriend from 16-11-15 12.00 to 22.00");
		result = testTankTask.executeCommand("1 every year");
		assertEquals(result,"Task 1 recurring has been set to 1 year");
	}
	
	@Test
	public void testAddRecurrenceEventEveryFiveYear() {
		testTankTask.executeCommand("add Celebrate Anniversary with beloved girlfriend from 16/11 12pm to 10pm");
		result = testTankTask.executeCommand("1 every 5 years");
		assertEquals(result,"Task 1 recurring has been set to 5 year");
	}
	
	@Test
	public void testAddRecurrenceEventEveryFiveYearWithExtraCharacters() {
		testTankTask.executeCommand("add Celebrate Anniversary with beloved girlfriend from 16/11 12pm to 10pm");
		result = testTankTask.executeCommand("1 every 5 years aaaaaa");
		assertEquals(result,"Task 1 recurring has been set to 5 year");
	}
	
	@Test
	public void testAddRecurrenceFloatError() {
		result = testTankTask.executeCommand("add something else every week");
		assertEquals(result,"NoDateForRecurrenceError: Cannot make floating task recur. Please set a deadline or start date for the task");
	}
	
	@Test
	public void testAddUndo() {
		testTankTask.executeCommand("add find beloved girlfriend");
		result = testTankTask.executeCommand("undo");
		assertEquals(result, "Undo successfully.");
	}
	
	@Test
	public void testAddRedo() {
		testTankTask.executeCommand("add find beloved girlfriend");
		testTankTask.executeCommand("undo");
		result = testTankTask.executeCommand("redo");
		assertEquals(result, "Redo successfully.");
	}
	
	// ================================================================
    // Edit Operations Test
    // ================================================================
	
	@Test
	public void testEditPriority() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		result = testTankTask.executeCommand("1 priority 5");
		assertEquals(result, "Task 1 has been set to priority 5");
	}
	
	@Test
	public void testSetPriorityInvalidLargerThanRange() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		result = testTankTask.executeCommand("1 priority 10");
		assertEquals(result, "InvalidPriorityError: '10' is not between 1 to 5");
	}
	
	@Test
	public void testSetPriorityInvalidSmallerThanRange() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		result = testTankTask.executeCommand("1 priority 0");
		assertEquals(result, "InvalidPriorityError: '0' is not between 1 to 5");
	}
	
	@Test 
	public void testSetPriorityMissingPriorityValue() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		String result = testTankTask.executeCommand("1 priority");
		assertEquals(result, "EmptyFieldError: Please enter content for the command 'priority'");
	}
	
	@Test
	public void testConvertFromFloatToToDo() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 deadline 30 oct");
		assertEquals(result, "Task 1 deadline has been set to Sun, 30 Oct 16, 11:59pm");
	}
	
	@Test
	public void testConvertFromFloatToEvents() {
		testTankTask.executeCommand("add go on a date beloved girlfriend");
		result = testTankTask.executeCommand("1 event 24 dec 12pm to 25 dec");
		assertEquals(result, "Event 1 has been setted to Thu, 24 Dec 15, 12pm till Fri, 25 Dec 15, 9pm");
	}
	
	@Test
	public void testSetDescription() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 description \"Get flowers from shop A\"");
		assertEquals(result, "Description for task 1 has been set");
	}
	
	@Test
	public void testSetDescriptionDuplicates() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 description Hello this is a test description");
		assertEquals(result, "Description for task 1 has been set");
	}
	
	@Test
	public void testSetReminder() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 reminder 23 dec 8am");
		assertEquals(result, "Reminder for Task 1 has been set to be at Wed, 23 Dec 15, 8am");
	}
	
	@Test
	public void testSetReminderInvalidTime() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 reminder 23 dec 40am");
		assertEquals(result, "InvalidTimeError: '40am' is not an acceptable time format");
	}
	
	@Test
	public void testSetReminderInvalidDate() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 reminder 3tevsv");
		assertEquals(result, "InvalidDateError: '3tevsv' is not an acceptable date format");
	}
	
	@Test
	public void testSetReminderInvalidDate2() {
		testTankTask.executeCommand("add get flowers beloved girlfriend");
		result = testTankTask.executeCommand("1 reminder 32 dec 10am");
		assertEquals(result, "InvalidDayOfMonthError: The date '32 dec' does not exist "
				+ "(December only has 31 days!)");
	}
	
	@Test
	public void testDelete() {
		testTankTask.executeCommand("add Impromptu Meeting with Boss");
		result = testTankTask.executeCommand("delete 1");
		assertEquals(result, "Task 1 has been deleted");
	}
	
	@Test
	public void testDeleteWithExtraThingsIgnored() {
		testTankTask.executeCommand("add Impromptu Meeting with Boss");
		result = testTankTask.executeCommand("delete 1 extra things here");
		assertEquals(result, "Task 1 has been deleted");
	}
	
	@Test
	public void testDeleteFail() {
		testTankTask.executeCommand("add Impromptu Meeting with Boss");
		result = testTankTask.executeCommand("delete ");
		assertEquals(result, "EmptyFieldError: Please enter content for the command 'delete'");
	}
	
	@Test 
	public void testDeleteAll() {
		testTankTask.executeCommand("add Float Task A");
		testTankTask.executeCommand("add Task B deadline 30 dec");
		testTankTask.executeCommand("add Soemthing happening C event 30 dec 10am to 30 dec 12pm");
		String result = testTankTask.executeCommand("deleteAll");
		assertEquals(result,"Everything has been deleted");
	}
	
	@Test
	public void testRename() {
		testTankTask.executeCommand("add I love my girlfriend");
		result = testTankTask.executeCommand("1 rename I love my CUTE girlfriend! Haha :D");
		assertEquals(result,"Task 1 has been renamed to I love my CUTE girlfriend! Haha :D");
	}
	
	@Test 
	public void testEditUndo() {
		testTankTask.executeCommand("add I love my cute girlfriend pri 5");
		testTankTask.executeCommand("1 pri 1");
		result = testTankTask.executeCommand("undo");
		assertEquals(result, "Undo successfully.");
	}
	
	@Test 
	public void testEditRedoSuccessful() {
		testTankTask.executeCommand("add I love my cute girlfriend pri 1");
		testTankTask.executeCommand("1 pri 5");
		testTankTask.executeCommand("undo");
		result = testTankTask.executeCommand("redo");
		assertEquals(result, "Redo successfully.");
	}
	
	@Test 
	public void testEditRedoFail() {
		testTankTask.executeCommand("add I love my cute girlfriend pri 1");
		testTankTask.executeCommand("1 pri 5");
		result = testTankTask.executeCommand("redo");
		assertEquals(result, "Invalid Command. Please try again.");
	}
	
	@Test
	public void testSetMultipleFieldsForTask() {
		testTankTask.executeCommand("add Backup Terminal ");
		result = testTankTask.executeCommand("1 priority 5 description Finish "
				+ "backing up all files reminder 19 dec every day deadline 22 dec "
				+ "rename Backup Terminal and Mobile Interface");
		assertEquals(result, "Fields have been updated");
	}
	
	@Test
	public void testSetMultipleFieldsForTaskWithErrors() {
		testTankTask.executeCommand("add Complete Project D Proposal");
		result = testTankTask.executeCommand("1 priority description Finish "
				+ "Subsections A to D reminder 19 dec deadline 22 dec "
				+ "rename Complete Project F Proposal");
		assertEquals(result, "EmptyFieldError: Please enter content for the command 'priority'");
	}
	
	@Test 
	public void testSetMultipleFieldsForEvents() {
		testTankTask.executeCommand("add Go on date with girlfriend");
		result = testTankTask.executeCommand("1 rename Go on a date with beloved girlfriend "
				+ "des Take her for ice skating priority 5 "
				+ "from sunday 2pm to 10pm reminder sunday 8am every month");
		assertEquals(result, "Fields have been updated");
	}
	
	@Test 
	public void testSetMultipleFieldsForEventsWithErrors() {
		testTankTask.executeCommand("add Go on date with girlfriend");
		result = testTankTask.executeCommand("1 rename Go on a date with beloved girlfriend "
				+ " des \"Take her to ice skating\" priority  "
				+ "from sunday 2pm to 10pm reminder sunday 8am");
		assertEquals(result, "EmptyFieldError: Please enter content for the command 'priority'");
	}
	
	@Test
	public void testSetMultipleFieldsForFloats() {
		testTankTask.executeCommand("add Think of what to get for beloved girlfriend's present");
		result = testTankTask.executeCommand("1 pri 5 description Seriously how to make her happier than what she already is? "
				+ "rename Think of how to make cute beloved girlfriend happy reminder sunday 12pm");
		assertEquals(result, "Fields have been updated");
	}
	
	@Test 
	public void testResetPriority() {
		testTankTask.executeCommand("add testReset pri 1");
		result = testTankTask.executeCommand("1 reset priority");
		assertEquals(result, "Field priority has been reset");
	}
	
	@Test 
	public void testResetDescription() {
		testTankTask.executeCommand("add testDescription des \"testing the reset\"");
		result = testTankTask.executeCommand("1 reset description");
		assertEquals(result, "Field description has been reset");
	}
	
	@Test
	public void testResetReminder() {
		testTankTask.executeCommand("add testReminder reminder tmr 9am");
		result = testTankTask.executeCommand("1 reset reminder");
		assertEquals(result, "Field reminder has been reset");
	}
	
	@Test
	public void testResetDeadline() {
		testTankTask.executeCommand("add testDeadline by tomorrow");
		result = testTankTask.executeCommand("1 reset date");
		assertEquals(result, "Field date has been reset");
	}
	
	@Test
	public void testResetEvent() {
		testTankTask.executeCommand("add testEvent from today to tomorrow");
		result = testTankTask.executeCommand("1 reset date");
		assertEquals(result, "Field date has been reset");
	}
	
	@Test
	public void testResetAll() {
		testTankTask.executeCommand("add testEvent from sat 5pm to 7pm"
				+ "des testing method pri 3 reminder tomorrow");
		result = testTankTask.executeCommand("1 reset all");
		assertEquals(result, "Field all has been reset");
	}
	
	@Test
	public void testResetWrongField() {
		testTankTask.executeCommand("add testEvent from sat 5pm to 7pm"
				+ "des testing method pri 3 reminder tomorrow");
		result = testTankTask.executeCommand("1 reset anyhow");
		assertEquals(result, "InvalidResetError: 'anyhow' is not a field that can be reset");
	}
	
	@Test
	public void testDoneFloat() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		result = testTankTask.executeCommand("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneFloatAnotherWay() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		result = testTankTask.executeCommand("1 done");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneEvent() {
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 DEC to 25 dec");
		result = testTankTask.executeCommand("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneEventAnotherWay() {
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 DEC");
		result = testTankTask.executeCommand("1 done");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneToDo() {
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		result = testTankTask.executeCommand("1 done");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testDoneToDoAnotherWay() {
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		result = testTankTask.executeCommand("done 1");
		assertEquals(result, "Task 1 is completed");
	}
	
	@Test
	public void testUndoneFloat() {
		testTankTask.executeCommand("add Plan a date with beloved girlfriend");
		testTankTask.executeCommand("done 1");
		result = testTankTask.executeCommand("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test
	public void testUndoneEvent() {
		testTankTask.executeCommand("add Meeting with Supervisor B event 22 dec 10am to 22 dec 10am");
		testTankTask.executeCommand("done 1");
		result = testTankTask.executeCommand("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test
	public void testUndoneToDo() {
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		testTankTask.executeCommand("done 1");
		result = testTankTask.executeCommand("undone 1");
		assertEquals(result, "Task 1 is not completed");
	}
	
	@Test
	public void testEmptyRedoStack() {
		result = testTankTask.executeCommand("redo");
		assertEquals(result,"Invalid Command. Please try again.");
	}
	
	// ================================================================
    // File Path Operations Test
    // ================================================================
	
	@Test 
	public void testFilePathNotFound() {
		String result = testTankTask.executeCommand("filepath C:\"Users\"Somewhere\"Not\"Found");
		assertEquals(result, "Invalid file path. Please try again");
	}
	
	// ================================================================
    // Search Operations Test
    // ================================================================
	
	@Test
	public void testSearchFoundByName(){
		testTankTask.executeCommand("add find beloved girlfriend");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testTankTask.executeCommand("search beloved girlfriend");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchFoundByDate(){
		testTankTask.executeCommand("add find beloved girlfriend");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testTankTask.executeCommand("search 24 dec");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchFoundByDescription(){
		testTankTask.executeCommand("add find beloved girlfriend des I love her lots");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec  des I love her lots");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec  des I love her lots");
		result = testTankTask.executeCommand("search I love her lots");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchFoundByJumbledDescription(){
		testTankTask.executeCommand("add find beloved girlfriend des I love her lots");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec  des I love her lots");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec  des I love her lots");
		result = testTankTask.executeCommand("search Her love I");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchFoundWithOtherCommandsIgnored(){
		testTankTask.executeCommand("add find beloved girlfriend");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testTankTask.executeCommand("search beloved girlfriend add delete priority");
		assertEquals(result,"Input not found");
	}
	
	@Test
	public void testUpdatingOfSearchResults(){
		testTankTask.executeCommand("add find beloved girlfriend");
		testTankTask.executeCommand("add Get something for beloved girlfriend deadline 24 dec");
		testTankTask.executeCommand("add Go on a date with beloved girlfriend event 24 dec to 25 dec");
		result = testTankTask.executeCommand("search beloved girlfriend");
		testTankTask.executeCommand("1 pri 5");
		assertEquals(result,"search");
	}
	
	@Test
	public void testSearchNotFound() {
		result = testTankTask.executeCommand("search picnic with beloved girlfriend");
		assertEquals(result,"Input not found");
	}
	
	// ================================================================
    // Sort Operations Test
    // ================================================================
	
	@Test
	public void testSortPriorityLongForm() {
		testTankTask.executeCommand("add A pri 1");
		testTankTask.equals("add B pri 3");
		testTankTask.equals("add C pri 5");
		result = testTankTask.executeCommand("sort Priority");
		assertEquals(result, "All items sorted");
	}
	
	@Test
	public void testSortPriorityShortForm1() {
		testTankTask.executeCommand("add A pri 1");
		testTankTask.equals("add B pri 3");
		testTankTask.equals("add C pri 5");
		result = testTankTask.executeCommand("sort Pri");
		assertEquals(result, "All items sorted");
	}
	
	@Test
	public void testSortPriorityShortForm2() {
		testTankTask.executeCommand("add A pri 1");
		testTankTask.equals("add B pri 3");
		testTankTask.equals("add C pri 5");
		result = testTankTask.executeCommand("sortP");
		assertEquals(result, "All items sorted");
	}
	
	@Test
	public void testSortPriorityEvenLongerForm() {
		testTankTask.executeCommand("add A pri 1");
		testTankTask.equals("add B pri 3");
		testTankTask.equals("add C pri 5");
		result = testTankTask.executeCommand("sort by Priority");
		assertEquals(result, "All items sorted");
	}
	
	@Test 
	public void testSortNameLongForm() {
		testTankTask.executeCommand("add C pri 5");
		testTankTask.executeCommand("add B pri 3");
		testTankTask.executeCommand("add A pri 1");
		result = testTankTask.executeCommand("sort Name");
		assertEquals(result, "All items sorted");
	}
	
	@Test
	public void testSortNameEvenLongerForm() {
		testTankTask.executeCommand("add A pri 1");
		testTankTask.equals("add B pri 3");
		testTankTask.equals("add C pri 5");
		result = testTankTask.executeCommand("sort by Name");
		assertEquals(result, "All items sorted");
	}
	
	@Test 
	public void testSortNameShortForm() {
		testTankTask.executeCommand("add C pri 5");
		testTankTask.executeCommand("add B pri 3");
		testTankTask.executeCommand("add A pri 1");
		result = testTankTask.executeCommand("sortN");
		assertEquals(result, "All items sorted");
	}
	
	@Test
	public void testSortDeadlineLongForm() {
		testTankTask.executeCommand("add C pri 5 deadline today");
		testTankTask.executeCommand("add B pri 3 deadline tomorrow");
		testTankTask.executeCommand("add A pri 1 deadline next monday");
		result = testTankTask.executeCommand("sort Deadline");
		assertEquals(result,"All items sorted");
	}
	
	@Test
	public void testSortDeadlineShortForm() {
		testTankTask.executeCommand("add C pri 5 deadline today");
		testTankTask.executeCommand("add B pri 3 deadline tomorrow");
		testTankTask.executeCommand("add A pri 1 deadline next monday");
		result = testTankTask.executeCommand("sortD");
		assertEquals(result,"All items sorted");
	}
	
	@Test
	public void testSortDeadlineEvenLonger() {
		testTankTask.executeCommand("add C pri 5 deadline today");
		testTankTask.executeCommand("add B pri 3 deadline tomorrow");
		testTankTask.executeCommand("add A pri 1 deadline next monday");
		result = testTankTask.executeCommand("sort by Deadline");
		assertEquals(result,"All items sorted");
	}
	
	@Test
	public void testSortWrongField() {
		testTankTask.executeCommand("add C pri 5 deadline today");
		testTankTask.executeCommand("add B pri 3 deadline tomorrow");
		testTankTask.executeCommand("add A pri 1 deadline next monday");
		result = testTankTask.executeCommand("sort anyhow");
		assertEquals(result,"InvalidSortFieldError: 'anyhow' is not a valid sort field "
				+ "(please enter 'date', 'name' or 'priority')");
	}
	
	// ================================================================
    // Observer Operations Test
    // ================================================================
	
	@Test 
	public void testShowFloatLong() {
		result = testTankTask.executeCommand("show Floating");
		assertEquals(result, "showF");
	}
	
	@Test 
	public void testShowFloatShort() {
		result = testTankTask.executeCommand("showF");
		assertEquals(result, "showF");
	}
	
	@Test
	public void testShowOverdueLong() {
		result = testTankTask.executeCommand("show overdue");
		assertEquals(result, "showO");
	}
	
	@Test
	public void testShowOverdueShort() {
		result = testTankTask.executeCommand("showO");
		assertEquals(result, "showO");
	}
	
	@Test
	public void testShowEventsLong() {
		result = testTankTask.executeCommand("show events");
		assertEquals(result, "showE");
	}
	
	@Test
	public void testShowEventsShort() {
		result = testTankTask.executeCommand("showE");
		assertEquals(result, "showE");
	}
	
	@Test
	public void testShowTaskWrongType() {
		result = testTankTask.executeCommand("show test");
		assertEquals(result, "InvalidTaskTypeError: 'test' is not a valid task type "
				+ "(please enter 'todo', 'event', 'floating', 'today', 'overdue' or 'complete')");
	}

}
