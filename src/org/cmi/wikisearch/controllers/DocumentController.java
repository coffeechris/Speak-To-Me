package org.cmi.wikisearch.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.cmi.wikisearch.services.DocumentService;
import org.cmi.wikisearch.services.TaskService;
import org.cmi.wikisearch.services.UserLoggerService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@Controller
public class DocumentController {

	private String docView;
	
	private String userIdCookieName;
	private String taskOrderCookieName;
	private String taskIndexCookieName;
	
	private DocumentService documentService;
	private TaskService taskService;
	
	private UserLoggerService interactionLogger;
	
	@RequestMapping(value="/view_document.htm")
	public ModelAndView viewDocument (HttpServletRequest request, String id, @RequestParam(value="s", required=false) Integer s) {	
		ModelAndView modelAndView = new ModelAndView(this.getDocView());
		//the link is from the search interface
		if (s!=null)
			modelAndView.addObject("documentString", this.getDocumentService().documentString(id));
		//the link is from the document viewer
		//TODO: check this to some sort of configuration
		else {
			int docID = Integer.parseInt(id.substring(0,id.length()-4));
			if (docID <= 60845) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-0/"+id));
			}
			else if (docID <= 117783) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-30000/"+id));
			}
			else if (docID <= 161895) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-60000/"+id));
			}
			else if (docID <= 250230) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-90000/"+id));
			}
			else if (docID <= 354256) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-120000/"+id));
			}
			else if (docID <= 457809) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-150000/"+id));
			}
			else if (docID <= 573040) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-180000/"+id));
			}
			else if (docID <= 698138) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-210000/"+id));
			}
			else if (docID <= 858340) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-240000/"+id));
			}
			else if (docID <= 988976) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-270000/"+id));
			}
			else if (docID <= 1124244) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-300000/"+id));
			}
			else if (docID <= 1271886) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-330000/"+id));
			}
			else if (docID <= 1445593) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-360000/"+id));
			}
			else if (docID <= 1618655) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-390000/"+id));
			}
			else if (docID <= 1793449) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-420000/"+id));
			}
			else if (docID <= 1963434) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-450000/"+id));
			}
			else if (docID <= 2150353) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-480000/"+id));
			}
			else if (docID <= 2328628) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-510000/"+id));
			}
			else if (docID <= 2541307) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-540000/"+id));
			}
			else if (docID <= 2770824) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-570000/"+id));
			}
			else if (docID <= 3011303) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-600000/"+id));
			}
			else if (docID <= 3251127) {
				modelAndView.addObject("documentString", this.getDocumentService().documentString("wikipedia_unzip/part-630000/"+id));
			}
		}
		
		//log which document was viewed
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "view_document.htm");
		interactionData.put("id", id);
		if (s != null)
			interactionData.put("s", s.toString());
		this.logInteraction(request, interactionData);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/close_document.htm")
	public void closeDocument (HttpServletRequest request, String id) {
		//log closure of a document view
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "close_document.htm");
		interactionData.put("id", id);
		this.logInteraction(request, interactionData);
	}
	
	@RequestMapping(value="/add_document.htm")
	public void addDocument (HttpServletRequest request, String id) {
		//log doc add
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "add_document.htm");
		interactionData.put("id", id);
		this.logInteraction(request, interactionData);
	}
	
	@RequestMapping(value="/remove_document.htm")
	public void removeDocument (HttpServletRequest request, String id) {
		//log doc remove
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "remove_document.htm");
		interactionData.put("id", id);
		this.logInteraction(request, interactionData);
	}

	@RequestMapping(value="/start_scroll_document.htm")
	public void startScroll (HttpServletRequest request, String id) {
		//log doc remove
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "start_scroll_document.htm");
		interactionData.put("id", id);
		this.logInteraction(request, interactionData);
	}
	
	@RequestMapping(value="/stop_scroll_document.htm")
	public void stopScroll (HttpServletRequest request, String id) {
		//log doc remove
		HashMap<String,String> interactionData = new HashMap<String,String>();
		interactionData.put("page", "stop_scroll_document.htm");
		interactionData.put("id", id);
		this.logInteraction(request, interactionData);
	}

	@RequestMapping(value="/record_selected_text.htm")
	public void recordSelectedText (HttpServletRequest request, String id, String text) {
		if (text != null && text.trim().length() != 0) {
			//log record selected text
			HashMap<String,String> interactionData = new HashMap<String,String>();
			interactionData.put("page", "record_selected_text.htm");
			interactionData.put("id", id);
			interactionData.put("text", text);
			this.logInteraction(request, interactionData);
		}
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
	
	public String getDocView() {
		return docView;
	}

	public void setDocView(String docView) {
		this.docView = docView;
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

	public DocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public UserLoggerService getInteractionLogger() {
		return interactionLogger;
	}

	public void setInteractionLogger(UserLoggerService interactionLogger) {
		this.interactionLogger = interactionLogger;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
}
