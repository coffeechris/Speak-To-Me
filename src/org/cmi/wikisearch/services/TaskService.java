package org.cmi.wikisearch.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmi.wikisearch.dao.TaskDao;
import org.cmi.wikisearch.beans.TaskOrder;
import org.cmi.wikisearch.beans.Task;

public class TaskService {
	private TaskDao taskDao;
	TaskOrder [] taskOrders;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public synchronized int getNextTaskOrderIndex () {
		//scan the array for the next least used
		int taskOrderIndex = 0;
		for (int i = 1; i < taskOrders.length; i++){
			//swap in the task order that has been used the least
			if (taskOrders[i].getCount() < taskOrders[taskOrderIndex].getCount()){
				taskOrderIndex = i;
			}
		}
		taskOrders[taskOrderIndex].setCount(taskOrders[taskOrderIndex].getCount()+1);
		
		if (logger.isDebugEnabled())
			logger.debug("taskOrderIndex: " + taskOrderIndex + " count: "+taskOrders[taskOrderIndex].getCount());
		
		return taskOrderIndex;
	}
	
	public TaskOrder getTaskOrder (int taskOrderIndex){
		return taskOrders[taskOrderIndex];
	}
	
	public Task getTask (String key){
		return taskDao.getTasksById().get(key);
	}

	public synchronized void increaseTaskOrderCount (int taskOrderIndex){
		//update the data in the dao
		this.taskDao.getTaskOrders()[taskOrderIndex].setCount(this.taskDao.getTaskOrders()[taskOrderIndex].getCount()+1);
		this.taskDao.persistTaskOrders();
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
		
		//set cache of task orders - maintains cached task order count
		taskOrders = new TaskOrder[taskDao.getTaskOrders().length];
		for (int i = 0; i < taskDao.getTaskOrders().length; i++){
			taskOrders[i] = new TaskOrder();
			taskOrders[i].setCount(taskDao.getTaskOrders()[i].getCount());
			taskOrders[i].setIds(taskDao.getTaskOrders()[i].getIds());
		}
	}
	
}
