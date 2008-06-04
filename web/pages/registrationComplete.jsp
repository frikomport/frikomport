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
<fmt:message key="access.registration.userdefaults" var="userdefaults"/>

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
            <fmt:message key="registration.organization"/>
        </th>
        <td>
            <spring:bind path="registration.organization.name">
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
            <c:if test="userdefaults == true">
                <button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.onemore"/>
			    </button>
            </c:if>
        </td>
        </form>
    </tr>

</table>

<h4><fmt:message key="registrationComplete.coursedetails"/></h4>

<table class="detail">
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>
</table>

