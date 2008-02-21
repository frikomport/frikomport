<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationComplete.title"/></title>
<content tag="heading"><fmt:message key="registrationComplete.heading"/></content>

<spring:bind path="registration.*">
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

<c:if test="${courseFull == false}">
	<div class="message" style="font-size: 12px"><fmt:message key="registrationComplete.completed"/></div>
</c:if>
<c:if test="${courseFull == true}">
	<div class="message" style="font-size: 12px"><fmt:message key="registrationComplete.waitinglist"/></div>
</c:if>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>

<h4><fmt:message key="registrationComplete.yourdetails"/></h4>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="registration.employeeNumber"/>
        </th>
        <td>
        	<fmt:formatNumber value="${registration.employeeNumber}" minFractionDigits="0"/>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.firstName"/>
        </th>
        <td>
            <spring:bind path="registration.firstName">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.lastName"/>
        </th>
        <td>
            <spring:bind path="registration.lastName">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.email"/>
        </th>
        <td>
            <spring:bind path="registration.email">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.phone"/>
        </th>
        <td>
            <spring:bind path="registration.phone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.mobilePhone"/>
        </th>
        <td>
            <spring:bind path="registration.mobilePhone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.jobTitle"/>
        </th>
        <td>
            <spring:bind path="registration.jobTitle">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.workplace"/>
        </th>
        <td>
            <spring:bind path="registration.workplace">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.municipality"/>
        </th>
        <td>
            <spring:bind path="registration.municipality.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.serviceArea"/>
        </th>
        <td>
            <spring:bind path="registration.serviceArea.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.comment"/>
        </th>
        <td>
            <spring:bind path="registration.comment">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td></td>
		 <form method="post" action="<c:url value="/listCourses.html"/>" id="registrationCompleteForm">

			<spring:bind path="registration.course.id">
				<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
			</spring:bind>
		        
        <td class="buttonBar">            
            <input type="submit" class="button" name="return" onclick="bCancel=true"
                value="<fmt:message key="button.return"/>" />        
        </td>
        </form>
    </tr>

</table>

<h4><fmt:message key="registrationComplete.coursedetails"/></h4>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="course.name"/>
        </th>
        <td>
			<c:choose>
				<c:when test="${not empty registration.course.detailURL}">
		          	<a href="<c:out value="${registration.course.detailURL}"/>" title="<c:out value="${registration.course.description}"/>"><c:out value="${registration.course.name}"/></a>
        		  	<button type="button" onclick="location.href='<c:out value="${registration.course.detailURL}"/>'">
	    	    		<fmt:message key="button.more"/>
				    </button>
				</c:when>
				<c:otherwise>
	          		<c:out value="${registration.course.name}"/>
				</c:otherwise>
			</c:choose>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.type"/>
        </th>
        <td>
            <spring:bind path="registration.course.type">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.startTime"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.stopTime"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.duration"/>
        </th>
        <td>
            <spring:bind path="registration.course.duration">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.municipality"/>
        </th>
        <td>
            <spring:bind path="registration.course.municipality.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.serviceArea"/>
        </th>
        <td>
            <spring:bind path="registration.course.serviceArea.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.location"/>
        </th>
        <td>
          	<a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${registration.course.location.id}"/></c:url>" title="<c:out value="${registration.course.location.description}"/>"><c:out value="${registration.course.location.name}"/></a>
          	<button type="button" onclick="location.href='<c:url value="/detailsLocation.html"><c:param name="id" value="${registration.course.location.id}"/></c:url>'">
	    	    <fmt:message key="button.details"/>
		    </button>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.responsible"/>
        </th>
        <td>
         <a href="<c:url value="/detailsUser.html"><c:param name="id" value="${course.responsible.id}"/></c:url>"><c:out value="${registration.course.responsible.name}"/></a>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.instructor"/>
        </th>
        <td>
          	<a href="<c:url value="/detailsPerson.html"><c:param name="id" value="${registration.course.instructor.id}"/></c:url>" title="<c:out value="${registration.course.instructor.description}"/>"><c:out value="${registration.course.instructor.name}"/></a>
          	<button type="button" onclick="location.href='<c:url value="/detailsPerson.html"><c:param name="id" value="${registration.course.instructor.id}"/></c:url>'">
	    	    <fmt:message key="button.details"/>
		    </button>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.maxAttendants"/>
        </th>
        <td>
        	<fmt:formatNumber value="${registration.course.maxAttendants}" minFractionDigits="0"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.reservedMunicipal"/>
        </th>
        <td>
        	<fmt:formatNumber value="${registration.course.reservedMunicipal}" minFractionDigits="0"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.feeMunicipal"/>
        </th>
        <td>
			<fmt:formatNumber value="${registration.course.feeMunicipal}" minFractionDigits="2"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.feeExternal"/>
        </th>
        <td>
			<fmt:formatNumber value="${registration.course.feeExternal}" minFractionDigits="2"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="course.registerStart"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.registerStart}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="course.reminder"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="course.registerBy"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="course.freezeAttendance"/>
        </th>
        <td>
        	<fmt:formatDate value="${registration.course.freezeAttendance}" type="both" pattern="${dateformat} ${timeformat}"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="course.description"/>
        </th>
        <td>
            <spring:bind path="registration.course.description">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
</table>

