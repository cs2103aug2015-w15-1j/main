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
	public void basicTest() {
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
	public void removeExtraTokens() {
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
}
