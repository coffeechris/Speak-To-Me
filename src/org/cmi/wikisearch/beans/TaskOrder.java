package org.cmi.wikisearch.beans;

import java.util.List;

public class TaskOrder implements Comparable <TaskOrder> {
	int count;
	List<String> ids;
	
	public int compareTo(TaskOrder o) {
		return count - o.getCount();
	}
	
	public String toString(){
		return "count: " + count + " ids: " + ids.toString();
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}	
}
