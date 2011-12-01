<%@ include file="/WEB-INF/jsp/include.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pre-task Questionnaire</title>
</head>
<body>
<h1>Pre-task Questionnaire</h1>
<p>
To come
</p>
<form method="post" action="pre-task.htm">
<p>
User id: <c:out value="${model.userId}" />
</p>
<p>
Task index: <c:out value="${model.taskIndex}" />
</p>
<p>
Task id: <c:out value="${model.taskId}" />
</p>
<input type="hidden" name="q" value="pre-task" />
<input type="submit" value="Submit" />
</form>
</body>
</html>