<%@ include file="/WEB-INF/jsp/include.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pre-study Questionnaire</title>
</head>
<body>
<h1>Pre-study Questionnaire</h1>
<p>
To come
</p>
<form method="post" action="pre-study.htm">
<p>
User id hidden and automatically generated: <c:out value="${model.userId}" />
</p>
<input type="hidden" name="q" value="pre-study" />
<input type="submit" value="Submit" />
</form>
</body>
</html>