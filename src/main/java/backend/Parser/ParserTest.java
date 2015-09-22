package main.java.backend;

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
	    assertEquals(expected, parsed);
	}
}
