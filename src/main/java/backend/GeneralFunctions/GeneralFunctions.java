package main.java.backend.GeneralFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

import main.java.backend.Storage.Task.Task;

public class GeneralFunctions {

	public static final SimpleDateFormat formatterForDateTime = 
			new SimpleDateFormat("EEE, dd MMM hh:mma");
	public static final SimpleDateFormat standardFormat = 
			new SimpleDateFormat("EEE, dd MMM hh:mma yyyy");

	private GeneralFunctions() {
		// prevent GeneralFunctions.java from being instantiated
	}

	public static long stringToMillisecond(String dateTime) {
		try {
			Date tempDateTime = formatterForDateTime.parse(dateTime);
			long dateTimeMillisecond = tempDateTime.getTime();
			return (dateTimeMillisecond);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		//Should not reach here
		return -1;
	}

	public static long getCurrentTime() {

		long currentMilliseconds = System.currentTimeMillis();
		Date resultdate = new Date(currentMilliseconds);

		return GeneralFunctions.stringToMillisecond(standardFormat.format(resultdate));
	}

	public static long getTime(Task task) {

		if(task.getEndTime() > 0) {
			return task.getEndTime();
		} else {
			return task.getStartTime();
		}
	}
}
