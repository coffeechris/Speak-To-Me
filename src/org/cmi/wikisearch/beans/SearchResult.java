package org.cmi.wikisearch.beans;

public class SearchResult {
	private float score;
	private String docID;
	private String title;
	private String gist;
	
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getDocID() {
		return docID;
	}
	public void setDocID(String docID) {
		this.docID = docID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGist() {
		return gist;
	}
	public void setGist(String gist) {
		this.gist = gist;
	}
}
