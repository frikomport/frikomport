<%@ include file="/common/taglibs.jsp"%>

<c:set var="admin" value="${false}"/>
<c:if test="${isAdmin || isEducationResponsible || (isEventResponsible && ((isSVV && course.organization2id == currentUserForm.organization2id) || course.responsible.username == currentUserForm.username))}">
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
	<c:choose> 
		<c:when test="${registrationList.canceled}">
		<c:set var="tdClass" value="canceled" />
		</c:when>
		<c:otherwise>
			<c:set var="tdClass" value="" />
		</c:otherwise>
	</c:choose>

    <c:if test="${(admin == true || canDelete == true) && allowEditRegistration == true}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <c:choose>
        <c:when test="${!registrationList.canceled}">
	        <a href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseId" value="${registrationList.courseid}"/></c:url>'>
	            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
	        </a>
	        <input src="<c:url value="/images/cross.png"/>" title="<fmt:message key="button.unregister"/>" 
		       alt="<fmt:message key="button.unregister"/>" type="image" value="unregister" name="unregister" 
		       onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmUnregistration()"/>
        </c:when>
        <c:otherwise>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;</c:otherwise>
        </c:choose>

        <c:if test="${isAdmin}">
        <input src="<c:url value="/images/bin.png"/>" title="<fmt:message key="button.delete"/>" alt="<fmt:message key="button.delete"/>" type="image" value="delete" name="delete" onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmDeleteRegistration()"/>
        </c:if>
    </display:column>
    </c:if>

	<display:column property="firstName" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.firstName"/>

	<display:column property="lastName" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.lastName"/>

    <c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column media="html ccsv cexcel cxml cpdf" property="email" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.email"/>
    </c:if>

	 <c:if test="${useBirthdateForRegistration}">
	<display:column sortable="true" headerClass="sortable" titleKey="registration.birthdate" sortProperty="birthdate">
		<fmt:formatDate value="${registrationList.birthdate}" type="date" pattern="${dateformat}"/>
    </display:column>
	</c:if>

	<display:column property="organization.name" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.organization"/>

	<c:if test="${showJobTitle}">
	<display:column property="jobTitle" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.jobTitle"/>
	</c:if>

	<c:if test="${showServiceArea}">		
	<display:column property="serviceArea.name" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.serviceArea.export"/>
	</c:if>

	<c:if test="${showWorkplace}">
	<display:column property="workplace" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.workplace"/>
	</c:if>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
	<display:column property="phone" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.phone"/>

	<display:column property="mobilePhone" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.mobilePhone"/>

	<c:if test="${showWorkplace}">
	<display:column property="comment" sortable="true" headerClass="sortable" class="${tdClass}" titleKey="registration.comment"/>
	</c:if>

	<c:if test="${useParticipants}">
	<display:column property="participants" sortable="true" headerClass="sortable" titleKey="registration.participants" class="${tdClass}"/>
	</c:if>

	<c:if test="${(admin == true || isReader) && usePayment}">
	<display:column media="ccsv cexcel cxml cpdf" property="invoiceName" sortable="true" headerClass="sortable" titleKey="registration.invoiceAddress.name"/>
	<display:column media="ccsv cexcel cxml cpdf" property="invoiceAddress.address" sortable="true" headerClass="sortable" titleKey="registration.invoiceAddress.address"/>
	<display:column media="ccsv cexcel cxml cpdf" property="invoiceAddress.postalCode" sortable="true" headerClass="sortable" titleKey="registration.invoiceAddress.postalCode"/>		
	<display:column media="ccsv cexcel cxml cpdf" property="invoiceAddress.city" sortable="true" headerClass="sortable" titleKey="registration.invoiceAddress.city"/>
	</c:if>

	<c:if test="${usePayment}">	
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
	</c:if>
	
	<c:if test="${!isSVV}">
		<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.reserved">
		<c:if test="${admin == true}">
			<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
			<input type="checkbox" name="reserved_<c:out value="${registrationList.id}"/>"
			<c:if test="${registrationList.status == 2}"> checked="checked" </c:if>
			<c:if test="${registrationList.status == 3}"> disabled </c:if> />
		</c:if>
		<c:if test="${admin == false}">
			<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
			<input type="hidden" name="reserved_<c:out value="${registrationList.id}"/>" value="${registrationList.reserved}" />
			<c:if test="${registrationList.status == 1}"><fmt:message key="registrationList.status.1"/></c:if>
			<c:if test="${registrationList.status == 2}"><fmt:message key="checkbox.checked"/></c:if>
		</c:if>
		</display:column>
		<display:column media="ccsv cexcel cxml cpdf" sortable="true" headerClass="sortable" titleKey="registration.reserved">
			<c:if test="${registrationList.status == 1}"><fmt:message key="registrationList.status.1"/></c:if>
			<c:if test="${registrationList.status == 2}"><fmt:message key="checkbox.checked"/></c:if>
			<c:if test="${registrationList.status == 3}"><fmt:message key="registrationList.status.3"/></c:if>
		</display:column>
		<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.attended">
		<c:if test="${admin == true}">
			<input type="hidden" name="_attended<c:out value="${registrationList.id}"/>" value="visible"/>
			<input type="checkbox" name="attended_<c:out value="${registrationList.id}"/>"
			<c:if test="${registrationList.attended == true}"> checked="checked" </c:if> 
			<c:if test="${registrationList.status == 3}"> disabled </c:if> />
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

	<c:if test="${!isSVV}">
    <display:setProperty name="export.cxml" value="true"/>
    </c:if>
    
</display:table>

<input type="hidden" id="regid" name="regid" value="0"/>

<c:if test="${admin == true && !isSVV}">
<input type="submit" class="button" name="save" 
	onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
</c:if> 

<c:if test="${!isSVV}">
<input type="submit" class="button" name="docancel"	onclick="bCancel=true" 
	value="<fmt:message key="button.cancel"/>" />
</c:if>
<c:if test="${isSVV}">
<input type="submit" class="button" name="docancel"	onclick="bCancel=true" 
	value="<fmt:message key="button.return"/>" />
</c:if>


	
<c:if test="${allowRegistration == true}">
	<button type="button" class="large" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
    	<fmt:message key="button.signup"/>
	</button>
</c:if>
</form>