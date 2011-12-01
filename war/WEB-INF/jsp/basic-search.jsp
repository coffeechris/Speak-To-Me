<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
 	<head><title>WikiSearch</title></head>
 	<body>
 		<!-- Search Form -->
    	<h1>WikiSearch</h1>
    	<p>Enter Your Search Query</p>
		<form method="get" action="basic-search.htm">
			<input type="text" name="searchQuery" value="${model.searchQuery}" />
			<input type="submit" value="Submit" />
		</form>
		
		<!-- Search Results -->
		<c:if test="${model.searchResults != null}">
			<c:forEach items="${model.searchResults}" var="searchResult" varStatus="status">
			<p>
				<c:out value="${status.index + 1}"/>)
				<c:out value="${searchResult.title}"/> 
				<i><c:out value="${searchResult.score}"/></i>
			</p>
			</c:forEach>
		</c:if>
 	</body>
</html>