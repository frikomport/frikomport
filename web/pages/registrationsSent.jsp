<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationsSent.title" />
</title>
<content tag="heading">
<fmt:message key="registrationsSent.heading" />
</content>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />


<c:if test="${sent.success}"> 
	<h4>
	    <fmt:message key="registrationsSent.listSent" />
	</h4>
</c:if>
<c:if test="${!sent.success}"> 
	<h4>
	   <fmt:message key="registrationsSent.listNotSent" />
	</h4>
</c:if>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="course.name" />
        </th>
        <td>
            <c:out value="${sent.coursename}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="course.location.export" />
        </th>
        <td>
            <c:out value="${sent.location}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="course.responsible.export" />
        </th>
        <td>
            <c:out value="${sent.responsible}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="course.instructor.export" />
        </th>
        <td>
            <c:out value="${sent.instructor}" />
        </td>
    </tr>
</table>
