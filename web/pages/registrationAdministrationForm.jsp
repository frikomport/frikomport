<%@ include file="/common/taglibs.jsp"%>

<c:set var="admin" value="${false}"/>
<c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && course.responsible.username == username)}">
	<c:set var="admin" value="${true}"/>
</c:if>

<title><fmt:message key="registrationAdministration.title"/></title>
<content tag="heading">
<c:if test="${admin == true}"><fmt:message key="registrationAdministration.heading"/></c:if>
<c:if test="${admin == false}"><fmt:message key="displayAdministration.heading"/></c:if>
</content>

<!--
<spring:bind path="registrationsBackingObject.*">
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
-->

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="registrationList.item" var="item"/>
<fmt:message key="registrationList.items" var="items"/>

<table class="detail">
	
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}"/>
	</jsp:include>
	
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
</table>

<form method="post" action="<c:url value="/administerRegistration.html"/>" name="registrationAdministrationForm" id="registrationAdministrationForm">

<input type="hidden" name="<c:out value="courseId"/>" value="<c:out value="${course.id}"/>"/>

<display:table name="${registrationsBackingObject.registrations}" cellspacing="0" cellpadding="0"
	pagesize="${itemCount}" class="list"
	export="true" id="registrationList" requestURI="">

    <c:if test="${(admin == true || canDelete == true) && allowEditRegistration == true}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <a href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseId" value="${registrationList.courseid}"/></c:url>'>
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
        <input src="<c:url value="/images/cross.png"/>" title="<fmt:message key="button.unregister"/>" alt="<fmt:message key="button.unregister"/>" type="image" value="unregister" name="unregister" onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmUnregistration();submit()"/>
        <c:if test="${admin == true}">
        <input src="<c:url value="/images/bin.png"/>" title="<fmt:message key="button.delete"/>" alt="<fmt:message key="button.delete"/>" type="image" value="delete" name="delete" onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmDeleteRegistration();submit()"/>
        </c:if>
    </display:column>
    </c:if>
    
	<display:column property="firstName" sortable="true" headerClass="sortable" 
		titleKey="registration.firstName"/>

	<display:column property="lastName" sortable="true" headerClass="sortable" 
		titleKey="registration.lastName"/>

    <c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column media="ccsv cexcel cxml cpdf" property="email" sortable="true" headerClass="sortable" 
        titleKey="registration.email"/>
    </c:if>

	<display:column property="organization.name" sortable="true" headerClass="sortable" 
		titleKey="registration.organization"/>

	<display:column property="jobTitle" sortable="true" headerClass="sortable" 
		titleKey="registration.jobTitle"/>
		
	<display:column property="serviceArea.name" sortable="true" headerClass="sortable" 
		titleKey="registration.serviceArea"/>

	<display:column property="workplace" sortable="true" headerClass="sortable" 
		titleKey="registration.workplace"/>

    <c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
	<display:column property="phone" sortable="true" headerClass="sortable" 
		titleKey="registration.phone"/>

	<display:column property="mobilePhone" sortable="true" headerClass="sortable" 
		titleKey="registration.mobilePhone"/>

	<display:column property="comment" sortable="true" headerClass="sortable"
		titleKey="registration.comment"/>

	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.invoiced">
	<c:if test="${admin == true}">
		<input type="hidden" name="_invoiced<c:out value="${registrationList.id}"/>" value="visible" />
		<input type="checkbox" name="invoiced_<c:out value="${registrationList.id}"/>"
					<c:if test="${registrationList.invoiced == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_invoiced<c:out value="${registrationList.id}"/>" value="visible" />
		<input type="hidden" name="invoiced_<c:out value="${registrationList.id}"/>" value="${registrationList.invoiced}" />
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="ccsv cexcel cxml cpdf" sortable="true" headerClass="sortable" titleKey="registration.invoiced">
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>

	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.reserved">
	<c:if test="${admin == true}">
		<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="checkbox" name="reserved_<c:out value="${registrationList.id}"/>"
		<c:if test="${registrationList.reserved == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="hidden" name="reserved_<c:out value="${registrationList.id}"/>" value="${registrationList.reserved}" />
		<c:if test="${registrationList.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="ccsv cexcel cxml cpdf" sortable="true" headerClass="sortable" titleKey="registration.reserved">
		<c:if test="${registrationList.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
	
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.attended">
	<c:if test="${admin == true}">
		<input type="hidden" name="_attended<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="checkbox" name="attended_<c:out value="${registrationList.id}"/>"
		<c:if test="${registrationList.attended == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_attended<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="hidden" name="attended_<c:out value="${registrationList.id}"/>" value="${registrationList.attended}" />
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="ccsv cexcel cxml cpdf" sortable="true" headerClass="sortable" titleKey="registration.attended.export">
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
    </c:if>

	<display:setProperty name="paging.banner.item_name" value="${item}"/>
	<display:setProperty name="paging.banner.items_name" value="${items}"/>
    <display:setProperty name="export.pdf" value="false"/>
    <display:setProperty name="export.cpdf" value="true"/>
    <display:setProperty name="export.excel" value="false"/>
    <display:setProperty name="export.cexcel" value="true"/>
    <display:setProperty name="export.csv" value="false"/>
    <display:setProperty name="export.ccsv" value="true"/>
    <display:setProperty name="export.xml" value="false"/>
    <display:setProperty name="export.cxml" value="true"/>
</display:table>

<input type="hidden" id="regid" name="regid" value="0"/>

<c:if test="${admin == true}">
<input type="submit" class="button" name="save" 
	onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
</c:if> 

<input type="submit" class="button" name="docancel"	onclick="bCancel=true" 
	value="<fmt:message key="button.cancel"/>" />
	
	<c:if test="${allowRegistration == true}">
		<button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
	    	<fmt:message key="button.signup"/>
		</button>
	</c:if>
</form>