<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
 	<head>
 		<title>Vocal Search Prototype</title>
 		
 		<script type="text/javascript" src="js/prototype-1.6.0.3.js"></script>
 		
 		<script type="text/javascript"> 
var minutes=<c:out value="${model.task.minutes}" />;
var seconds=<c:out value="${model.task.seconds}" />;

var displayClicked = false;
function display(){
	//second underflow 
	if (seconds<=0){ 
		seconds=60;
	    minutes-=1;
	}
	
	//count down
	if (minutes<=-1){ 
	    seconds=0;
	    minutes=0;
	} 
	else 
	    seconds-=1;
    
    //update timer display
    if (seconds <= 9 )
    	document.getElementById("taskTimer").value=minutes+":0"+seconds;
    else
    	document.getElementById("taskTimer").value=minutes+":"+seconds;
    	
    //recursive call for next second
    if (minutes != 0 || seconds != 0)
    	setTimeout("display()",1000);
} 

function saveDoc(id,title) {
	var sd = document.createElement("div");
	sd.setAttribute("id", id+"_saved");
	var link = document.createElement("a");
	link.setAttribute("href", "view_document.htm?id="+id+"&s=1");
	link.setAttribute("target", "_blank");
	link.innerHTML=title;
	sd.appendChild(link);
	
	var span = document.createElement("span");
	span.innerHTML="&nbsp;&nbsp;&nbsp;";
	sd.appendChild(span);
	
	var button = document.createElement("button");
	button.setAttribute("onclick", "removeDoc('"+id+"_saved')");
	button.innerHTML="Remove";
	sd.appendChild(button);
	
	var savedDocuments = document.getElementById("savedDocuments");
	savedDocuments.insertBefore(sd,savedDocuments.firstChild);
	
	//send ajax request to log user action
	new Ajax.Request('add_document.htm', {
		method: 'get',
		parameters: {id: id}
	});
}

function removeDoc(id) {
	var d = document.getElementById(id);
	d.parentNode.removeChild(d);
	
	//send ajax request to log user action
	new Ajax.Request('remove_document.htm', {
		method: 'get',
		parameters: {id: id}
	});
}

function performSearch(pNum) {
	var searchResults = $('searchResults');
	searchResults.innerHTML = '<div align="center" ><img src="images/loading_animation2.gif" /></div>';
	var query = document.getElementById('searchQuery').value;
	new Ajax.Request('perform-search.htm', {
		method: 'get',
		parameters: {searchQuery: query, pageNum: pNum},
	  	onSuccess: function(transport) {
	    	var searchResults = $('searchResults');
	    	searchResults.innerHTML = transport.responseText;
		}
	});
}

function startTask() {
	new Ajax.Request('start_task.htm');
}

function endTask() {
	new Ajax.Request('end_task.htm');
}
		</script> 
	</head>
 	<body>
    	<table style="width: 1250px" border="1">
    	<tr>
    	<td valign="top">
	    	<table style="width: 950px; height: 250px" border="1">
    			<tr>
		    		<td>
		    		Current Task: <br />
		    		<textarea style="width: 100%; height: 75px"><c:out value="${model.task.desc}" /></textarea>
		    		</td>
		    	</tr>
		    	<tr>
    				<td align="right">
    				    Time: <input id="taskTimer" type="text" style="width: 46px"/> 
			    	    <button onclick="document.location='vocal-search-interface.htm?et=1'">End</button> 
			   		</td>
    			</tr>
		    	<tr>
			    	<td>
			    		<strong style="background:yellow">
			    		Please describe your information problem in detail<br />
			    	    The more you say, the better the results are likely to be
			    	    </strong>
			    	</td> 
		    	</tr>
		    	<tr>
		    		<td style="width: 100%; height: 100px">
						<textarea id="searchQuery" style="width: 100%; height: 75px"><c:out value="${model.searchQuery}" /></textarea>
						<div align="right" ><button onclick="if (!displayClicked){startTask();display();displayClicked=true;} performSearch(-1);">Search</button></div>
					</td>
		    	</tr>
	    	</table>
    	</td>
    	<td valign="top">
    		<table style="width: 300px; height: 250px" border="1">
    			<tr>
    				<th>
			    		Saved Documents 
    				</th>
    			</tr>
    			<tr>
			    	<td colspan=2 style="width: 100%; height: 250px">
			    		<div id="savedDocuments" style="overflow:scroll; height: 250px">
			    		</div>	
			    	</td>
    			</tr>
    		</table>
    	</td>
    	</tr>
		</table>
		
		<script type="text/javascript"> 
		//initialize the timer
		if (seconds <= 9 )
 			document.getElementById('taskTimer').value=minutes+':0'+seconds; 
		else
			document.getElementById("taskTimer").value=minutes+":"+seconds;
		</script>
		
		<!-- Search Results -->
		<strong>Results</strong>
		<div id="searchResults">
		</div>
		
 	</body>
</html>