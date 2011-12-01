package org.cmi.wikisearch.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cmi.wikisearch.services.SearchService;
import org.cmi.wikisearch.services.TaskService;
import org.cmi.wikisearch.services.UserLoggerService;
import org.cmi.wikisearch.beans.SearchInfo;
import org.cmi.wikisearch.beans.Task;
import org.cmi.wikisearch.beans.SearchResult;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;
import org.springframework.stereotype.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Controller
public class SearchController {
	
	private String userIdCookieName;
	private String taskOrderCookieName;
	private String taskIndexCookieName;
	private int    resultsLimit;
	private int    resultsPerPage;
	
	private SearchService searchService;
	private TaskService taskService;
	
	private UserLoggerService searchQueryLogger;
	private UserLoggerService interactionLogger;
	
	private String searchView;
	private String vocalSearchView;
	private String searchResultsView;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value="/basic-search.htm")
	public ModelAndView basicSearch(HttpServletRequest request, HttpServletResponse response) {
		String searchQuery = request.getParameter("searchQuery");
		if (searchQuery==null)
			return new ModelAndView(this.getSearchView());
		
		if (logger.isDebugEnabled())
			logger.debug("processing search query");
		
		List<SearchResult> searchResults = searchService.search(searchQuery, this.getResultsLimit()).getResults();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("searchResults", searchResults);
		model.put("searchQuery", searchQuery);
		return new ModelAndView(this.getSearchView(), "model", model);
	}
	
	@RequestMapping(value="/vocal-search-interface.htm")
	public ModelAndView vocalSearch(HttpServletRequest request, HttpServletResponse response){
		//check cookies
		if (logger.isDebugEnabled()){
			for (Cookie cookie : request.getCookies()){
				logger.debug("Cookie name: " + cookie.getName() +"\tvalue: " + cookie.getValue());
			}
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		//get cookies
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
		Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
		//if any cookie is null - redirect back to pre-study questionnaire
		if (userIdCookie == null || taskOrderCookie == null || taskIndexCookie == null){
			return new ModelAndView("redirect:/pre-study.htm");
		}
		model.put("userId", userIdCookie.getValue());
		model.put("taskOrderIndex", Integer.parseInt(taskOrderCookie.getValue()));
		
		//check to see if the end task button was pressed
		if (request.getParameter("et") != null){		
			//log end of task timestamp
			
			return new ModelAndView("redirect:/post-task.htm");
		}
		
		if (logger.isDebugEnabled())
			logger.debug("\nuser id: " + userIdCookie.getValue() +
					     "\ntask order index: " + taskOrderCookie.getValue() +
					     "\ttask order: " + taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue())));
		
		//Use the task index to get the task id from the task order
		String taskId = taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue())).getIds().get(
							Integer.parseInt(taskIndexCookie.getValue()));
		Task task = taskService.getTask(taskId);
		model.put("task", task);
		model.put("taskIndex", Integer.parseInt(taskIndexCookie.getValue()));
		
		if (logger.isDebugEnabled())
			logger.debug("\nTask: "+task.toString());
		
		return new ModelAndView(this.getVocalSearchView(), "model", model);
	}

	@RequestMapping(value="/perform-search.htm")
	public ModelAndView performSearch (HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//get cookies
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
		Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
		
		//Use the task index to get the task id from the task order
		String taskId = taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue())).getIds().get(
							Integer.parseInt(taskIndexCookie.getValue()));
		
		//perform search
		String searchQuery = request.getParameter("searchQuery");
		if (searchQuery!=null) {
			searchQuery = searchQuery.trim();
			if (logger.isDebugEnabled())
				logger.debug("request pageNum: " + request.getParameter("pageNum"));
			
			int pageNum = 1;
			if (request.getParameter("pageNum") != null && Integer.parseInt(request.getParameter("pageNum")) >= 1)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			
			if (logger.isDebugEnabled())
				logger.debug("processing search query: " + searchQuery +
						     "\npageNum: " + pageNum + " startIndex: " + (pageNum-1)*this.getResultsPerPage() +
						     " endIndex: " + pageNum*this.getResultsPerPage());
			
			//calculate number of results to process
			int numResults = pageNum * this.getResultsPerPage();
			if (numResults > this.getResultsLimit()) {
			    numResults = this.getResultsLimit();
			}
			
			SearchInfo searchInfo = searchService.search(searchQuery, numResults);
			List<SearchResult> allResults = searchInfo.getResults();
			List<SearchResult> searchResults = new ArrayList<SearchResult>();
			for(int i = (pageNum-1)*this.getResultsPerPage(); i >= 0 && i < allResults.size() && i < pageNum*this.getResultsPerPage(); i++){
				searchResults.add(allResults.get(i));
			}
			model.put("searchResults", searchResults);
			model.put("pageNum", pageNum);
			model.put("numPages", (int) Math.ceil(( ((double)searchInfo.getTotalHits()) / ((double)this.getResultsPerPage()) )));
			model.put("startIndex", (pageNum-1)*this.getResultsPerPage());
			model.put("prevDisable", pageNum == 1? "DISABLED": "");
			int maxHits = searchInfo.getTotalHits();
			if (maxHits > this.getResultsLimit()) {
			    maxHits = this.getResultsLimit();
			}
			model.put("nextDiable", (pageNum*this.getResultsPerPage() >= maxHits)? "DISABLED": "");
			model.put("searchQuery", searchQuery);
			
			//log if pagination was used
			HashMap<String,String> interactionData = new HashMap<String,String>();
			interactionData.put("page", "perform-search.htm");
			interactionData.put("pageNum",request.getParameter("pageNum"));
			interactionData.put("searchQuery", searchQuery);
			this.getInteractionLogger().logData(userIdCookie.getValue(), 
					                            taskId, 
					                            taskIndexCookie.getValue(), 
					                            interactionData);
			
			//log search query and number of results
			HashMap<String,String> searchData = new HashMap<String,String>();
			searchData.put("searchQuery", searchQuery);
			this.getSearchQueryLogger().logData(userIdCookie.getValue(), 
					                            taskId, 
					                            taskIndexCookie.getValue(), 
					                            searchData);
		}
		
		return new ModelAndView(this.getSearchResultsView(), "model", model);
	}
	
	@RequestMapping(value="/start_task.htm")
	public void startTask (HttpServletRequest request) {
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "start_task.htm");
		this.logInteraction(request, interactionData);
	}
	
	@RequestMapping(value="/end_task.htm")
	public void endTask (HttpServletRequest request) {
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "end_task.htm");
		this.logInteraction(request, interactionData);
	}
	
	private void logInteraction (HttpServletRequest request, Map<String,String> logData){
		//get cookies
		Cookie userIdCookie = WebUtils.getCookie(request, this.getUserIdCookieName());
		Cookie taskOrderCookie = WebUtils.getCookie(request, this.getTaskOrderCookieName());
		Cookie taskIndexCookie = WebUtils.getCookie(request, this.getTaskIndexCookieName());
		
		//Use the task index to get the task id from the task order
		String taskId = taskService.getTaskOrder(Integer.parseInt(taskOrderCookie.getValue())).getIds().get(
							Integer.parseInt(taskIndexCookie.getValue()));
		
		this.getInteractionLogger().logData(userIdCookie.getValue(), 
					                            taskId, 
					                            taskIndexCookie.getValue(), 
					                            logData);
	}
	
	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public int getResultsLimit() {
		return resultsLimit;
	}

	public void setResultsLimit(int resultsLimit) {
		this.resultsLimit = resultsLimit;
	}

	public String getSearchView() {
		return searchView;
	}

	public void setSearchView(String searchView) {
		this.searchView = searchView;
	}

	public String getVocalSearchView() {
		return vocalSearchView;
	}

	public void setVocalSearchView(String vocalSearchView) {
		this.vocalSearchView = vocalSearchView;
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

	public UserLoggerService getSearchQueryLogger() {
		return searchQueryLogger;
	}

	public void setSearchQueryLogger(UserLoggerService searchQueryLogger) {
		this.searchQueryLogger = searchQueryLogger;
	}

	public String getSearchResultsView() {
		return searchResultsView;
	}

	public void setSearchResultsView(String searchResultsView) {
		this.searchResultsView = searchResultsView;
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public UserLoggerService getInteractionLogger() {
		return interactionLogger;
	}

	public void setInteractionLogger(UserLoggerService interactionLogger) {
		this.interactionLogger = interactionLogger;
	}
}
