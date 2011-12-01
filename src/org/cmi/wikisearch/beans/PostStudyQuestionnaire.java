package org.cmi.wikisearch.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONException;

public class PostStudyQuestionnaire implements Questionnaire {
	private String comment1;

	protected final Log logger = LogFactory.getLog(getClass());
	
	public String toString() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("comment1", this.getComment1());
			return jsonObject.toString();
		}
		catch (JSONException json){
			logger.error("Error in formatting questionnaire");
			throw new RuntimeException("Error in formatting questionnaire", json);
		}
	}
	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}
	
}
