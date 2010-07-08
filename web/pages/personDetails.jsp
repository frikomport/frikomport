<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>

<title><fmt:message key="personDetail.title"/></title>
<content tag="heading"><fmt:message key="personDetail.heading"/></content>

<spring:bind path="person.*">
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

<form method="post" action="<c:url value="/editPerson.html"/>" id="personForm" onsubmit="return validatePerson(this)">
<table class="detail">

    <spring:bind path="person.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
    </spring:bind>

    <tr>
        <th>
            <fmt:message key="person.name"/>
        </th>
        <td>
			<c:choose>
				<c:when test="${not empty person.detailURL}">
		          	<a class="external" href="<c:out value="${person.detailURL}"/>" target="_blank" title="<c:out value="${person.description}"/>"><c:out value="${person.name}"/></a>
				</c:when>
				<c:otherwise>
	          		<c:out value="${person.name}"/>
				</c:otherwise>
			</c:choose>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.email"/>
        </th>
        <td>
          	<a href="<c:out value="mailto:${person.email}"/>"><c:out value="${person.email}"/></a>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <tr>
        <th>
            <fmt:message key="person.phone"/>
        </th>
        <td>
            <spring:bind path="person.phone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.mobilePhone"/>
        </th>
        <td>
            <spring:bind path="person.mobilePhone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.mailAddress"/>
        </th>
        <td>
            <spring:bind path="person.mailAddress">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="person.description"/>
        </th>
        <td>
            <spring:bind path="person.description">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <tr>
        <th>
            <fmt:message key="person.selectable"/>
        </th>
        <td>
            <spring:bind path="person.selectable">
				<c:if test="${status.value == true}"><fmt:message key="checkbox.checked"/></c:if>
				<c:if test="${status.value == false}"><fmt:message key="checkbox.unchecked"/></c:if>
            </spring:bind>
        </td>
    </tr>
</c:if>

    <tr>
        <td class="buttonBar">            
            <input type="submit" class="button large" name="return" onclick="bCancel=true"
                value="<fmt:message key="button.person.list"/>" />
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
		    <button type="button" onclick="location.href='<c:url value="/editPerson.html"><c:param name="id" value="${person.id}"/></c:url>'">
	    	    <fmt:message key="button.edit"/>
		    </button>
</c:if>
        </td>
    </tr>
</table>
</form>

<c:if test="${courseList != null}">
<h1><fmt:message key="course.related"/></h1>
<display:table name="${courseList}" cellspacing="0" cellpadding="0"
    id="courseList" pagesize="${itemCount}" class="list"
    export="true" requestURI="detailsLocation.html">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
        <c:if test="${courseList.status == 3}"><img src="<c:url value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>"
         title="<c:out value="${courseList.description}"/>"><c:out value="${courseList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>

    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
         <fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>


	<c:if test="${!isSVV}">
    <display:column sortable="true" headerClass="sortable" titleKey="course.stopTime" sortProperty="stopTime">
         <fmt:formatDate value="${courseList.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
	</c:if>

	<c:if test="${showDuration}">
    <display:column property="duration" sortable="true" headerClass="sortable" titleKey="course.duration"/>
	</c:if>
	
    <display:column property="organization.name" sortable="true" headerClass="sortable" titleKey="course.organization"/>

	<c:if test="${useOrganization2}">
    <display:column property="organization2.name" sortable="true" headerClass="sortable" titleKey="course.organization2"/>
	</c:if>

	<c:if test="${useServiceArea}">
    <display:column property="serviceArea.name" sortable="true" headerClass="sortable" titleKey="course.serviceArea"/>
	</c:if>
	
	<c:if test="${!isSVV}">
    <display:column media="csv excel xml pdf" property="type" sortable="true" headerClass="sortable" titleKey="course.type.export"/>
	</c:if>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out value="${courseList.responsible.fullName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

</display:table>
</c:if>

<v:javascript formName="person" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>