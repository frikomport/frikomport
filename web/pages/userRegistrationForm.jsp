<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userRegistrationForm.title"/></title>
<content tag="heading"><fmt:message key="userRegistrationForm.heading"/></content>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br/>
        </c:forEach>
    </div>
    </c:if>
</spring:bind>



<form id="userRegistrationForm" action="<c:url value="/profileUser.html" />">
    <fmt:message key="userRegistrationForm.message"/>
    <table class="detail">
    <tr>

    </tr>
    <tr>
        <th>
            <fmt:message key="user.email" />
        </th>
        <td>
            <input type="text" name="email" size="50"/>
            <input type="submit" class="button" name="send" onclick="bCancel=false"
                value="<fmt:message key="button.mail"/>" />
        </td>
    </tr>
    </table>
</form>