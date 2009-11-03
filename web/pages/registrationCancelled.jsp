<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationCancelled.title" />
</title>
<content tag="heading">
<fmt:message key="registrationCancelled.heading" />
</content>

<div class="message" style="font-size: 12px">
    <fmt:message key="registrationCancelled.completed" />
</div>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />

<h4>
    <fmt:message key="registrationCancelled.yourdetails" />
</h4>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="course.name" />
        </th>
        <td>
            <c:out value="${cancellation.coursename}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.firstName" />
        </th>
        <td>
            <c:out value="${cancellation.firstname}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.lastName" />
        </th>
        <td>
            <c:out value="${cancellation.lastname}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.email" />
        </th>
        <td>
            <c:out value="${cancellation.email}" />
        </td>
    </tr>

</table>
