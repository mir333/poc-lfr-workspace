<%@ include file="init.jsp" %>

<portlet:actionURL name="sendEvent" var="actionURL">
	<portlet:param name="testValue" value="This is form portletA" />
</portlet:actionURL>

<a href="${actionURL}">click me</a>
