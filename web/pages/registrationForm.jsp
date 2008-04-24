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

	<form method="post" action="<c:url value="/performRegistration.html"/>" id="registrationForm"
	    onsubmit="return validateRegistration(this)">
	<table class="detail">
	
	<spring:bind path="registration.courseid">
		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${course.id}"/>"/> 
	</spring:bind>
	
	<spring:bind path="registration.id">
		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
	</spring:bind>
	
	    <tr>
	        <th>
	            <soak:label key="registration.employeeNumber"/>
	        </th>
	        <td>
	            <spring:bind path="registration.employeeNumber">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.firstName"/>
	        </th>
	        <td>
	            <spring:bind path="registration.firstName">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.lastName"/>
	        </th>
	        <td>
	            <spring:bind path="registration.lastName">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.email"/>
	        </th>
	        <td>
	            <spring:bind path="registration.email">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.phone"/>
	        </th>
	        <td>
	            <spring:bind path="registration.phone">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.mobilePhone"/>
	        </th>
	        <td>
	            <spring:bind path="registration.mobilePhone">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.jobTitle"/>
	        </th>
	        <td>
	            <spring:bind path="registration.jobTitle">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	    
	    <tr>
	        <th>
	            <soak:label key="registration.workplace"/>
	        </th>
	        <td>
	            <spring:bind path="registration.workplace">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.organization"/>
	        </th>
	        <td>
	            <spring:bind path="registration.organizationid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="organization" items="${organizations}">
					      <option value="<c:out value="${organization.id}"/>" 
					      <c:if test="${organization.id == status.value}"> selected="selected"</c:if>>
					        <c:out value="${organization.name}"/>
					      </option>
					    </c:forEach>
					  </select>            
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	
	    <tr>
	        <th>
	            <soak:label key="registration.serviceArea"/>
	        </th>
	        <td>
	            <spring:bind path="registration.serviceareaid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="serviceArea" items="${serviceareas}">
					      <option value="<c:out value="${serviceArea.id}"/>" 
					      <c:if test="${serviceArea.id == status.value}"> selected="selected"</c:if>>
					        <c:out value="${serviceArea.name}"/>
					      </option>
					    </c:forEach>
					  </select>            
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
	        </td>
	    </tr>
	    
	    <tr>
	        <th>
	            <soak:label key="registration.comment"/>
	        </th>
	        <td>
	            <spring:bind path="registration.comment">
	                <textarea cols="50" rows="2" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	            </spring:bind>
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
		            <input type="submit" class="button" name="delete"
		                onclick="bCancel=true;return confirmDelete('<fmt:message key="registrationList.theitem"/>')" 
		                value="<fmt:message key="button.delete"/>" />
				</c:if>
	            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
	                value="<fmt:message key="button.cancel"/>" />        
	        </td>
	    </tr>
	</table>
	</form>
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
