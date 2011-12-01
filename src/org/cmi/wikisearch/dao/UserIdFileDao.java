package org.cmi.wikisearch.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserIdFileDao implements UserIdDao {

	private String userIdFile;
	private int userId;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public int getId() {
		return userId;
	}

	public synchronized void setId(int id) {
		userId = id;
		try {
			FileWriter writer = new FileWriter(this.getUserIdFile());
			writer.write(Integer.toString(userId));
			writer.close();
		}
		catch(IOException io){
			logger.error("Error writing the user id file", io);
			throw new RuntimeException(io);
		}
	}

	public String getUserIdFile() {
		return userIdFile;
	}

	public void setUserIdFile(String userIdFile) {
		this.userIdFile = userIdFile;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.getUserIdFile()));
			userId = Integer.parseInt(br.readLine().trim());
		}
		catch(IOException io){
			logger.error("Error reading the user id file", io);
			throw new RuntimeException(io);
		}
	}

}
