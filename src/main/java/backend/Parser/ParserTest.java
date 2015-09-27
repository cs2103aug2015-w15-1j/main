package main.java.backend.Parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
	public Parser parser = new Parser();
	String input;
	public ArrayList<String> parsed; 
	public ArrayList<String> expected;
	
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
	    
	    input = "Project Proposal deadline 30 October 2359";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal", "30 October 2359") );
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
	}
	
	@Test
	public void OneShotCommand() {
		System.out.println("\n-----------------Result for OneShotCommand-----------------");
	    
		input = "add Project Proposal priority 5";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "Project Proposal", "", "5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add Project Proposal deadline 30/10/15 2359";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "30/10/15 2359", "", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add Project Proposal deadline 30/10/15 2359 priority 5 description i need to start doing this!";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "i need to start doing this!", "30/10/15 2359", 
	    												"5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);   
	    
	    input = "add User Guide reminder 20/10/15 1200 description i will do this in 10 days priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addF", "User Guide", "i will do this in 10 days", "3", 
	    												"20/10/15 1200", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);  
	    
	    input = "CS2103 Assignment description i must finish this early reminder 19/10/15 2100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("set", "CS2103 Assignment", "i must finish this early", "", 
	    												"19/10/15 2100", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "CS2101 Project Research priority 4 deadline 05/10/15 2000 category research ";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "CS2101 Project Research", "", "05/10/15 2000",
	    												"4", "", "research") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline 30/10/15 description must be done";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "Project Proposal", "must be done", "30/10/15", "", "", "") );
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
	    
	    input = "deadline deadline deadline 10/10/10 1000";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "deadline deadline", "10/10/10 1000") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal deadline tomorrow deadline 30/10/15 2359";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("deadline", "Project Proposal deadline tomorrow", "30/10/15 2359") );
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
	    
		input = "add Project Proposal deadline coming soon deadline 30/09/15 2359";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal deadline coming soon", "", "30/09/15 2359", 
														"", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add deadline is coming! priority 5 deadline tomorrow";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "deadline is coming!", "", "tomorrow", 
														"5", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "add Project Proposal priority 4 deadline tomorrow reminder 29/10/15 1200 deadline 30/10/15 1200";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addT", "Project Proposal", "", "30/10/15 1200", 
														"4 deadline tomorrow", "29/10/15 1200", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		input = "Project Proposal description deadline is coming soon deadline 30/09/15 2359";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setT", "Project Proposal", "deadline is coming soon", "30/09/15 2359", 
														"", "", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void EventDateTime(){
		System.out.println("\n-----------------Result for EventDateTime-----------------");
		
	    input = "OP1 event 15/09/15 1000";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP1", "15/09/15 1000", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    input = "OP2 event 15/09/15 1000 - 16/09/15 0959";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP2", "15/09/15 1000", "16/09/15 0959") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP3 event 15/09/15 1000-16/09/15 0959";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP3", "15/09/15 1000", "16/09/15 0959") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "OP4 event 15/09/15 1000-1400";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("event", "OP4", "15/09/15 1000", "15/09/15 1400") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Presentation description i must reach there early event 30/10/15 1200 reminder 29/10/15 2100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setE", "Project Presentation", "i must reach there early", "30/10/15 1200", "",
	    									"", "29/10/15 2100", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "Project Presentation 2 description i must reach there early too event 30/10/15 1200 - 1400 reminder 29/10/15 2100";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("setE", "Project Presentation 2", "i must reach there early too", "30/10/15 1200",
	    								"30/10/15 1400", "", "29/10/15 2100", "") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    input = "add 2101 meeting category meetings event 12/10/15 1200 - 1800 priority 3";
	    parsed = parser.parseInput(input);
	    expected = new ArrayList<String>( Arrays.asList("addE", "2101 meeting", "", "12/10/15 1200", "12/10/15 1800",
	    									"3", "", "meetings") );
	    System.out.println("Input:    " + input);
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
}
