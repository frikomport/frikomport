<%-- Error Messages --%>
<c:if test="${not empty listOfErrorMessages}">
    <div class="error" id="errorMessages">
        <c:forEach var="error" items="${listOfErrorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="listOfErrorMessages" scope="session"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty listOfMessages}">
    <div class="message" id="successMessages">
        <c:forEach var="msg" items="${listOfMessages}">
            <img src="<c:url value="/images/iconInformation.gif"/>"
                alt="<fmt:message key="icon.information"/>" class="icon" />
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="listOfMessages" scope="session"/>
</c:if>
