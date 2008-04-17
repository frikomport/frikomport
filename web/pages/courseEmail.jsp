<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseDetail.title"/></title>
<content tag="heading"><fmt:message key="courseDetail.heading"/></content>

<spring:bind path="course.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post" action="<c:url value="/emailCourse.html"/>" id="courseForm"
    onsubmit="return validateCourse(this)">
    
<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="attachmentList.item" var="item"/>
<fmt:message key="attachmentList.items" var="items"/>
    
<spring:bind path="course.id">
<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
</spring:bind>

<h4><fmt:message key="courseNotification.mailheading"/></h4>
<table class="detail">
    <tr>
        <th>
            <soak:label key="course.mailcomment"/>
        </th>
        <td>
            <textarea cols="50" rows="3" name="mailcomment" id="mailcomment"></textarea>
        </td>
    </tr>

    <tr>
        <th>
            
        </th>
        <td colspan="2" class="buttonBar">            

            <input type="submit" class="button" name="skip" onclick="bCancel=true"
                value="<fmt:message key="button.skipmail"/>" />

            <input type="submit" class="button" name="send" onclick="bCancel=false"
                value="<fmt:message key="button.sendmail"/>" />
        </td>
    </tr>
</table>
    
<h4><fmt:message key="courseNotification.coursedetails"/></h4>

<table class="detail">

	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>

</table>
</form>

<v:javascript formName="course" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>