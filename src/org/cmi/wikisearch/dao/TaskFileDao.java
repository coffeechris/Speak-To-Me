package org.cmi.wikisearch.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmi.wikisearch.beans.Task;
import org.cmi.wikisearch.beans.TaskOrder;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class TaskFileDao implements TaskDao {

	private String taskOrderDataFileName;
	
	private TaskOrder [] taskOrders;
	private Map<String, Task> tasksById;
	
	protected final Log logger = LogFactory.getLog(getClass());

	public synchronized void persistTaskOrders() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.taskOrderDataFileName));
			for (TaskOrder to : taskOrders){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count", to.getCount());
				jsonObject.put("taskIds", to.getIds());
				bw.write(jsonObject.toString(), 0, jsonObject.toString().length());
				bw.newLine();
			}
			bw.close();
		}
		catch (IOException io){
			logger.error("Error writing the task orders to file", io);
			throw new RuntimeException(io);
		}
		catch (JSONException json){
			logger.error("Error constructing json objects prior to persisting task order data", json);
			throw new RuntimeException(json);
		}
	}

	public TaskOrder[] getTaskOrders() {
		return this.taskOrders;
	}

	/**
	 * Currently only used for testing.
	 * @param taskOrders
	 */
	public void setTaskOrders (TaskOrder[] taskOrders){
		this.taskOrders = taskOrders;
	}
	
	public String getTaskOrderDataFileName() {
		return taskOrderDataFileName;
	}

	public void setTaskOrderDataFileName(String taskOrderDataFileName) {
		this.taskOrderDataFileName = taskOrderDataFileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.taskOrderDataFileName));
			String line;
			List<TaskOrder> TOs = new ArrayList<TaskOrder>();
			while ((line = br.readLine()) != null){
				JSONObject jsonObject = new JSONObject(line);
				int count = jsonObject.getInt("count");
				JSONArray ids = jsonObject.getJSONArray("taskIds");
				List <String> taskIds = new ArrayList<String>();
				for (int i = 0; i < ids.length(); i++){
					taskIds.add(ids.getString(i));
				}
				TaskOrder taskOrder = new TaskOrder();
				taskOrder.setCount(count);
				taskOrder.setIds(taskIds);
				
				if (logger.isDebugEnabled()){
					logger.debug("count: " + count + "\torder: " + taskOrder.toString());
				}
				
				TOs.add(taskOrder);
			}
			this.taskOrders = new TaskOrder[TOs.size()];
			this.taskOrders = TOs.toArray(this.taskOrders);
		}
		catch (IOException io){
			logger.error("Problems reading the task order file", io);
			throw new RuntimeException(io);
		}
		catch (JSONException json){
			logger.error("The task order file is not formatted properly", json);
			throw new RuntimeException(json);
		}
		
		if (logger.isDebugEnabled()){
			for (TaskOrder to : this.taskOrders){
				logger.debug(to.getIds().toArray());
			}
		}
	}

	public Map<String, Task> getTasksById() {
		return this.tasksById;
	}

	public void setTaskData(Map<String,Task> taskData) {
		this.tasksById = taskData;
	}

}
