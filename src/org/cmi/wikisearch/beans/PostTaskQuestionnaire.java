package org.cmi.wikisearch.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class PostTaskQuestionnaire implements Questionnaire {
	private String taskId;
	private int taskOrderIndex;
	private String comment1;

	protected final Log logger = LogFactory.getLog(getClass());
	
	public String toString() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("taskId", taskId);
			jsonObject.put("taskOrderIndex", taskOrderIndex);
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public int getTaskOrderIndex() {
		return taskOrderIndex;
	}
	public void setTaskOrderIndex(int taskOrderIndex) {
		this.taskOrderIndex = taskOrderIndex;
	}
	
}
