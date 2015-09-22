package main.java.backend.Parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
	Parser parser = new Parser();
	
	@Test
	public void basicTest() {
	    ArrayList<String> parsed = parser.parseInput("add Project Proposal");
	    ArrayList<String> expected = new ArrayList<String>( Arrays.asList("add", "Project Proposal") );
	    System.out.println("Expected: " + expected.toString());
	    System.out.println("Actual: " + parsed.toString());
	    assertEquals(expected, parsed);
	}
}
