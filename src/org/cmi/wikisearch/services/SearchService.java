package org.cmi.wikisearch.services;

import org.cmi.wikisearch.beans.SearchInfo;

public interface SearchService {
	public SearchInfo search (String searchQuery, int numResults);
}
