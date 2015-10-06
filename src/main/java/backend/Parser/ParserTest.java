package main.java.backend.Parser;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

public class ParserTest {
	public Parser parser = new Parser();
	String input;
	public ArrayList<String> parsed; 
	public ArrayList<String> expected;
	
	private String getCurrentTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}

	/*private String getCurrentDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEE MMM dd");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}
	
	private String getTmrDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEE MMM dd");
	    Date now = new Date();
	    long milliNow = now.getTime();
	    long milliTmr = milliNow += (1000 * 60 * 60 * 24);
	    String strDate = sdfDate.format(milliTmr);
		return strDate;
	}
	
	private String getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("zzz yyyy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}*/
	
	@Test
	public void BasicTests() {
		System.out.println("\n-----------------Result for BasicTests-----------------");
		
	    input = "return";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("return") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
		input = "add Project Proposal";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal deadline 30 October 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal priority 5";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("priority", "Project Proposal", "5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal description i need to start doing this!";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "Project Proposal", "i need to start doing this!") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "ADD PROJECT PROPOSAL";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "PROJECT PROPOSAL", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "addcat Work Stuffs";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addcat", "Work Stuffs") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "setcol Work Stuffs red";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setcol", "Work Stuffs", "red") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "setcol Work Stuffs red blue";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setcol", "Work Stuffs red", "blue") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DominatingCommands() {
		System.out.println("\n-----------------Result for DominatingCommands-----------------");
		
	    input = "return extra extra extra words";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("return") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "done Project Proposal";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "Project Proposal") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "delete Oral Presentation priority 100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("delete", "Oral Presentation priority 100") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "Project Proposal") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal done bla bla bla bla";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("", "Project Proposal done bla bla bla bla") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal done priority 1";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("priority", "Project Proposal done", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Add Project Proposal done delete";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal done delete", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "delete Project Proposal done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("delete", "Project Proposal done") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "addcat Work Stuffs priority 100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addcat", "Work Stuffs priority 100") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "addcat Work Stuffs done priority 100 description bla bla";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addcat", "Work Stuffs done priority 100"
	    												+ " description bla bla") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add Project Proposal addcat priority 5";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal addcat", "", "5",
	    												"", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "search Project Proposal priority 100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("search", "Project Proposal priority 100") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add Project Proposal search";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal search", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void OneShotCommand() {
		System.out.println("\n-----------------Result for OneShotCommand-----------------");
	    
	    input = "add Project Proposal deadline 30 October 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "Fri Oct 30 23:59:00 SGT 2015", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add Project Proposal priority 5";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal", "", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add Project Proposal deadline 30 October 23:59 priority 5 description i need to start doing this!";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "i need to start doing this!",  
	    								  "Fri Oct 30 23:59:00 SGT 2015", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);   
	    
	    input = "add User Guide reminder 20 October 12:00 description i will do this in 10 days priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "User Guide", "i will do this in 10 days", "3", 
	    												"Tue Oct 20 12:00:00 SGT 2015", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);  
	    
	    input = "CS2103 Assignment description i must finish this early reminder 19 October 21:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "CS2103 Assignment", "i must finish this early", "", 
	    												"Mon Oct 19 21:00:00 SGT 2015", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "CS2101 Project Research priority 4 deadline 5 October 20:00 category research ";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "CS2101 Project Research", "", "Mon Oct 05 20:00:00 SGT 2015",
	    												"4", "", "research") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30 October 23:59 description must be done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "Project Proposal", "must be done", "Fri Oct 30 23:59:00 SGT 2015", 
	    												"", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DuplicateCommands() {
		System.out.println("\n-----------------Result for DuplicateCommands-----------------");
		
	    input = "add";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add add";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "add", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add add food to the fridge priority 2";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "add food to the fridge", "", "2", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    input = "deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "deadline deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "deadline", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "deadline deadline deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "deadline deadline", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "deadline deadline deadline Oct 10 10:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "deadline deadline", "Sat Oct 10 10:00:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline tomorrow deadline 30 October 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal deadline tomorrow", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	
		input = "Project Proposal priority 1 priority 5 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("priority", "Project Proposal priority 1 priority 5", "3") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add Project Proposal deadline coming soon deadline 30 Nov 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal deadline coming soon", "", "Mon Nov 30 23:59:00 SGT 2015", 
														"", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add deadline is coming! priority 5 deadline 30 Nov 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "deadline is coming!", "", "Mon Nov 30 23:59:00 SGT 2015", 
														"5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add Project Proposal priority 4 deadline tomorrow reminder 29 October 12:00 deadline 30 October 12:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "Fri Oct 30 12:00:00 SGT 2015", 
														"4 deadline tomorrow", "Thu Oct 29 12:00:00 SGT 2015", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal description deadline is coming soon deadline 30 Nov 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "Project Proposal", "deadline is coming soon", "Mon Nov 30 23:59:00 SGT 2015", 
														"", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DateAndTime(){
		System.out.println("\n-----------------Result for DateAndTime-----------------");
		
		String timeNow = getCurrentTime();
		//String dateNow = getCurrentDate();
		//String yearNow = getCurrentYear();
		//String dateTmr = getTmrDate();
		
		input = "Project Proposal deadline 30/10/15 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30-10-15 23.59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30/10 11:59pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30-10 11pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:00:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30/10 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30 Oct 23:59";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 23:59:00 SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 12/3/2016 04:56:22";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Sat Mar 12 04:56:22 SGT 2016") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30/10";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 " + timeNow + " SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30 Oct";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 " + timeNow + " SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline October 30";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "Fri Oct 30 " + timeNow + " SGT 2015") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    //The result of these tests depend on whether the current time is before or after the stated deadline
		/*input = "Project Proposal deadline 2:30pm";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", dateTmr + " 14:30:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		input = "Project Proposal deadline 20:00";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", dateNow + " 20:00:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);*/
		
	    input = "OP1 event 15/09 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "Thu Sep 15 10:00:00 SGT 2016", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP1 event 15-09 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "Thu Sep 15 10:00:00 SGT 2016", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    input = "School camp event 15/09 10:00 to 17/09 09:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "School camp", "Thu Sep 15 10:00:00 SGT 2016", 
	    								  "Sat Sep 17 09:59:00 SGT 2016") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP2 event 15/09 10am to 2pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP2", "Thu Sep 15 10:00:00 SGT 2016", 
			    										"Thu Sep 15 14:00:00 SGT 2016") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP1 description i must reach there early event 30/10 11am to 3pm reminder 29/10/15 12:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setE", "OP1", "i must reach there early", 
	    					"Fri Oct 30 11:00:00 SGT 2015", "Fri Oct 30 15:00:00 SGT 2015", "", "Thu Oct 29 12:00:00 SGT 2015", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event 12/10/15 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Mon Oct 12 18:00:00 SGT 2015", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP1 event 15 Sep 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "Thu Sep 15 10:00:00 SGT 2016", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event Oct 12 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Mon Oct 12 18:00:00 SGT 2015", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event 12 Oct 2015 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Mon Oct 12 18:00:00 SGT 2015", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event Oct 12 12:00 to 12 Oct 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Mon Oct 12 18:00:00 SGT 2015", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add longest meeting ever category meetings event Oct 12 2015 12:00 to Oct 12 2016 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Wed Oct 12 18:00:00 SGT 2016", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add longest meeting ever category meetings event 12/10/15 12:00 to 12/10/16 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Mon Oct 12 12:00:00 SGT 2015", 
	    										"Wed Oct 12 18:00:00 SGT 2016", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP1 event 31 Dec 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "Thu Dec 31 10:00:00 SGT 2015", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP1 event 2 Jan 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "Sat Jan 02 10:00:00 SGT 2016", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
}
