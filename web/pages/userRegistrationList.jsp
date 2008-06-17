<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userRegistrationList.title"/></title>
<content tag="heading"><fmt:message key="userRegistrationList.heading"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="userRegistrationList.item" var="item"/>
<fmt:message key="userRegistrationList.items" var="items"/>

<c:choose>
<c:when test="${user != null}">
    <jsp:include page="userDetails.jsp"/>
</c:when>
<%-- ein form her som lar brukeren skrive inn ein epostaddresse--%>
<c:otherwise>
<form commandName="user">
    <fmt:message key="userRegistrationList.message"/>
    <table class="detail">
    <tr>
        
    </tr>
    <tr>
        <th>
            <fmt:message key="user.email" />
        </th>
        <td>
            <input type="text" name="email" size="50"/>
            <input type="submit" class="button" name="send" onclick="bCancel=false"
                value="<fmt:message key="button.sendmail"/>" />
        </td>
    </tr>
    </table>
</form>
</c:otherwise>
</c:choose>

<%--
<c:if test="${user.username == username}">
<display:table name="${userRegistrations}" pagesize="25" id="userRegistrations" class="list" requestURI="listUserRegistrations.html">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
        <c:if test="${userRegistrations.course.status == 3}"><img src="<c:url value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${userRegistrations.course.id}"/></c:url>"
         title="<c:out value="${userRegistrations.course.description}"/>"><c:out value="${userRegistrations.course.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>

    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
         <fmt:formatDate value="${userRegistrations.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column sortable="true" headerClass="sortable" titleKey="course.stopTime" sortProperty="stopTime">
         <fmt:formatDate value="${userRegistrations.course.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column property="course.organization.name" sortable="true" headerClass="sortable" titleKey="course.organization"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${userRegistrations.course.location.id}"/></c:url>" title="<c:out value="${userRegistrations.course.location.description}"/>"><c:out value="${userRegistrations.course.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url value="/detailsUser.html"><c:param name="username" value="${userRegistrations.course.responsible.username}"/></c:url>"><c:out value="${userRegistrations.course.responsible.fullName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:column property="course.instructor.name" sortable="true" headerClass="sortable" titleKey="course.instructor.export"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.reserved">
    <c:if test="${admin == false}">
        <input type="hidden" name="_reserved<c:out value="${userRegistrations.id}"/>" value="visible"/>
        <input type="hidden" name="reserved_<c:out value="${userRegistrations.id}"/>" value="${registrationList.reserved}" />
        <c:if test="${userRegistrations.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${userRegistrations.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </c:if>
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="registration.reserved">
        <c:if test="${userRegistrations.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${userRegistrations.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>

    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="id" value="${userRegistrations.id}"/><c:param name="courseid" value="${registrationList.courseid}"/></c:url>'">
            <fmt:message key="button.edit"/>
        </button>
        <button type="submit" name="unregister"	onclick="document.registrationAdministrationForm.regid.value=<c:out value="${userRegistrations.id}"/>;bCancel=true;return confirmUnregistration()">
            <fmt:message key="button.unregister"/>
        </button>
        <c:if test="${admin == true}">
        <button type="submit" name="delete" onclick="document.registrationAdministrationForm.regid.value=<c:out value="${userRegistrations.id}"/>;bCancel=true;return confirmDeleteRegistration()">
            <fmt:message key="button.delete"/>
        </button>
        </c:if>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

</display:table>
</c:if> --%>