<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationCancel.title" />
</title>
<content tag="heading">
    <fmt:message key="registrationCancel.heading" />
</content>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />

<c:if test="${cancel.ask_for_confirmation == true}">
    <form method="post" action="<c:url context="${urlContext}" value="/cancelRegistration.html"/>" name="form" id="cform">
        <input type="hidden" name="rid" value="<c:out value="${cancel.rid}"/>"/>
        <input type="hidden" name="hash" value="<c:out value="${cancel.hash}"/>"/>
        <input type="hidden" name="confirm" value="false"/>
        <div class="message" style="font-size: 12px"><fmt:message key="registrationCancel.confirmcancellation" /></div>
        <input type="submit" class="button" style="width: 60px" onclick="document.form.confirm.value=true;document.form.submit()" value=" <fmt:message key="button.yes"/> " />
        &nbsp;
        <input type="submit" class="button" style="width: 60px" value=" <fmt:message key="button.no"/> " />
    </form>
</c:if>
<c:if test="${cancel.completed == true}">
    <div class="message" style="font-size: 12px"><fmt:message key="registrationCancel.completed" /></div>
</c:if>
<c:if test="${cancel.abort == true}">
    <div class="message" style="font-size: 12px"><fmt:message key="registrationCancel.abort" /></div>
</c:if>

<h4>
    <fmt:message key="registrationCancel.yourdetails" />
</h4>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="course.name" />
        </th>
        <td>
            <c:out value="${cancel.coursename}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.firstName" />
        </th>
        <td>
            <c:out value="${cancel.firstname}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.lastName" />
        </th>
        <td>
            <c:out value="${cancel.lastname}" />
        </td>
    </tr>
    <tr>
        <th>
            <fmt:message key="registration.email" />
        </th>
        <td>
            <c:out value="${cancel.email}" />
        </td>
    </tr>
</table>
