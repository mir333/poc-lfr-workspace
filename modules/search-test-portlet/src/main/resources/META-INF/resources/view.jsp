<%@ include file="init.jsp" %>

<div>
	<b><liferay-ui:message key="searchtest.caption"/></b>

	<hr/>

	<c:forEach items="${data1}" var="item">
		${item}<br/>
	</c:forEach>

	<hr/>

	<c:forEach items="${data2}" var="item">
		${item}<br/>
	</c:forEach>

	<hr/>

	<c:forEach items="${data3}" var="item">
		${item}<br/>
	</c:forEach>
	<hr/>

	<c:forEach items="${data4}" var="item">
		${item}<br/>
	</c:forEach>
</div>
<div>
	<portlet:renderURL var="up">
		<portlet:param name="sorting" value="true"/>
	</portlet:renderURL>
	<portlet:renderURL var="down">
		<portlet:param name="sorting" value="false"/>
	</portlet:renderURL>
	<aui:button-row>
		<aui:button href="${up}"
		value="up"/>
		<aui:button href="${down}" value="down"/>
	</aui:button-row>
</div>
