package org.cmi.wikisearch.services;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.cmi.wikisearch.beans.TaskOrder;
import org.cmi.wikisearch.dao.TaskFileDao;

public class TaskServiceTests extends TestCase {

		public void testServices () throws Exception {
			TaskOrder taskOrder1 = new TaskOrder();
			ArrayList<String> ids1 = new ArrayList<String>();
			ids1.add("id1");
			taskOrder1.setIds(ids1);
			
			TaskOrder taskOrder2 = new TaskOrder();
			ArrayList<String> ids2 = new ArrayList<String>();
			ids2.add("id2");
			taskOrder2.setIds(ids2);
			
			TaskOrder taskOrder3 = new TaskOrder();
			ArrayList<String> ids3 = new ArrayList<String>();
			ids3.add("id3");
			taskOrder3.setIds(ids3);
			
			TaskOrder [] taskOrders = {taskOrder1, taskOrder2, taskOrder3};
			
			TaskFileDao dao = new TaskFileDao();
			dao.setTaskOrders(taskOrders);
			
			TaskService service = new TaskService();
			service.setTaskDao(dao);
			
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id1");
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id2");
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id3");
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id1");
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id2");
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getIds().get(0), "id3");
			
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getCount(), 3);
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getCount(), 3);
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getCount(), 3);
			assertEquals(service.getTaskOrder(service.getNextTaskOrderIndex()).getCount(), 4);
		}
		
}
