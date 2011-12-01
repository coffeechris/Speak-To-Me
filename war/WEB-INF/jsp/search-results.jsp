<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:if test="${model.searchResults != null}">
	<c:forEach items="${model.searchResults}" var="searchResult" varStatus="status">
	<p style="border-top-style:solid;">
		<c:out value="${status.index + 1 + model.startIndex}"/>)
		<a href="view_document.htm?id=<c:out value="${searchResult.docID}"/>&s=0" target="_blank">
			<c:out value="${searchResult.title}"/> 
		</a>
		<!-- <i><c:out value="${searchResult.score}"/></i> -->
		&nbsp;&nbsp;&nbsp;&nbsp;
		<button onclick="saveDoc('<c:out value="${searchResult.docID}"/>','<c:out value="${searchResult.title}"/>')">
		&nbsp;&nbsp;&nbsp;&nbsp;Save&nbsp;&nbsp;&nbsp;&nbsp;
		</button><br />
		<c:out value="${searchResult.gist}" />...
	</p>
	</c:forEach>
</c:if>

<p align="center">
<button onclick="performSearch(<c:out value="${model.pageNum - 1}"/>)" <c:out value="${model.prevDisable}"/>>Prev</button>
<button onclick="performSearch(<c:out value="${model.pageNum + 1}"/>)" <c:out value="${model.nextDiable}"/>>Next</button>
Result page ${model.pageNum} of ${model.numPages}
</p>