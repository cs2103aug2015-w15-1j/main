package logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Logic.LogicController;
import main.java.backend.Logic.LogicCreator;
import main.java.backend.Storage.Task.Task;

public class LogicCreatorTest {

	private static final String TEST_FILE_NAME = "test.txt";
	private static final String TYPE_EVENT = "event";
	
	private static final String DESCRIPTION_TODO = "Find out how to code query in PHP";
	private static final String DESCRIPTION_EVENT = "Meet at East Coast Lagoon Food Village at 1pm";
	
	private static final String DATE_TODO_START = "Mon, 12 Oct 8:00am";
	private static final String DATE_FLOAT_START = "Tue, 3 Nov 12:00pm";
	private static final String DATE_TODO_END = "Sat, 3 Oct 8:00am";
	private static final String DATE_FLOAT_END = "Tue, 20 Oct 8:00am";
	
	private static final long DATE_TODO_STARTTIME = 
			GeneralFunctions.stringToMillisecond("Mon, 12 Oct 8:00am");
	private static final long DATE_FLOAT_STARTTIME = 
			GeneralFunctions.stringToMillisecond("Tue, 3 Nov 12:00pm");
	private static final long DATE_TODO_ENDTIME = 
			GeneralFunctions.stringToMillisecond("Sat, 3 Oct 8:00am");
	private static final long DATE_FLOAT_ENDTIME = 
			GeneralFunctions.stringToMillisecond("Tue, 20 Oct 8:00am");
	
	private static final boolean DONE = true;
	private static final boolean UNDONE = false;
	
	private static final int PRIORITY_3 = 3;
	private static final int PRIORITY_5 = 5;
	private static final int SIZE_0 = 0;
	private static final int SIZE_1 = 1;
	private static final int SIZE_2 = 2;

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
	
	LogicController logicController;
	LogicCreator logicCreator;
	
	@Test
	public void init() {
		logicController = LogicController.getInstance(TEST_FILE_NAME);
	}

}
