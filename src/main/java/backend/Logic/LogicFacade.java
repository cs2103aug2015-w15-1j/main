package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Parser.Parser;

public class LogicFacade {
	private LogicFacade logicFacade;
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static Parser parserComponent;
	private static LogicCommandHandler logicCommandHandler;
	
	private LogicFacade(String filename) {
		initLogger();
		logicCommandHandler = LogicCommandHandler.getInstance(filename);
		parserComponent = new Parser();
	}
	
	public LogicFacade getInstance(String filename) {
		if (logicFacade == null) {
			logicFacade = new LogicFacade(filename);
		}
		return logicFacade;
	}
	
	private void initLogger() {
		try {
			logHandler = new FileHandler("LogicFacadeLog.txt",true);
			logHandler.setFormatter(new SimpleFormatter());
			logicControllerLogger.addHandler(logHandler);
			logicControllerLogger.setUseParentHandlers(false);
			
		} catch (SecurityException | IOException e) {
			logicControllerLogger.warning("Logger failed to initialise: " + e.getMessage());
		}
	}
	
	public String execute(String userInput) {
		ArrayList<String> parsedUserInput = parserComponent.parseInput(userInput);
		Command commandObject = logicCommandHandler.parse(parsedUserInput);
		String feedbackString = commandObject.execute();
		return feedbackString;
	}

}
