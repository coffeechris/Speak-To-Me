package org.cmi.wikisearch.services;

import org.cmi.wikisearch.dao.QuestionnaireDao;
import org.cmi.wikisearch.beans.Questionnaire;

public class QuestionnaireService {
	QuestionnaireDao questionnaireDao;
	
	public void storeQuestionnaire (String userId, Questionnaire questionnaire){
		questionnaireDao.storeQuestionnaire(userId, questionnaire);
	}

	public QuestionnaireDao getQuestionnaireDao() {
		return questionnaireDao;
	}

	public void setQuestionnaireDao(QuestionnaireDao questionnaireDao) {
		this.questionnaireDao = questionnaireDao;
	}
	
}
