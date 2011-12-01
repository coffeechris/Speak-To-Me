package org.cmi.wikisearch.dao;

import java.util.Map;

public interface UserLoggerDao {
	public void logData (String userID, String taskID, String taskIndex, Map<String,String> data);
}
