<%@ include file="/common/taglibs.jsp"%>
<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />
<fmt:message key="attachmentList.item" var="item" />
<fmt:message key="attachmentList.items" var="items" />
<fmt:message key="access.course.singleprice" var="singleprice" />

<tr>
	<th>
		<fmt:message key="course.name" />
	</th>
	<td>
		<c:choose>
			<c:when test="${not empty course.detailURL}">
				<a href="<c:out value="${course.detailURL}"/>" target="_blank"
					title="<c:out value="${course.description}"/>"><c:out
						value="${course.name}" /><img src="<c:url value="/images/link_go.png"/>"></a>
			</c:when>
			<c:otherwise>
				<c:out value="${course.name}" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.description" />
	</th>
	<td>
		<spring:bind path="course.description">
			<c:out value="${status.value}" />
		</spring:bind>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.type" />
	</th>
	<td>
		<spring:bind path="course.type">
			<c:out value="${status.value}" />
		</spring:bind>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.startTime" />
	</th>
	<td>
		<fmt:formatDate value="${course.startTime}" type="both"
			pattern="${dateformat} ${timeformat}" />
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.stopTime" />
	</th>
	<td>
		<fmt:formatDate value="${course.stopTime}" type="both"
			pattern="${dateformat} ${timeformat}" />
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.duration" />
	</th>
	<td>
		<spring:bind path="course.duration">
			<c:out value="${status.value}" />
		</spring:bind>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.organization" />
	</th>
	<td>
		<spring:bind path="course.organization.name">
			<c:out value="${status.value}" />
		</spring:bind>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.serviceArea" />
	</th>
	<td>
		<spring:bind path="course.serviceArea">
			<c:out value="${status.value.name}" />
		</spring:bind>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.location" />
	</th>
	<td>
		<a
			href="<c:url value="/detailsLocation.html"><c:param name="id" value="${course.location.id}"/></c:url>"
			title="<c:out value="${course.location.description}"/>"><c:out
				value="${course.location.name}" /></a>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.responsible" />
	</th>
	<td>
		<a
			href="<c:url value="/detailsUser.html"><c:param name="username" value="${course.responsible.username}"/></c:url>"><c:out
				value="${course.responsible.fullName}" /> </a>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.instructor" />
	</th>
	<td>
		<a
			href="<c:url value="/detailsPerson.html"><c:param name="id" value="${course.instructor.id}"/></c:url>"
			title="<c:out value="${course.instructor.description}"/>"><c:out
				value="${course.instructor.name}" /></a>
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.maxAttendants" />
	</th>
	<td>
		<fmt:formatNumber value="${course.maxAttendants}"
			minFractionDigits="0" />
	</td>
</tr>

<c:if test="${!singleprice}">
	<tr>
		<th>
			<fmt:message key="course.reservedInternal" />
		</th>
		<td>
			<fmt:formatNumber value="${course.reservedInternal}"
				minFractionDigits="0" />
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.feeInternal" />
		</th>
		<td>
			<fmt:formatNumber value="${course.feeInternal}" minFractionDigits="2" />
		</td>
	</tr>
</c:if>

<tr>
	<th>
		<fmt:message key="course.feeExternal" />
	</th>
	<td>
		<fmt:formatNumber value="${course.feeExternal}" minFractionDigits="2" />
	</td>
</tr>

<tr>
	<th>
		<fmt:message key="course.registerStart" />
	</th>
	<td>
		<fmt:formatDate value="${course.registerStart}" type="both"
			pattern="${dateformat} ${timeformat}" />
	</td>
</tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
	<tr>
		<th>
			<fmt:message key="course.reminder" />
		</th>
		<td>
			<fmt:formatDate value="${course.reminder}" type="both"
				pattern="${dateformat} ${timeformat}" />
		</td>
	</tr>
</c:if>

<tr>
	<th>
		<fmt:message key="course.registerBy" />
	</th>
	<td>
		<fmt:formatDate value="${course.registerBy}" type="both"
			pattern="${dateformat} ${timeformat}" />
	</td>
</tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
	<tr>
		<th>
			<fmt:message key="course.freezeAttendance" />
		</th>
		<td>
			<fmt:formatDate value="${course.freezeAttendance}" type="both"
				pattern="${dateformat} ${timeformat}" />
		</td>
	</tr>
</c:if>

<tr>
	<th>
		<fmt:message key="course.url" />
	</th>
	<td>
		<a
			href="<fmt:message key="javaapp.baseurl"/><fmt:message key="javaapp.courseurl"/><c:out value="${course.id}"/>"
			target="_blank"><fmt:message key="javaapp.baseurl" /><fmt:message
				key="javaapp.courseurl" /><c:out value="${course.id}" /><img src="<c:url value="/images/link_go.png"/>"></a>
	</td>
</tr>
