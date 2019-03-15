<%@ include file="/init.jsp" %>
<div>
    <c:choose>
        <c:when test="${noConfig}">
            <liferay-ui:message  key="not-configured"/>
        </c:when>
        <c:otherwise>
            <portlet:actionURL name="submitForm" var="url" />
            <aui:form action="${url}">
                <aui:input name="bottom"/>
                <aui:input name="top"/>
                <aui:button-row>
                    <aui:button type="submit" value="submit"/>
                </aui:button-row>
            </aui:form>
            <c:forEach items="${data}" var="item">
                <p>${item}</p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
