package org.cmi.wikisearch.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONException;

public class UserLoggerFileDao implements UserLoggerDao {
	private String logPath;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public synchronized void logData(String userID, String taskID, String taskIndex, Map<String,String> data) {
		try {
			//append data to the file
			FileWriter fileWriter = new FileWriter(logPath+File.separator+userID, true);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("timestamp", System.currentTimeMillis());
			jsonObject.put("taskID", taskID);
			jsonObject.put("taskIndex", taskIndex);
			jsonObject.put("data", data);
			
			fileWriter.write(jsonObject.toString() + System.getProperty("line.separator"));
			fileWriter.close();
		}
		catch (IOException io) {
			//Log the error and display it on the Web browser.
			//This kind of error should interrupt the participant during the user study
			//so a study invigilator can be notified logging it not working.   
			logger.error("Error writing log file: " + logPath+File.separator+userID, io);
			throw new RuntimeException("Please notify user study support. Error in logging user data.", io);
		}
		catch (JSONException json){
			logger.error("Error forming log entry for userID: " + userID, json);
			throw new RuntimeException("Error forming log entry for userID: " + userID, json);
		}
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

}
