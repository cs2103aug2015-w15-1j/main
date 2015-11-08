package main.java.backend.Util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerGlobal {
	
	private static final String LOGGER_INIT_UNSUCCESSFUL = "Logger failed to initialise.";
	private static final String LOGGER_FILE_NAME = "TankTaskLog.log";
	private static final int LOGGER_LIMIT = 1000000000;
	private static final int LOGGER_COUNT = 10;
	
	private static final Logger logger = Logger.getGlobal();
	private static FileHandler logHandler;
	
	public LoggerGlobal() {
		
	}
	
	public static void initLogger() {
		
		try {
			logHandler = new FileHandler(LOGGER_FILE_NAME, LOGGER_LIMIT, LOGGER_COUNT, true);
			logHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(logHandler);
			logger.setUseParentHandlers(false);

		} catch (SecurityException | IOException e) {
			System.out.println(LOGGER_INIT_UNSUCCESSFUL);
		}
	}

	public static Logger getLogger() {
		return logger;
	}
	
}
