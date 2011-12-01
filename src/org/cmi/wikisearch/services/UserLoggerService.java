package org.cmi.wikisearch.services;

import java.util.Map;

import org.cmi.wikisearch.dao.UserLoggerDao;

public class UserLoggerService {
	UserLoggerDao userLoggerDao;
	
	public void logData (String userID, String taskID, String taskIndex, Map<String,String> data){
		//I reserve the option to perform data cleaning hence the
		//need for this object
		userLoggerDao.logData(userID,taskID, taskIndex, data);
	}

	public UserLoggerDao getUserLoggerDao() {
		return userLoggerDao;
	}

	public void setUserLoggerDao(UserLoggerDao userLoggerDao) {
		this.userLoggerDao = userLoggerDao;
	}
}
