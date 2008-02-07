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

<form method="post" action="<c:url value="/notifyCourse.html"/>" id="courseForm"
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
    <tr>
        <th>
            <fmt:message key="course.name"/>
        </th>
        <td>
			<c:choose>
				<c:when test="${not empty course.detailURL}">
		          	<a href="<c:out value="${course.detailURL}"/>" title="<c:out value="${course.description}"/>"><c:out value="${course.name}"/></a>
        		  	<button type="button" onclick="location.href='<c:out value="${course.detailURL}"/>'">
	    	    		<fmt:message key="button.more"/>
				    </button>
				</c:when>
				<c:otherwise>
	          		<c:out value="${course.name}"/>
				</c:otherwise>
			</c:choose>
        </td>
    </tr>
    
    <tr>
        <th>
            <fmt:message key="course.role"/>
        </th>
        <td>
            <spring:bind path="course.role">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    
    <tr>
        <th>
            <fmt:message key="course.type"/>
        </th>
        <td>
            <spring:bind path="course.type">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.startTime"/>
        </th>
        <td>
        	<fmt:formatDate value="${course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.stopTime"/>
        </th>
        <td>
        	<fmt:formatDate value="${course.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.duration"/>
        </th>
        <td>
            <spring:bind path="course.duration">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.municipality"/>
        </th>
        <td>
            <spring:bind path="course.municipality.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.serviceArea"/>
        </th>
        <td>
            <spring:bind path="course.serviceArea">
            	<c:out value="${status.value.name}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.location"/>
        </th>
        <td>
          	<a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${course.location.id}"/></c:url>" title="<c:out value="${course.location.description}"/>"><c:out value="${course.location.name}"/></a>
          	<button type="button" onclick="location.href='<c:url value="/detailsLocation.html"><c:param name="id" value="${course.location.id}"/></c:url>'">
	    	    <fmt:message key="button.details"/>
		    </button>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.responsible"/>
        </th>
        <td>
         <a href="<fmt:message key="course.responsible.urlpart"/>/<c:out value="${course.responsible.url_name}"/>" target="_top"><c:out value="${course.responsible.name}"/></a>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.instructor"/>
        </th>
        <td>
          	<a href="<c:url value="/detailsPerson.html"><c:param name="id" value="${course.instructor.id}"/></c:url>" title="<c:out value="${course.instructor.description}"/>"><c:out value="${course.instructor.name}"/></a>
          	<button type="button" onclick="location.href='<c:url value="/detailsPerson.html"><c:param name="id" value="${course.instructor.id}"/></c:url>'">
	    	    <fmt:message key="button.details"/>
		    </button>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.maxAttendants"/>
        </th>
        <td>
        	<fmt:formatNumber value="${course.maxAttendants}" minFractionDigits="0"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.reservedMunicipal"/>
        </th>
        <td>
        	<fmt:formatNumber value="${course.reservedMunicipal}" minFractionDigits="0"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.feeMunicipal"/>
        </th>
        <td>
        	<fmt:formatNumber value="${course.feeMunicipal}" minFractionDigits="2"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.feeExternal"/>
        </th>
        <td>
        	<fmt:formatNumber value="${course.feeExternal}" minFractionDigits="2"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.registerStart"/>
        </th>
        <td>
        	<fmt:formatDate value="${course.registerStart}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="course.reminder"/>
        </th>
        <td>
        	<fmt:formatDate value="${course.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="course.registerBy"/>
        </th>
        <td>
			<fmt:formatDate value="${course.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="course.freezeAttendance"/>
        </th>
        <td>
        	<fmt:formatDate value="${course.freezeAttendance}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="course.description"/>
        </th>
        <td>
            <spring:bind path="course.description">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

</table>
</form>

<v:javascript formName="course" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>