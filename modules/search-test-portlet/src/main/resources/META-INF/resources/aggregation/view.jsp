<%@ include file="/init.jsp" %>
<div>
    <c:choose>
        <c:when test="${empty data}">
            <liferay-ui:message  key="not-configured"/>
        </c:when>
        <c:otherwise>
            <c:forEach items="${data}" var="item">
                <p>${item.label} (${item.count})</p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
