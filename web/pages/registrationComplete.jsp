<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationComplete.title" /></title>
<content tag="heading"><fmt:message key="registrationComplete.heading" /></content>

<spring:bind path="registration.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url context="${urlContext}" value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />

<h4>
	<fmt:message key="registrationComplete.yourdetails" />
</h4>

<table class="detail">
	<tr>
		<th>
			<fmt:message key="course.name" />
		</th>
		<td>
			<c:out value="${registration.course.name}" escapeXml="false" />, <c:out value="${registration.course.location.name}"/> - <fmt:formatDate value="${registration.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
		</td>
	</tr>

<c:if test="${showEmployeeFields}">
	<tr>
		<th>
			<fmt:message key="registration.employeeNumber" />
		</th>
		<td>
			<fmt:formatNumber value="${registration.employeeNumber}"
				minFractionDigits="0" />
		</td>
	</tr>
</c:if>

	<tr>
		<th>
			<fmt:message key="registration.firstName" />
		</th>
		<td>
			<spring:bind path="registration.firstName">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="registration.lastName" />
		</th>
		<td>
			<spring:bind path="registration.lastName">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="registration.email" />
		</th>
		<td>
			<spring:bind path="registration.email">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>

<c:if test="${useBirthdateForRegistration}">
	<tr>
		<th>
			<fmt:message key="registration.birthdate" />
		</th>
		<td>
			<spring:bind path="registration.birthdate">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

<c:if test="${useAttendants}">
	<tr>
		<th>
			<fmt:message key="registration.participants" />
		</th>
		<td>
			<spring:bind path="registration.participants">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

<c:if test="${!isSVV}">
	<tr>
		<th>
			<fmt:message key="registration.phone" />
		</th>
		<td>
			<spring:bind path="registration.phone">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>	

	<tr>
		<th>
			<fmt:message key="registration.mobilePhone" />
		</th>
		<td>
			<spring:bind path="registration.mobilePhone">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>

<c:if test="${showEmployeeFields}">
	<tr>
		<th>
			<fmt:message key="registration.closestLeader" />
		</th>
		<td>
			<spring:bind path="registration.closestLeader">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

<c:if test="${showJobTitle}">
	<tr>
		<th>
			<fmt:message key="registration.jobTitle" />
		</th>
		<td>
			<spring:bind path="registration.jobTitle">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

<c:if test="${showWorkplace}">
	<tr>
		<th>
			<fmt:message key="registration.workplace" />
		</th>
		<td>
			<spring:bind path="registration.workplace">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

	<c:if test="${!isSVV}">
	<tr>
		<th>
			<fmt:message key="registration.organization" />
		</th>
		<td>
			<c:if test="${registration.organization != null}">
				<spring:bind path="registration.organization.name">
					<c:out value="${status.value}" />
				</spring:bind>
			</c:if>
		</td>
	</tr>
	</c:if>

<c:if test="${showServiceArea}">
	<tr>
		<th>
			<fmt:message key="registration.serviceArea" />
		</th>
		<td>
			<c:if test="${registration.serviceArea != null}">
				<spring:bind path="registration.serviceArea.name">
					<c:out value="${status.value}" />
				</spring:bind>
			</c:if>
		</td>
	</tr>
</c:if>

<c:if test="${showComment}">
	<tr>
		<th>
			<fmt:message key="registration.comment" />
		</th>
		<td>
			<spring:bind path="registration.comment">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
</c:if>

<c:if test="${!freeCourse || isSVV}">
	<c:if test="${!isSVV}">
	<tr>
		<th>
			<soak:label key="registration.invoiceAddress" />
		</th>
	</tr>
	<tr>
		<th>
			<fmt:message key="registration.invoiceAddress.name" />
		</th>
		<td>
			<spring:bind path="registration.invoiceName">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
	</c:if>	
	
	<tr>
		<th>
			<fmt:message key="registration.invoiceAddress.address" />
		</th>
		<td>
			<spring:bind path="registration.invoiceAddress.address">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th>
			<fmt:message key="registration.invoiceAddress.postalCode" />
		</th>
		<td>
			<spring:bind path="registration.invoiceAddress.postalCode">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>

	<c:if test="${!isSVV}">
	<tr>
		<th>
			<fmt:message key="registration.invoiceAddress.city" />
		</th>
		<td>
			<spring:bind path="registration.invoiceAddress.city">
				<c:out value="${status.value}" />
			</spring:bind>
		</td>
	</tr>
	</c:if>

</c:if>

	<tr>
		<th>
			<fmt:message key="registration.organization" />
		</th>
		<td>
			<c:if test="${registration.organization != null}">
				<spring:bind path="registration.organization.name">
					<c:out value="${status.value}" />
				</spring:bind>
			</c:if>
		</td>
	</tr>


	<tr>
		<td class="buttonBar">
		<form method="post" action="<c:url context="${urlContext}" value="/listCourses.html"/>"
			id="registrationCompleteForm">

			<spring:bind path="registration.course.id">
				<input type="hidden" name="<c:out value="${status.expression}"/>"
					value="<c:out value="${status.value}"/>" />
			</spring:bind>

<c:if test="${!isSVV}">
			<input type="submit" class="button" name="return" onclick="bCancel=true" value="<fmt:message key="button.course.list"/>" />
</c:if>

			<c:if test="${userdefaults == true}">
				<button type="button"
					onclick="location.href='<c:url context="${urlContext}" value="/performRegistration.html"><c:param name="courseId" value="${course.id}"/></c:url>'">
					<fmt:message key="button.onemore" />
				</button>
			</c:if>
		</form>
		</td>
		<td></td>
	</tr>

</table>
<%-- 

<h4>
	<fmt:message key="registrationComplete.coursedetails" />
</h4>
<table class="detail">
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}" />
	</jsp:include>
</table>
--%>
