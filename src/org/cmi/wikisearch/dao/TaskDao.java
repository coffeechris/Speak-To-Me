package org.cmi.wikisearch.dao;

import org.cmi.wikisearch.beans.Task;
import org.cmi.wikisearch.beans.TaskOrder;

import java.util.Map;

public interface TaskDao {
	public Map<String, Task> getTasksById ();
	
	public TaskOrder[] getTaskOrders (); 
	
	public void persistTaskOrders ();
}
