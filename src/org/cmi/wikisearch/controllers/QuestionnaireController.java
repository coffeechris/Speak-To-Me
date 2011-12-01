package org.cmi.wikisearch.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmi.wikisearch.beans.PreTaskQuestionnaire;
import org.cmi.wikisearch.beans.PostTaskQuestionnaire;
import org.cmi.wikisearch.beans.PreStudyQuestionnaire;
import org.cmi.wikisearch.beans.PostStudyQuestionnaire;
import org.cmi.wikisearch.beans.TaskOrder;
import org.cmi.wikisearch.services.TaskService;
import org.cmi.wikisearch.services.UserIdService;
import org.cmi.wikisearch.services.QuestionnaireService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@Controller
public class QuestionnaireController {

	private String userIdCookieName;
	private String taskOrderCookieName;
	private String taskIndexCookieName;
	
	private String consentView;
	private String preStudyView;
	private String postStudyView;
	private String endStudyView;
	private String preTaskView;
	private String postTaskView;
	
	private TaskService taskService;
	private UserIdService userIdService;
	private QuestionnaireService preStudyService;
	private QuestionnaireService postStudyService;
	private List<QuestionnaireService> pretaskQuestionnaireServices;
	private List<QuestionnaireService> posttaskQuestionnaireServices;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value="/consent.htm")
	public ModelAndView consent () {
		return new ModelAndView(this.getConsentView());
	}
	
	@RequestMapping(value="/pre-study.htm")
	public ModelAndView prestudy (HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//if the userId has been set, then the form has been submitted - process
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		if (userIdCookie != null && request.getParameter("q") != null){
			//store questionnaire data
			PreStudyQuestionnaire preStudyQuestionnaire = new PreStudyQuestionnaire();
			preStudyQuestionnaire.setComment1("prestudy comments to come");
			preStudyService.storeQuestionnaire(userIdCookie.getValue(), preStudyQuestionnaire);
			
			//prepare for first task
			int taskOrderIndex = taskService.getNextTaskOrderIndex();
			response.addCookie(new Cookie(this.getTaskOrderCookieName(), Integer.toString(taskOrderIndex)));
			response.addCookie(new Cookie(this.getTaskIndexCookieName(), Integer.toString(0)));
			
			if (logger.isDebugEnabled())
				logger.debug("userId: " + userIdCookie.getValue() + "\ttask order index: " + taskOrderIndex);
			
			return new ModelAndView("redirect:/pre-task.htm");
		}
		
		//else the form has not been filled out - get next userId
		int userId = userIdService.nextId();
		model.put("userId", userId);
		response.addCookie(new Cookie(this.getUserIdCookieName(), Integer.toString(userId)));
		
		return new ModelAndView(preStudyView, "model", model);
	}
	
	@RequestMapping(value="/post-study.htm")
	public ModelAndView poststudy (HttpServletRequest request, HttpServletResponse response) {
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		if (userIdCookie != null){
			//questionnaire submitted
			if (request.getParameter("q") != null){
				//store questionnaire data
				PostStudyQuestionnaire postStudyQuestionnaire = new PostStudyQuestionnaire();
				postStudyQuestionnaire.setComment1("poststudy comments to come");
				postStudyService.storeQuestionnaire(userIdCookie.getValue(), postStudyQuestionnaire);
				
				//persist the task order count
				taskService.increaseTaskOrderCount(Integer.parseInt(
								WebUtils.getCookie(request, this.getTaskOrderCookieName()).getValue()));
				
				//clear cookies - post study clean up
				userIdCookie.setMaxAge(0);
				response.addCookie(userIdCookie);
				Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
				if (taskOrderCookie != null){
					taskOrderCookie.setMaxAge(0);
					response.addCookie(taskOrderCookie);
				}
				Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
				if (taskIndexCookie != null){
					taskIndexCookie.setMaxAge(0);
					response.addCookie(taskIndexCookie);
				}
				
				return new ModelAndView(this.getEndStudyView());
			}
			
			//display questionnaire data
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("userId", Integer.parseInt(userIdCookie.getValue()));
			if (logger.isDebugEnabled()){
				logger.debug("userId: " + userIdCookie.getValue());
			}
			
			return new ModelAndView(this.getPostStudyView(), "model", model);
		}
		
		//no user id - redirect to the prestudy questionnaire
		return new ModelAndView("redirect:/pre-study.htm");
	}

	@RequestMapping(value="/pre-task.htm")
	public ModelAndView preTask (HttpServletRequest request, HttpServletResponse response){
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
		Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
		//no user id - redirect to the prestudy questionnaire
		if (userIdCookie == null){
			return new ModelAndView("redirect:/pre-study.htm");
		}
		
		int taskIndex = Integer.parseInt(taskIndexCookie.getValue());
		TaskOrder taskOrder = taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue()));
		
		//the form has been submitted - process
		if (request.getParameter("q") != null){
			//store questionnaire data using the pretaskQuestionnaireServices
			PreTaskQuestionnaire preTaskQuestionnaire = new PreTaskQuestionnaire();
			preTaskQuestionnaire.setTaskId(taskOrder.getIds().get(taskIndex));
			preTaskQuestionnaire.setTaskOrderIndex(taskIndex);
			preTaskQuestionnaire.setComment1("pretask questionnaire to come...");
			pretaskQuestionnaireServices.get(taskIndex).storeQuestionnaire(userIdCookie.getValue(), preTaskQuestionnaire);		
			
			if (logger.isDebugEnabled())
				logger.debug("userId: " + userIdCookie.getValue() + "\ttask index: " + taskIndex +
						     "\ttask id: " + taskOrder.getIds().get(taskIndex));
			
			
			return new ModelAndView("redirect:/vocal-search-interface.htm");
		}
		
		//no submission - display form
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userId", Integer.parseInt(userIdCookie.getValue()));
		model.put("taskIndex", taskIndex);
		model.put("taskId", taskOrder.getIds().get(taskIndex));
		return new ModelAndView(this.getPreTaskView(), "model", model);
	}
	
	@RequestMapping(value="/post-task.htm")
	public ModelAndView postTask (HttpServletRequest request, HttpServletResponse response){
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
		Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
		//no user id - redirect to the prestudy questionnaire
		if (userIdCookie == null){
			return new ModelAndView("redirect:/pre-study.htm");
		}
		
		int taskIndex = Integer.parseInt(taskIndexCookie.getValue());
		TaskOrder taskOrder = taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue()));
		
		//the form has been submitted - process
		if (request.getParameter("q") != null){
			//store questionnaire data using the posttaskQuestionnaireServices
			PostTaskQuestionnaire postTaskQuestionnaire = new PostTaskQuestionnaire();
			postTaskQuestionnaire.setTaskId(taskOrder.getIds().get(taskIndex));
			postTaskQuestionnaire.setTaskOrderIndex(taskIndex);
			postTaskQuestionnaire.setComment1("posttask questionnaire to come...");
			posttaskQuestionnaireServices.get(taskIndex).storeQuestionnaire(userIdCookie.getValue(), postTaskQuestionnaire);
			
			//check if there are more tasks
			taskIndex ++;
			//update cookie
			if (taskIndex < taskOrder.getIds().size()){
				taskIndexCookie.setValue(Integer.toString(taskIndex));
				response.addCookie(taskIndexCookie);
				
				if (logger.isDebugEnabled()){
					logger.debug("Updated cookie name: " + taskIndexCookie.getName() + "\tvalue: " + taskIndexCookie.getValue());
				}
				
				return new ModelAndView("redirect:/pre-task.htm");
			}
			//no more tasks - redirect to post study questionnaire
			else {
				return new ModelAndView("redirect:/post-study.htm");
			}
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userId", Integer.parseInt(userIdCookie.getValue()));
		model.put("taskIndex", taskIndex);
		model.put("taskId", taskOrder.getIds().get(taskIndex));
		return new ModelAndView(this.getPostTaskView(), "model", model);
	}
	
	public String getPreStudyView() {
		return preStudyView;
	}

	public void setPreStudyView(String preStudyView) {
		this.preStudyView = preStudyView;
	}

	public String getPostStudyView() {
		return postStudyView;
	}

	public void setPostStudyView(String postStudyView) {
		this.postStudyView = postStudyView;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public String getUserIdCookieName() {
		return userIdCookieName;
	}

	public void setUserIdCookieName(String userIdCookieName) {
		this.userIdCookieName = userIdCookieName;
	}

	public UserIdService getUserIdService() {
		return userIdService;
	}

	public void setUserIdService(UserIdService userIdService) {
		this.userIdService = userIdService;
	}

	public QuestionnaireService getPreStudyService() {
		return preStudyService;
	}

	public void setPreStudyService(QuestionnaireService preStudyService) {
		this.preStudyService = preStudyService;
	}

	public QuestionnaireService getPostStudyService() {
		return postStudyService;
	}

	public void setPostStudyService(QuestionnaireService postStudyService) {
		this.postStudyService = postStudyService;
	}

	public String getTaskOrderCookieName() {
		return taskOrderCookieName;
	}

	public void setTaskOrderCookieName(String taskOrderCookieName) {
		this.taskOrderCookieName = taskOrderCookieName;
	}

	public String getTaskIndexCookieName() {
		return taskIndexCookieName;
	}

	public void setTaskIndexCookieName(String taskIndexCookieName) {
		this.taskIndexCookieName = taskIndexCookieName;
	}

	public String getEndStudyView() {
		return endStudyView;
	}

	public void setEndStudyView(String endStudyView) {
		this.endStudyView = endStudyView;
	}

	public String getConsentView() {
		return consentView;
	}

	public void setConsentView(String consentView) {
		this.consentView = consentView;
	}

	public String getPreTaskView() {
		return preTaskView;
	}

	public void setPreTaskView(String preTaskView) {
		this.preTaskView = preTaskView;
	}

	public String getPostTaskView() {
		return postTaskView;
	}

	public void setPostTaskView(String postTaskView) {
		this.postTaskView = postTaskView;
	}

	public List<QuestionnaireService> getPretaskQuestionnaireServices() {
		return pretaskQuestionnaireServices;
	}

	public void setPretaskQuestionnaireServices(
			List<QuestionnaireService> pretaskQuestionnaireServices) {
		this.pretaskQuestionnaireServices = pretaskQuestionnaireServices;
	}

	public List<QuestionnaireService> getPosttaskQuestionnaireServices() {
		return posttaskQuestionnaireServices;
	}

	public void setPosttaskQuestionnaireServices(
			List<QuestionnaireService> posttaskQuestionnaireServices) {
		this.posttaskQuestionnaireServices = posttaskQuestionnaireServices;
	}
}
