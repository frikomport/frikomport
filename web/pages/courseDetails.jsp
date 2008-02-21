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

<form method="post" action="<c:url value="/editCourse.html"/>" id="courseForm"
    onsubmit="return validateCourse(this)">
    
<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="attachmentList.item" var="item"/>
<fmt:message key="attachmentList.items" var="items"/>
    
<table class="detail">

    <spring:bind path="course.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
    </spring:bind>

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

    <tr>
        <td colspan="2" class="buttonBar">            

            <input type="submit" class="button" name="return" onclick="bCancel=true"
                value="<fmt:message key="button.return"/>" />

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible || isCourseParticipant}">
            <c:if test="${allowRegistration == true}">
			    <button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.signup"/>
			    </button>
			</c:if>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && eZUserid == course.responsibleid)}">
		    <button type="button" onclick="location.href='<c:url value="/editCourse.html"><c:param name="id" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.edit"/>
		    </button>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
			<button type="button" onclick="location.href='<c:url value="/editCourse.html"><c:param name="copyid" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.copy"/>
		    </button>
</c:if>

<c:choose>
	<c:when test="${isAdmin || isEducationResponsible || isCourseResponsible}">
		    <button type="button" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.administerRegistrations"/>
		    </button>
	</c:when>
	<c:otherwise>
		<c:if test="${isCourseParticipant}">
		    <button type="button" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.displayRegistrations"/>
		    </button>
		</c:if>
	</c:otherwise>
</c:choose>

<c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && eZUserid == course.responsibleid)}">
		    <button type="button" onclick="location.href='<c:url value="/editFileCourse.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.administerFiles"/>
		    </button>
</c:if>
<c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && eZUserid == course.responsibleid)}">
		    <button type="button" onclick="location.href='<c:url value="/emailCourse.html"><c:param name="id" value="${course.id}"/><c:param name="enablemail" value="true"/></c:url>'">
	    	    <fmt:message key="button.mail"/>
		    </button>
</c:if>
        </td>
    </tr>
</table>
</form>

<h4><fmt:message key="attachment.list"/></h4>

<form method="post" id="courseFileListForm" name="courseFileListForm" action="<c:url value="/editCourse.html"/>"
    enctype="multipart/form-data" onsubmit="return validateFileUpload(this)">
    
<display:table name="${attachments}" cellspacing="0" cellpadding="0"
    id="attachmentsList" pagesize="25" class="list"
    export="true" requestURI="">
    
    <display:column property="filename" sortable="true" headerClass="sortable"
         titleKey="attachment.filename"/>
         
    <display:column sortable="true" headerClass="sortable"
         titleKey="attachment.size">
		<fmt:formatNumber value="${attachmentsList.size}" minFractionDigits="0"/>
    </display:column>
    
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
	            <input type="submit" class="button" name="download"
                onclick="document.courseFileListForm.attachmentid.value=<c:out value="${attachmentsList.id}"/>;bCancel=true;" 
                value="<fmt:message key="button.download"/>" />
	</display:column>
	
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<input type="hidden" name="attachmentid" id="attachmentid" value="0"/>
<input type="hidden" name="id" value="<c:out value="${course.id}"/>"/>
</form>

<v:javascript formName="course" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>