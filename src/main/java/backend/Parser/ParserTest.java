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
	public ArrayList<String> actual; 
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
	/**
	 * Test whether basic commands with valid inputs work
	 */
	public void BasicTests() {
		System.out.println("\n-----------------Result for BasicTests-----------------");
		
	    input = "exit";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("exit") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		
	    input = "redo";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("redo") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "deleteall";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deleteAll") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
		input = "add Project Proposal";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "1 deadline 30 December 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "1 priority 5";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("priority", "1", "5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "1 description i need to start doing this!";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "i need to start doing this!") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "ADD PROJECT PROPOSAL";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "PROJECT PROPOSAL", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "addcat Work Stuffs";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addcat", "Work Stuffs") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	   
	    input = "1 by 12 Feb 3pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Fri, 12 Feb 16, 3pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "1 BY 12 Feb 3pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Fri, 12 Feb 16, 3pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "3 from 4 Apr 4pm to 5 May 5:30pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "3", "Mon, 04 Apr 16, 4pm", "Thu, 05 May 16, 5:30pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "4 rename new task name";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("rename", "4", "new task name") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "showF";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showF") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "showO";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showO") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "show floating";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showF") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "show events";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showE") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "show o";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("showO") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "sortp";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "sort deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortD") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "sort pri";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "sort name";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortN") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "sort by priority";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("sortP") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	}
	
	@Test
	/**
	 * Test whether dominating commands work properly
	 */
	public void DominatingCommands() {
		System.out.println("\n-----------------Result for DominatingCommands-----------------");
		
		//should ignore the extra words
	    input = "exit extra extra extra words";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("exit") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //either format for done/delete should work
	    input = "done 1";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 done";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("done", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //dominating commands should be ignored when 'add' is in effect
	    input = "Add Project Proposal done delete";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal done delete", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add Project Proposal search";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal search", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //all other commands should be ignored when 'addcat' is in effect
	    input = "addcat Work Stuffs done priority 100 description bla bla";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addcat", "Work Stuffs done priority 100"
	    												+ " description bla bla") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //all other commands should be ignored when 'search' is in effect
	    input = "search Project Proposal priority 100";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("search", "Project Proposal priority 100") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //'addcat' should be ignored when 'add' is in effect
	    input = "add Project Proposal addcat priority 5";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal addcat", "", "5",
	    												"", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //all dominating commands and no-content commands should be ignored when 'add' is in effect
	    input = "add addcat delete search reset sortp exit showf";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "addcat delete search reset sortp exit showf", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //delete/done should ignore extra words
	    input = "delete 1 aaaaaaaaaa";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("delete", "1") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether reset works properly
	    input = "1 reset all";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "all") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 reset deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "deadline") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 reset pri";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("reset", "1", "priority") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //If last word is a commad keyword and 'add' is in effect, the last command keyword is taken as part of task name
	    input = "add bla deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "bla deadline", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des bla bla deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "bla bla deadline") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	}
	
	@Test
	/**
	 * Test whether one-shot commands work properly
	 */
	public void OneShotCommand() {
		System.out.println("\n-----------------Result for OneShotCommand-----------------");
	    
		//Check that you can add floatings with one-shot commands
		input = "add Project Proposal priority 5";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal", "", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add User Guide reminder 20 December 12:00 description i will do this in 10 days priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "User Guide", "i will do this in 10 days", "3", 
	    												"Sun, 20 Dec 15, 12pm", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);  
	    
	    //Check that you can add to-dos with one-shot commands
	    input = "add Project Proposal deadline 30 December 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "Wed, 30 Dec 15, 11:59pm", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add Project Proposal deadline 30 December 23:59 priority 5 description i need to start doing this!";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "i need to start doing this!",  
	    								  "Wed, 30 Dec 15, 11:59pm", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);   
	    
	    //Check that you can add events with one-shot commands
	    input = "add OP2 event 29 Dec 2pm to 3pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "OP2", "", "Tue, 29 Dec 15, 2pm", "Tue, 29 Dec 15, 3pm", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Check that you can set multiple fields for existing tasks 
	    input = "1 description i must finish this early reminder 25 Dec 21:00";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "i must finish this early", "", 
	    												"Fri, 25 Dec 15, 9pm", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "2 priority 4 deadline 5 October 20:00 category research ";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "2", "", "Wed, 05 Oct 16, 8pm",
	    												"4", "", "research", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "3 deadline 19 Nov 3pm category important";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "3", "", "Thu, 19 Nov 15, 3pm", 
													"", "", "important", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Check that command shortforms also work for one-shot commands
	    input = "1 des hello cat world dea 11 Jan 2pm rem 5 Jan 2pm pri 2";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "1", "hello", "Mon, 11 Jan 16, 2pm", "2", "Tue, 05 Jan 16, 2pm", "world", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des hello rename bla bla pri 2";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello", "2", "", "", "bla bla") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Check that 'every' is not recognised in one-shot commands
	    input = "1 des hello every bla bla pri 2";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello every bla bla", "2", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	}
	
	@Test
	/**
	 * Test whether parser can ignore duplicate commands in certain scenarios
	 */
	public void DuplicateCommands() {
		System.out.println("\n-----------------Result for DuplicateCommands-----------------");
	    
		//Test whether duplicate 'add' is considered as part of task name
	    input = "add add";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "add", "", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add add food to the fridge priority 2";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "add food to the fridge", "", "2", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether duplicate 'description' is considered as part of description
	    input = "1 description hello this is a description";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "hello this is a description") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 description hello this is a description priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "hello this is a description", "3", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether other duplicate commands are considered as part of description (when description is in effect)
	    input = "1 cat hello des this is a cat";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "1", "this is a cat", "", "", "hello", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether command keywords inside quotations will be ignored
	    input = "1 des buy cat from pet shop";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'category'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des \"buy cat from pet shop\"";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "buy cat from pet shop") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add \"buy new cat\" des \"buy cat from pet shop\"";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "buy new cat", "buy cat from pet shop", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add \"buy new cat\" pri 3 rem 21 Jul 7pm des \"buy cat from pet shop\"";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "buy new cat", "buy cat from pet shop", "3", "Thu, 21 Jul 16, 7pm", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add \"buy new cat\" rem 21 Jul 7pm des \"buy cat from pet shop\" pri 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "buy new cat", "buy cat from pet shop", "3", "Thu, 21 Jul 16, 7pm", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des \"buy new pet";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "\"buy new pet") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des \"buy\"";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("description", "1", "buy") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	}
	
	@Test
	/**
	 * Test whether date and time are being formatted correctly
	 */
	public void DateAndTime(){
		System.out.println("\n-----------------Result for DateAndTime-----------------");
		
		//String timeNow = getCurrentTime();
		//String dateNow = getCurrentDate();
		//String yearNow = getCurrentYear();
		//String dateTmr = getTmrDate();
		
		//Testing different deadline formats for the same date and time
		input = "1 deadline 30/12/15 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 30-12-15 23.59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 30/12 11:59pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 30/12 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 30 Dec 23:59";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether time without minutes work
		input = "1 deadline 30-12 11pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether a full date and time works
		input = "1 deadline 12/3/2016 04:56:22";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Sat, 12 Mar 16, 4:56am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether deadline without time is given the default time 11:59pm
		input = "1 deadline 30/12";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 30 Dec";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline December 30";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //The result of these tests depend on whether the current time is before or after the stated deadline
		/*input = "1 deadline 2:30pm";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", dateTmr + " 14:30:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 deadline 20:00";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", dateNow + " 20:00:00 " + yearNow) );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);*/
	    
	    //Testing for event with both start and end date/time
	    input = "3 event 15/09 10:00 to 17/09 09:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "3", "Thu, 15 Sep 16, 10am", 
	    "Sat, 17 Sep 16, 9:59am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for event with end date but not time (should set the end time to be same as start time)
	    input = "4 event 15/09 10am to 16/09";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 Sep 10am to 16 Sep";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 September 10am to 16 September";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for when end date is before start date (should set it to one day or one year later)
	    input = "4 event 15 Sep 10am to 14 Sep";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Thu, 14 Sep 17, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 Sep 10:30am to 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10:30am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 Sep 10:30am to 10:10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10:30am", "Fri, 16 Sep 16, 10:10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 Sep 10am to 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "2 event 15 Sep 2pm to 15 Sep 1pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 2pm", "Fri, 15 Sep 17, 1pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for event with end time but not date (should set end date to be the same day or the next day depending on time)
	    input = "4 event 15/09 10am to 2pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Thu, 15 Sep 16, 2pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15/09 10am to 8am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 8am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15/09 2am to 4am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 2am", "Thu, 15 Sep 16, 4am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15/09 12pm to 12pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 12pm", "Fri, 16 Sep 16, 12pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 15 Sep 10:30am to 10:45am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10:30am", "Thu, 15 Sep 16, 10:45am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for event with no end date/time (should set end date to be same as start date, time to be 11:59pm)
	    input = "2 event 15/09 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "Thu, 15 Sep 16, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "2 event 15 Sep 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "Thu, 15 Sep 16, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for event with no start or end time (set both to 12pm)
	    input = "2 event 15 Sep to 16 Sep";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 12pm", "Fri, 16 Sep 16, 12pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing for when time was entered before date
		input = "1 deadline 3pm 30 Dec";
		actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "1", "Wed, 30 Dec 15, 3pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 10am 15/09 to 16/09";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 10am 15 Sep to 14 Sep";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Thu, 14 Sep 17, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 10:30am 15 Sep to 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10:30am", "Fri, 16 Sep 16, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "4 event 10am 15/09 to 8am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "4", "Thu, 15 Sep 16, 10am", "Fri, 16 Sep 16, 8am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    input = "2 event 10am 15/09";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 15 Sep 16, 10am", "Thu, 15 Sep 16, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Testing event in one-shot commands (some with year specified, some without)
	    input = "2 description i must reach there early event 30/12 11am to 3pm reminder 29/12/15 12:00";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setE", "2", "i must reach there early", 
	    "Wed, 30 Dec 15, 11am", "Wed, 30 Dec 15, 3pm", "", "Tue, 29 Dec 15, 12pm", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add 2101 meeting category meetings event 12/12/15 12:00 to 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    "Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);   
	    input = "add 2101 meeting category meetings event Dec 12 12:00 to 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    "Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add 2101 meeting category meetings event 12 Dec 2015 12:00 to 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    										"Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add 2101 meeting category meetings event Dec 12 12:00 to 12 Dec 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "Sat, 12 Dec 15, 12pm", 
	    										"Sat, 12 Dec 15, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add longest meeting ever category meetings event Dec 12 2015 12:00 to Dec 12 2016 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Sat, 12 Dec 15, 12pm", 
	    										"Mon, 12 Dec 16, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add longest meeting ever category meetings event 12/12/15 12:00 to 12/12/16 18:00 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "longest meeting ever", "", "Sat, 12 Dec 15, 12pm", 
	    										"Mon, 12 Dec 16, 6pm", "3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether parser will change an old year to the current year
	    input = "2 by 31 Dec 10am 1995";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "2", "Thu, 31 Dec 15, 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test when year is not specified, whether parser can set the date to always be the nearest one in the future
	    input = "2 event 31 Dec 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Thu, 31 Dec 15, 10am", "Thu, 31 Dec 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "2 event 2 Jan 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Sat, 02 Jan 16, 10am", "Sat, 02 Jan 16, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test when only event start date is specified, whether parser can set start time to default 12pm, and end date/time to default too
	    input = "2 event 2 Jan";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "2", "Sat, 02 Jan 16, 12pm", "Sat, 02 Jan 16, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether shortform for deadline works
	    input = "3 by today";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "3", getTodayDate() + " 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "3 by tmr";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "3", getTmrDate() + " 15, 11:59pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether daily recurring task works
	    input = "5 every day 8am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "8am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every day 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether weekly recurring task works
	    input = "5 every week Tuesday 10am";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Tue 10am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every week Wed 10.30";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Wed 10:30am") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every week Tue";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "Tue 12pm") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether monthly recurring task works
	    input = "5 every month 15";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "15 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every month 1";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "1 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every month 15th";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "15 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every month 1th";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "1 of month") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Test whether yearly recurring task works
	    input = "5 every year April 1";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("every", "5", "01 Apr") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	}
	
	@Test
	/**
	 * Test whether parser can handle invalid commands
	 */
	public void ErrorTests(){
		System.out.println("\n-----------------Result for ErrorTests-----------------");
		
		//Parser should generate error if first word is not a command keyword or index
	    input = "addd Project Proposal";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidWordError: 'addd' is not recognised as a command or index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "Project Proposal done";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidWordError: 'Project' is not recognised as a command or index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		
	    //Parser should generate error if task index is not followed by a command keyword
		input = "1 bla";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidCommandError: 'bla' is not recognised as a command") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "NoCommandError: please enter a command after the task index '1'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error when a command is expecting an index, but the next word(s) is not an index
	    input = "delete Project Proposal";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidIndexError: 'Project Proposal' is not recognised as an index") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	   
	    //Parser should generate error when duplicate commands are detected
		input = "1 deadline 30 October 12:34 deadline 30 December 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "1 priority 1 priority 5 priority 3";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
		input = "add Project Proposal deadline coming soon priority 2 deadline 30 Nov 23:59";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "DuplicateCommandError: duplicate command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error when user never include the content for a command
	    input = "deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "deadline deadline";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'add'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "done";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'done'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 priority";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 priority 3 deadline description hello";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'deadline'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "add dea des pri cat";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'add'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "show";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'show'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 reset";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "EmptyFieldError: please enter content for the command 'reset'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error if priority is invalid (not between 1 to 5)
	    input = "1 priority 6";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: '6' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 priority bla";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: 'bla' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 deadline 30 Dec 23:59 priority 0";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidPriorityError: '0' is not between 1 to 5") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);

	    //Parser should generate error when a date cannot be actual
	    input = "1 deadline qwertyuiop";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 priority 2 deadline qwertyuiop";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 reminder qwertyuiop";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    input = "1 reminder qwertyuiop priority 2";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 event qwertyuiop";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'qwertyuiop' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 event asdfghjkl to 30 Dec 2pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'asdfghjkl' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 event 30 Dec 2pm 2015 to asdfghjkl";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'asdfghjkl' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every week zzzzz";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDateError: 'zzzzz' is not an acceptable date format") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should not allow a task to have both deadline and event date
	    input = "1 deadline 15 Dec 8pm event 30 Dec 2pm to 6pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "ConflictingDatesError: Task cannot have both deadline and event date") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 des conflicting dates deadline 15 Dec 8pm pri 4 event 30 Dec 2pm to 6pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "ConflictingDatesError: Task cannot have both deadline and event date") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    input = "1 event 30 Dec 2pm to";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "NoEndDateError: please enter an end date after the command word 'to'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "1 event 30 Dec 2pm to rem 23 Dec 2pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "NoEndDateError: please enter an end date after the command word 'to'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error when user never indicate the frequency of a recurring task
	    input = "5 every 2pm 2015";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidFrequencyError: please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every time Tue 12pm";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidFrequencyError: please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error when the day of the month in a monthly task is not valid
	    input = "5 every month aaaaa";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDayOfMonthError: 'aaaaa' is not between 1 to 31") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "5 every month 32";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidDayOfMonthError: '32' is not between 1 to 31") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	    //Parser should generate error when the content to show/sort/reset is invalid
	    input = "show aaaaa";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidTaskTypeError: 'aaaaa' is not 'todo', 'event' or 'floating'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    input = "sort aaaaa";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidSortFieldError: 'aaaaa' is not 'deadline', 'name' or 'priority'") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);  
	    input = "1 reset aaaaaaaaaa";
	    actual = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("error", "InvalidResetError: 'aaaaaaaaaa' is not a field that can be reset") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + actual.toString());
	    assertEquals(expected, actual);
	    
	}
}
