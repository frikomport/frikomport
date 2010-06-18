<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseFile.title"/></title>
<content tag="heading"><fmt:message key="courseFile.heading"/></content>

<spring:bind path="fileUpload.*">
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
<fmt:message key="attachmentList.item" var="item"/>
<fmt:message key="attachmentList.items" var="items"/>

<table class="detail">
    <spring:bind path="course.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
    </spring:bind>

	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>
	
</table>

<form method="post" id="courseFileForm" action="<c:url value="/editFileCourse.html"/>"
    enctype="multipart/form-data" onsubmit="return validateFileUpload(this)">

<table>
	<input type="hidden" name="courseId" value="<c:out value="${course.id}"/>"/>
    <tr>
        <th>
            <soak:label key="courseFile.chosenFile"/>
        </th>
        <td>
        	<spring:bind path="fileUpload.file">
            <input type="file" name="file" id="file" size="50" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <authz:authorize ifAnyGranted="admin,instructor,editor">
            <input type="submit" name="upload" class="button" onclick="bCancel=false"
                value="<fmt:message key="button.upload"/>" />
            </authz:authorize>

            <input type="submit" name="docancel" class="button" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</table>
</form>

<h4><fmt:message key="attachment.list"/></h4>

<form method="post" id="courseFileListForm" name="courseFileListForm" action="<c:url value="/editFileCourse.html"/>"
    enctype="multipart/form-data" onsubmit="return validateFileUpload(this)">
    
<display:table name="${attachmentsBackingObject.attachments}" cellspacing="0" cellpadding="0"
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
        <authz:authorize ifAnyGranted="admin,instructor,editor">
        <input type="submit" class="button" name="delete"
                onclick="document.courseFileListForm.attachmentid.value=<c:out value="${attachmentsList.id}"/>;bCancel=true;" 
                value="<fmt:message key="button.delete"/>" />
        </authz:authorize>
	</display:column>
	
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<input type="hidden" name="attachmentid" id="attachmentid" value="0"/>
<input type="hidden" name="courseId" value="<c:out value="${course.id}"/>"/>
</form>
