package org.cmi.wikisearch.dao;

import org.cmi.wikisearch.beans.Questionnaire;

public interface QuestionnaireDao {
	public void storeQuestionnaire (String userId, Questionnaire questionnaire);
}
