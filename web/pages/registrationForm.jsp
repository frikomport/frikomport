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
	            <soak:label key="registration.municipality"/>
	        </th>
	        <td>
	            <spring:bind path="registration.municipalityid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="municipality" items="${municipalities}">
					      <option value="<c:out value="${municipality.id}"/>" 
					      <c:if test="${municipality.id == status.value}"> selected="selected"</c:if>>
					        <c:out value="${municipality.name}"/>
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
            <spring:bind path="course.serviceArea.name">
            	<c:out value="${status.value}"/>
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
            <spring:bind path="course.responsible.name">
            	<a href="<fmt:message key="course.responsiblePreURL"/><c:out value="${course.responsible.first_name}"/>_<c:out value="${course.responsible.last_name}"/>"><c:out value="${status.value}"/></a>
            </spring:bind>
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

<v:javascript formName="registration" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>
