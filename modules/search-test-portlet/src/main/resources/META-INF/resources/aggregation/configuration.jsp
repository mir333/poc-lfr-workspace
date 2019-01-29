<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL"/>
<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL"/>

<aui:form action="${configurationActionURL}" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="${configurationRenderURL}"/>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>"/>

	<aui:input name="preferences--structureId--" value="${portletPreferences.getValue('structureId','')}"/>
	<aui:input name="preferences--fieldName--" value="${portletPreferences.getValue('fieldName','')}"/>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>
