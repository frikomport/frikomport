<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseDetail.title" /></title>
<content tag="heading">
<fmt:message key="courseDetail.heading" />
</content>

<spring:bind path="course.*">
    <c:if test="${not empty status.errorMessages}">
        <div class="error"><c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>" alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false" />
            <br />
        </c:forEach></div>
    </c:if>
</spring:bind>

<form method="post" action="<c:url value="/notifyCourse.html"/>" id="courseForm" onsubmit="return validateCourse(this)">

<fmt:message key="date.format" var="dateformat" /> 
<fmt:message key="time.format" var="timeformat" /> 
<fmt:message key="attachmentList.item" var="item" /> 
<fmt:message key="attachmentList.items" var="items" /> 
<spring:bind path="course.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
</spring:bind>

	<input type="hidden" name="waitinglist" value="<c:out value="${waitinglist}"/>" />

<c:if test="${enableMail}">

    <c:choose>
        <c:when test="${!waitinglist}">
            <div class="message" style="font-size: 12px"><fmt:message key="courseNotification.askmailnotification" />
            </div>
        </c:when>
        <c:otherwise>
            <div class="message" style="font-size: 12px"><fmt:message key="courseNotification.askwaitinglistmail" />
            </div>
        </c:otherwise>

    </c:choose>

    <h4><fmt:message key="courseNotification.mailheading" /></h4>
    <table class="detail">
        <tr>
            <th><soak:label key="course.mailcomment" /></th>
            <td><textarea cols="50" rows="3" name="mailcomment" id="mailcomment"></textarea></td>
        </tr>
        <tr>
            <th><soak:label key="course.mailsender" /></th>
            <td><select name="mailsender">
                <c:forEach var="sender" items="${mailsenders}">
                    <option value="<c:out value="${sender}"/>"><c:out value="${sender}" /></option>
                </c:forEach>
            </select></td>
        </tr>
        <tr>
            <td colspan="2" class="buttonBar">
                <c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && username == course.responsibleUsername)}">
                <button type="button"
                    onclick="location.href='<c:url value="/editCourse.html"><c:param name="id" value="${course.id}"/></c:url>'">
                <fmt:message key="button.reedit" /></button>
                </c:if> 
                <input type="submit" class="button" name="skip" onclick="bCancel = true;" value="<fmt:message key="button.skipmail"/>" /> 
                <input type="submit" class="button" name="send" onclick="bCancel = false;" value="<fmt:message key="button.sendmail"/>" />
            </td>
        </tr>
    </table>
</c:if> 
<c:if test="${!enableMail}">
    <c:if test="${!newCourse}">
        <div class="message" style="font-size: 12px"><fmt:message key="courseNotification.nomailnotification" /></div>
    </c:if>

    <h4><fmt:message key="courseNotification.confirmheading" /></h4>
    <table class="detail">
        <tr>
            <td colspan="2" class="buttonBar">
                <c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible)}">
                <button type="button"
                    onclick="location.href='<c:url value="/editCourse.html"><c:param name="id" value="${course.id}"/></c:url>'">
                <fmt:message key="button.reedit" /></button>
                </c:if> 
                <input type="submit" class="button" name="confirm" onclick="bCancel = true;" value="<fmt:message key="button.continue"/>" /> 
            </td>
        </tr>
    </table>

    <h4><fmt:message key="courseNotification.coursedetails" /></h4>

    <table class="detail">

        <jsp:include page="course.jsp">
            <jsp:param name="course" value="${course}" />
        </jsp:include>

    </table>
</c:if>
</form>

<v:javascript formName="course" cdata="false" dynamicJavascript="true" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
