<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userDetail.title"/></title>
<content tag="heading"><c:out value="${user.fullName}"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="userRegistrationList.item" var="item"/>
<fmt:message key="userRegistrationList.items" var="items"/>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="user.firstName"/>
        </th>
        <td>
	    	<c:out value="${user.firstName}"/>
        </td>
    </tr>
    
    <tr>
        <th>
            <fmt:message key="user.lastName"/>
        </th>
        <td>
	    	<c:out value="${user.lastName}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="user.email"/>
        </th>
        <td>
          	<a href="<c:out value="mailto:${user.email}"/>"><c:out value="${user.email}"/></a>
        </td>
    </tr>

<c:if test="${useBirthdateForUser}">
    <tr>
        <th>
            <fmt:message key="user.birthdate"/>
        </th>
        <td>
			<fmt:formatDate value="${user.birthdate}" type="date" pattern="${dateformat}"/>
        </td>
    </tr>
</c:if>

<c:if test="${showJobTitle}">
    <tr>
        <th>
            <fmt:message key="user.jobTitle"/>
        </th>
        <td>
	    	<c:out value="${user.jobTitle}"/>
        </td>
    </tr>
</c:if>

<c:if test="${showWorkplace}">
    <tr>
        <th>
            <fmt:message key="user.workplace"/>
        </th>
        <td>
	    	<c:out value="${user.workplace}"/>
        </td>
    </tr>
</c:if>

<c:if test="${showServiceArea}">
    <tr>
        <th>
            <fmt:message key="user.servicearea"/>
        </th>
        <td>
	    	<c:out value="${user.serviceArea.name}"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="user.organization"/>
        </th>
        <td>
          	<c:out value="${user.organization.name}"/>
        </td>
    </tr>

<c:if test="${useOrganization2 && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
    <tr>
        <th>
            <fmt:message key="user.organization2"/>
        </th>
        <td>
        <c:if test="${user.organization2 != null}">
          	<c:out value="${user.organization2.name}"/>
        </c:if>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="user.phoneNumber"/>
        </th>
        <td>
	    	<c:out value="${user.phoneNumber}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="user.mobilePhone"/>
        </th>
        <td>
	    	<c:out value="${user.mobilePhone}"/>
        </td>
    </tr>

    <tr>
        <td class="buttonBar">            
            <c:if test="${isAdmin || user.username == username || user.username == altusername}">
            	<button type="button" onclick="location.href='<c:url context="${urlContext}" value="/editUser.html"><c:param name="username" value="${user.username}"/></c:url>'">
    	            <fmt:message key="button.edit"/>
	            </button>
            </c:if>
        </td>
    </tr>
</table>

<c:if test="${!isSVV && ((isAdmin && user.hashuser) || (isReader && user.hashuser) || user.username == username || user.username == altusername)}">
<display:table name="${userRegistrations}" pagesize="${itemCount}" id="userRegistrations" class="list" requestURI="detailsUser.html">

    <display:column property="firstName" sortable="true" headerClass="sortable"
         titleKey="registration.firstName"/>
         
    <display:column property="lastName" sortable="true" headerClass="sortable"
         titleKey="registration.lastName"/>
    
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="course.name">
        <c:if test="${userRegistrations.course.status == 3}"><img src="<c:url context="${urlContext}" value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${userRegistrations.course.id}"/></c:url>"
         title="<c:out value="${userRegistrations.course.description}"/>"><c:out value="${userRegistrations.course.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>
    
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime">
         <fmt:formatDate value="${userRegistrations.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column property="course.organization.name" sortable="true" headerClass="sortable" titleKey="course.organization"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url context="${urlContext}" value="/detailsLocation.html"><c:param name="id" value="${userRegistrations.course.location.id}"/></c:url>" title="<c:out value="${userRegistrations.course.location.description}"/>"><c:out value="${userRegistrations.course.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url context="${urlContext}" value="/detailsUser.html"><c:param name="username" value="${userRegistrations.course.responsible.username}"/></c:url>"><c:out value="${userRegistrations.course.responsible.fullName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="course.responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:column property="course.instructor.name" sortable="true" headerClass="sortable" titleKey="course.instructor.export"/>

	<c:if test="${!isSVV}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.reserved">
        <input type="hidden" name="_reserved<c:out value="${userRegistrations.id}"/>" value="visible"/>
        <input type="hidden" name="reserved_<c:out value="${userRegistrations.id}"/>" value="${registrationList.reserved}" />
        <c:if test="${userRegistrations.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${userRegistrations.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="registration.reserved">
        <c:if test="${userRegistrations.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${userRegistrations.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
	</c:if>

	<c:if test="${isSVV}">
	    <display:column property="participants" sortable="true" headerClass="sortable" titleKey="registration.participants"/>
	</c:if>

    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <button type="button" onclick="location.href='<c:url context="${urlContext}" value="/performRegistration.html"><c:param name="id" value="${userRegistrations.id}"/><c:param name="courseId" value="${userRegistrations.course.id}"/></c:url>'">
            <fmt:message key="button.edit"/>
        </button>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

</display:table>
</c:if>
