<%@ include file="/common/taglibs.jsp"%>
<c:set var="admin" value="${false}"/>
<c:if test="${isAdmin || isEducationResponsible || (isEventResponsible && ((isSVV && course.organization2id == currentUserForm.organization2id) || course.responsible.username == currentUserForm.username))}">
	<c:set var="admin" value="${true}"/>
</c:if>

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

<form method="post" action="<c:url value="/editCourse.html"/>" id="courseForm" onsubmit="return validateCourse(this)">

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="attachmentList.item" var="item"/>
<fmt:message key="attachmentList.items" var="items"/>

<table class="detail">
    <spring:bind path="course.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
    </spring:bind>

	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>

    <tr>
        <td colspan="2" class="buttonBar">

<c:if test="${!isSVV}">
            <input type="submit" class="button" name="return" onclick="bCancel=true" value="<fmt:message key="button.course.list"/>" />
</c:if>

<c:if test="${isPublished}">
            <c:if test="${allowRegistration == true && isCourseFull == false}">
			    <button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.signup"/>
			    </button>
			</c:if>
</c:if>

<c:if test="${admin == true}">
		    <button type="button" onclick="location.href='<c:url value="/editCourse.html"><c:param name="id" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.edit"/>
		    </button>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
			<button type="button" onclick="location.href='<c:url value="/editCourse.html"><c:param name="copyid" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.copy"/>
		    </button>
</c:if>

<c:choose>
	<c:when test="${(admin == true || isReader) && isPublished}">
		    <button type="button" class="large" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.administerRegistrations"/>
		    </button>
	</c:when>
	<c:otherwise>
		<c:if test="${!course.restricted || admin == true}">
		    <button type="button" class="large" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.displayRegistrations"/>
		    </button>
		</c:if>
	</c:otherwise>
</c:choose>

<c:if test="${admin == true && !isSVV}">
		    <button type="button" class="large" onclick="location.href='<c:url value="/editFileCourse.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
	    	    <fmt:message key="button.administerFiles"/>
		    </button>
</c:if>
<c:if test="${admin == true}">
		    <button type="button" class="large" onclick="location.href='<c:url value="/emailCourse.html"><c:param name="id" value="${course.id}"/><c:param name="enablemail" value="true"/></c:url>'">
	    	    <fmt:message key="button.mails"/>
		    </button>
</c:if>
        </td>
    </tr>
</table>
</form>

<c:if test="${attachments != null && attachments[0] != null}">
<h4><fmt:message key="attachment.list"/></h4>

<form method="post" id="courseFileListForm" name="courseFileListForm" action="<c:url value="/editCourse.html"/>"
    enctype="multipart/form-data" onsubmit="return validateFileUpload(this)">

<display:table name="${attachments}" cellspacing="0" cellpadding="0"
    id="attachmentsList" pagesize="${itemCount}" class="list"
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
</c:if>

<!-- v:javascript formName="course" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"
    src="<c:url value="/scripts/validator.jsp"/>"></script -->
