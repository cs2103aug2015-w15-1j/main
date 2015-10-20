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
	
	/*private String getCurrentTime() {
	SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mma");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}
	private String getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("zzz yyyy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}*/
	
	private String getTodayDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMM");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}
	
	private String getTmrDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMM");
	    Date now = new Date();
	    long milliNow = now.getTime();
	    long milliTmr = milliNow += (1000 * 60 * 60 * 24);
	    String strDate = sdfDate.format(milliTmr);
		return strDate;
	}
	
	@Test
	public void BasicTests() {
		System.out.println("\n-----------------Result for BasicTests-----------------");
		
	    input = "exit";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("exit") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    input = "redo";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("redo") );
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
	    
	    input = "1 deadline 30 December 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority 5";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("priority", "1", "5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 description i need to start doing this!";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "i need to start doing this!") );
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
	   
	    input = "1 by 12 Feb 3pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Fri, 12 Feb 16, 3pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "3 from 4 Apr 4pm to 5 May 5:30pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "3", "Mon, 04 Apr 16, 4pm", "Thu, 05 May 16, 5:30pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "4 rename new task name";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("rename", "4", "new task name") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "showF";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showF") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "showO";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showO") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "show floating";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showF") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "show events";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showE") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "show o";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showO") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sortp";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sort deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortD") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sort pri";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sort name";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortN") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sort by priority";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DominatingCommands() {
		System.out.println("\n-----------------Result for DominatingCommands-----------------");
		
	    input = "exit extra extra extra words";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("exit") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "done 1";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "1") );
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
	    
	    input = "add addcat delete search reset sortp exit showf";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "addcat delete search reset sortp exit showf", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "delete 1 aaaaaaaaaa";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("delete", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reset all";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "all") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reset deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "deadline") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reset pri";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "priority") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void OneShotCommand() {
		System.out.println("\n-----------------Result for OneShotCommand-----------------");
	    
	    input = "add Project Proposal deadline 30 December 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "Wed, 30 Dec 15, 11:59pm", "", "", "") );
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
	    
	    input = "add Project Proposal deadline 30 December 23:59 priority 5 description i need to start doing this!";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "i need to start doing this!",  
	    								  "Wed, 30 Dec 15, 11:59pm", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);   
	    
	    input = "add User Guide reminder 20 December 12:00 description i will do this in 10 days priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "User Guide", "i will do this in 10 days", "3", 
	    												"Sun, 20 Dec 15, 12pm", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);  
	    
	    input = "1 description i must finish this early reminder 25 Dec 21:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "i must finish this early", "", 
	    												"Fri, 25 Dec 15, 9pm", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 priority 4 deadline 5 October 20:00 category research ";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "2", "", "Wed, 05 Oct 16, 8pm",
	    												"4", "", "research", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "3 deadline 19 Nov 3pm category important";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "3", "", "Thu, 19 Nov 15, 3pm", 
													"", "", "important", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 des hello cat world dea 11 Jan 2pm rem 5 Jan 2pm pri 2";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "1", "hello", "Mon, 11 Jan 16, 2pm", "2", "Tue, 05 Jan 16, 2pm", "world", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 des hello rename bla bla pri 2";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello", "2", "", "", "bla bla") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 des hello every bla bla pri 2";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello every bla bla", "2", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DuplicateCommands() {
		System.out.println("\n-----------------Result for DuplicateCommands-----------------");
	    
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
	    
	    input = "1 description hello this is a description";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "hello this is a description") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 description hello this is a description priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello this is a description", "3", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 cat hello des this is a cat";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "this is a cat", "", "", "hello", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DateAndTime(){
		System.out.println("\n-----------------Result for DateAndTime-----------------");
		
		//String timeNow = getCurrentTime();
		//String dateNow = getCurrentDate();
		//String yearNow = getCurrentYear();
		//String dateTmr = getTmrDate();
		
		input = "1 deadline 30/12/15 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30-12-15 23.59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30/12 11:59pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30-12 11pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30/12 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30 Dec 23:59";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 12/3/2016 04:56:22";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Sat, 12 Mar 16, 4:56am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30/12";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30 Dec";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline December 30";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    //The result of these tests depend on whether the current time is before or after the stated deadline
		/*input = "1 deadline 2:30pm 2015";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", dateTmr + " 14:30:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		input = "1 deadline 20:00";
		parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", dateNow + " 20:00:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);*/
		
	    input = "2 event 15/09 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 event 15-09 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    input = "3 event 15/09 10:00 to 17/09 09:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "3", "Thu, 15 Sep 16, 10am", 
	    								  "Sat, 17 Sep 16, 9:59am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "4 event 15/09 10am to 2pm";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", 
		"Thu, 15 Sep 16, 2pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 description i must reach there early event 30/12 11am to 3pm reminder 29/12/15 12:00";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setE", "2", "i must reach there early", 
	    "Wed, 30 Dec 15, 11am", "Wed, 30 Dec 15, 3pm", "", "Tue, 29 Dec 15, 12pm", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event 12/12/15 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    "Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 event 15 Sep 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event Dec 12 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    "Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event 12 Dec 2015 12:00 to 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    										"Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event Dec 12 12:00 to 12 Dec 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    										"Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add longest meeting ever category meetings event Dec 12 2015 12:00 to Dec 12 2016 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Sat, 12 Dec 15, 12pm", 
	    										"Mon, 12 Dec 16, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add longest meeting ever category meetings event 12/12/15 12:00 to 12/12/16 18:00 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Sat, 12 Dec 15, 12pm", 
	    										"Mon, 12 Dec 16, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 by 31 Dec 10am 1995";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "2", "Thu, 31 Dec 15, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 event 31 Dec 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 31 Dec 15, 10am", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 event 2 Jan 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Sat, 02 Jan 16, 10am", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "2 event 2 Jan";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Sat, 02 Jan 16, 12pm", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "3 by today";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "3", getTodayDate() + " 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "3 by tmr";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "3", getTmrDate() + " 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every day 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every week Tuesday 10am";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Tue 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every week Wed 10.30";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Wed 10:30am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every week Tue";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Tue 12pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month 15";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "15 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month 1";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "1 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month 15th";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "15 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month 1th";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "1 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every year April 1";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "01 Apr") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void ErrorTests(){
		System.out.println("\n-----------------Result for ErrorTests-----------------");
		
	    input = "delete Project Proposal";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "IndexError: 'Project Proposal' is not recognised as an index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "UnrecognisedFirstWordError: 'Project' is not recognised as a command or index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Proposal done bla bla bla bla";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "UnrecognisedFirstWordError: 'Project' is not recognised as a command or index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "deadline deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "1 deadline 30 October 12:34 deadline 30 December 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	
		input = "1 priority 1 priority 5 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add Project Proposal deadline coming soon priority 2 deadline 30 Nov 23:59";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "deadline";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'add'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);

	    input = "done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'done'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority 3 deadline description hello";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add dea des pri cat";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'add'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "show";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'show'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority 6";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: '6' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority bla";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: 'bla' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 deadline 30 Dec 23:59 priority bla";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: 'bla' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 deadline qwertyuiop";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 priority 2 deadline qwertyuiop";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reminder qwertyuiop";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reminder qwertyuiop priority 2";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 event qwertyuiop";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 event asdfghjkl to 30 Dec 2pm 2015";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'asdfghjkl' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 event 30 Dec 2pm 2015 to asdfghjkl";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'asdfghjkl' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every 2pm 2015";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidFrequencyError: '2pm 2015' is not 'day', 'week', 'month' or 'year'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every week zzzzz";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'zzzzz' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month aaaaa";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDayOfMonthError: 'aaaaa' is not between 1 to 31") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "5 every month 32";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDayOfMonthError: '32' is not between 1 to 31") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "show aaaaa";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidTaskTypeError: 'aaaaa' is not 'todo', 'event' or 'floating'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "sort aaaaa";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidSortFieldError: 'aaaaa' is not 'deadline', 'name' or 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reset";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'reset'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "1 reset aaaaaaaaaa";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidResetError: 'aaaaaaaaaa' is not a field that can be resetted") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	}
}
