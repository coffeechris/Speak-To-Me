package org.cmi.wikisearch.beans;

import java.util.List;

public class SearchInfo {
    private List<SearchResult> results;
    private int totalHits;
    
    public List<SearchResult> getResults() {
        return results;
    }
    public void setResults(List<SearchResult> results) {
        this.results = results;
    }
    public int getTotalHits() {
        return totalHits;
    }
    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }
}
