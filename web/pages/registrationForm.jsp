<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationDetail.title"/></title>
<content tag="heading"><fmt:message key="registrationDetail.heading"/></content>

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

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
    
<c:if test="${!illegalRegistration}">
	<div class="message" style="font-size: 12px"><fmt:message key="registrationDetail.responsibility"/></div>
	
	<h4><fmt:message key="registrationComplete.yourdetails"/></h4>

	<div class="message" style="font-size: 12px"><fmt:message key="registrationDetail.emailexplanation"/></div>

	<form:form commandName="registration"  onsubmit="return validateRegistration(this)">
	<table class="detail">
	    <form:hidden path="courseid"/>
        <form:hidden path="id"/>
	    <tr>
	        <th>
	            <soak:label key="registration.employeeNumber"/>
	        </th>
	        <td>
                <form:input path="employeeNumber"/>
                <form:errors cssClass="fieldError" path="employeeNumber"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.firstName"/>
	        </th>
	        <td>
                <form:input path="firstName"/>
                <form:errors cssClass="fieldError" path="firstName"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.lastName"/>
	        </th>
	        <td>
                <form:input path="lastName"/>
                <form:errors cssClass="fieldError" path="lastName"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.email"/>
	        </th>
	        <td>
                <form:input path="email"/>
                <form:errors cssClass="fieldError" path="email"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.phone"/>
	        </th>
	        <td>
                <form:input path="phone"/>
                <form:errors cssClass="fieldError" path="phone"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.mobilePhone"/>
	        </th>
	        <td>
                <form:input path="mobilePhone"/>
                <form:errors cssClass="fieldError" path="mobilePhone"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.jobTitle"/>
	        </th>
	        <td>
                <form:input path="jobTitle"/>
                <form:errors cssClass="fieldError" path="jobTitle"/>
	        </td>
	    </tr>
	    
	    <tr>
	        <th>
	            <soak:label key="registration.workplace"/>
	        </th>
	        <td>
                <form:input path="workplace"/>
                <form:errors cssClass="fieldError" path="workplace"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.organization"/>
	        </th>
	        <td>
                <form:select path="organizationid" items="${organizations}" itemLabel="name" itemValue="id"/>
                <form:errors cssClass="fieldError" path="organizationid"/>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.serviceArea"/>
	        </th>
	        <td>
                <form:select path="serviceareaid" items="${serviceareas}" itemLabel="name" itemValue="id"/>
                <form:errors cssClass="fieldError" path="serviceareaid"/>
	        </td>
	    </tr>
	    
	    <tr>
	        <th>
	            <soak:label key="registration.comment"/>
	        </th>
	        <td>
                <form:textarea path="comment" cols="50" rows="2"/>
                <form:errors cssClass="fieldError" path="comment"/>
	        </td>
   		</tr>
	
	    <tr>
	        <td></td>
	        <td class="buttonBar">            
				<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible || isCourseParticipant}">
	            <input type="submit" class="button" name="save" 
	                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
				</c:if>
				<c:if test="${!empty registration.id}">
                    <c:if test="${isAdmin}">
                    <input type="submit" class="button" name="delete"
		                onclick="bCancel=true;return confirmDeleteRegistration()"
		                value="<fmt:message key="button.delete"/>" />
                    </c:if>
                    <input type="submit" class="button" name="unregister"
                        onclick="bCancel=true;return confirmUnregistration()"
                        value="<fmt:message key="button.unregister"/>" />
				</c:if>
	            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
	                value="<fmt:message key="button.cancel"/>" />        
	        </td>
	    </tr>
	</table>
	</form:form>
</c:if>

<h4><fmt:message key="registrationComplete.coursedetails"/></h4>

<table class="detail">
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>
</table>

<v:javascript formName="registration" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>
