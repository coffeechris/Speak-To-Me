package org.cmi.wikisearch.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmi.wikisearch.beans.Questionnaire;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class QuestionnaireFileDao implements QuestionnaireDao {

	private String storagePath;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public synchronized void storeQuestionnaire(String userId, Questionnaire questionnaire) {
		try {
			FileWriter writer = new FileWriter(this.getStoragePath() + File.separator + userId);
			writer.write(questionnaire.toString());
			writer.close();
		}
		catch (IOException io){
			logger.error("Error logging questionnaire for userId: " + userId, io);
			throw new RuntimeException("Error logging questionnaire for userId: " + userId, io);
		}
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

}
