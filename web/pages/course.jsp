<%@ include file="/common/taglibs.jsp"%>
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
            <fmt:message key="course.description"/>
        </th>
        <td>
            <spring:bind path="course.description">
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
         <a href="<c:url value="/detailsUser.html"><c:param name="id" value="${course.responsible.id}"/></c:url>"><c:out value="${course.responsible.name}"/></a>
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

<c:if test="${allowRegistration == true && isCourseFull == true}">
    <tr>
        <td colspan="2">
            <div class="error">
           		<img src="<c:url value="/images/iconWarning.gif"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" />
           		<fmt:message key="errors.courseFull.warning"/><br />
    		</div>
        </td>
    </tr>
</c:if>