package main.java.backend.Parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
	public Parser parser = new Parser();
	public ArrayList<String> parsed; 
	public ArrayList<String> expected;
	
	@Test
	public void BasicTests() {
		System.out.println("\n-----------------Result for BasicTests-----------------");
		
		parsed = parser.parseInput("add Project Proposal");
	    expected = new ArrayList<String>( Arrays.asList("add", "Project Proposal") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("deadline 30 October 2359");
	    expected = new ArrayList<String>( Arrays.asList("deadline", "30 October 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("priority 5");
	    expected = new ArrayList<String>( Arrays.asList("priority", "5") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("description i need to start doing this!");
	    expected = new ArrayList<String>( Arrays.asList("description", "i need to start doing this!") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
		parsed = parser.parseInput("ADD PROJECT PROPOSAL");
	    expected = new ArrayList<String>( Arrays.asList("add", "PROJECT PROPOSAL") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("done Project Proposal");
	    expected = new ArrayList<String>( Arrays.asList("done", "Project Proposal") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("return");
	    expected = new ArrayList<String>( Arrays.asList("return") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void RemoveExtraTokens() {
		System.out.println("\n-----------------Result for RemoveExtraTokens-----------------");
		
	    parsed = parser.parseInput("return extra extra extra words");
	    expected = new ArrayList<String>( Arrays.asList("return") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("undo extra extra extra words");
	    expected = new ArrayList<String>( Arrays.asList("undo") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void OneShotCommand() {
		System.out.println("\n-----------------Result for OneShotCommand-----------------");
		
	    parsed = parser.parseInput("add Project Proposal deadline 30 October 2359");
	    expected = new ArrayList<String>( Arrays.asList("add", "Project Proposal", "deadline", "30 October 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("Project Proposal priority 5");
	    expected = new ArrayList<String>( Arrays.asList("Project Proposal", "priority", "5") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add Project Proposal deadline 30 October 2359 priority 5 description i need to start doing this!");
	    expected = new ArrayList<String>( Arrays.asList("add", "Project Proposal", "deadline", "30 October 2359", 
	    								  "priority", "5", "description", "i need to start doing this!") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("Project Presentation description i must reach there early event 30 October 1200 reminder 29 October 2100");
	    expected = new ArrayList<String>( Arrays.asList("Project Presentation", "description", "i must reach there early",
	    								  "event", "30 October 1200", "reminder", "29 October 2100") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void DuplicateCommands() {
		System.out.println("\n-----------------Result for DuplicateCommands-----------------");
		
	    parsed = parser.parseInput("deadline tomorrow deadline 30 October 2359");
	    expected = new ArrayList<String>( Arrays.asList("deadline", "30 October 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("priority 1 priority 5 priority 3");
	    expected = new ArrayList<String>( Arrays.asList("priority", "3") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("deadline deadline");
	    expected = new ArrayList<String>( Arrays.asList("deadline") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add Project Presentation priority 4 event tomorrow event 30 October 1200 reminder 29 October 1200");
	    expected = new ArrayList<String>( Arrays.asList("add", "Project Presentation", "priority", "4", "event", "30 October 1200", "reminder", "29 October 1200" ) );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
	
	@Test
	public void CommandInParameter() {
		System.out.println("\n-----------------Result for CommandInParameter-----------------");
		
	    parsed = parser.parseInput("Project Proposal: deadline coming soon");
	    expected = new ArrayList<String>( Arrays.asList("Project Proposal:", "deadline", "coming soon") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
		
	    parsed = parser.parseInput("Project Proposal: deadline coming soon deadline 30 September 2359");
	    expected = new ArrayList<String>( Arrays.asList("Project Proposal: deadline coming soon", "deadline", "30 September 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add Project Proposal: deadline coming soon deadline 30 September 2359");
	    expected = new ArrayList<String>( Arrays.asList("add", "Project Proposal: deadline coming soon", "deadline", "30 September 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("Project Proposal description deadline is coming soon deadline 30 September 2359");
	    expected = new ArrayList<String>( Arrays.asList("Project Proposal", "description", "deadline is coming soon", "deadline", "30 September 2359") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add deadline is coming! priority 5 deadline tomorrow");
	    expected = new ArrayList<String>( Arrays.asList("add", "deadline is coming!", "priority", "5", "deadline", "tomorrow") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add add");
	    expected = new ArrayList<String>( Arrays.asList("add", "add") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	    
	    parsed = parser.parseInput("add add food to the fridge priority 2");
	    expected = new ArrayList<String>( Arrays.asList("add", "add food to the fridge", "priority", "2") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual:   " + parsed.toString());
	    assertEquals(expected, parsed);
	}
}
