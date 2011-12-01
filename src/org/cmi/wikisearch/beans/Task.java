package org.cmi.wikisearch.beans;

public class Task {
	private String id;
	private String desc;
	private int minutes;
	private int seconds;
	
	public String toString () {
		return "\nid: " + id + " minutes: " + minutes + " seconds: " + seconds +
		       "\ndesc: " + desc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
