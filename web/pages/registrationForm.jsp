<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationDetail.title" />
</title>
<content tag="heading">
<fmt:message key="registrationDetail.heading" />
</content>

<spring:bind path="registration.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />

<c:if test="${!illegalRegistration}">
	<div class="message" style="font-size: 12px">
		<fmt:message key="registrationDetail.responsibility" />
	</div>

	<h4>
		<fmt:message key="registrationComplete.yourdetails" />
	</h4>

	<div class="message" style="font-size: 12px">
		<fmt:message key="registrationDetail.emailexplanation" />
	</div>

	<form:form commandName="registration" onsubmit="return validateRegistration(this)" name="registration">
		<table class="detail">
			<form:hidden path="id" />
			<form:hidden path="courseid" />
			<tr>
				<th>
					<soak:label key="registration.employeeNumber" />
				</th>
				<td>
					<form:input path="employeeNumber" />
					<form:errors cssClass="fieldError" path="employeeNumber" />
				</td>
			</tr>
			<tr>
				<th>
					<soak:label key="registration.firstName" />
				</th>
				<td>
					<form:input path="firstName" />
					<form:errors cssClass="fieldError" path="firstName" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.lastName" />
				</th>
				<td>
					<form:input path="lastName" />
					<form:errors cssClass="fieldError" path="lastName" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.email" />
				</th>
				<td>
					<form:input path="email" />
					<form:errors cssClass="fieldError" path="email" />
				</td>
			</tr>
<c:if test="${emailrepeat}">
            <tr>
                <th>
                    <soak:label key="registration.emailRepeat" />
                </th>
                <td>
                    <form:input path="emailRepeat" />
                    <form:errors cssClass="fieldError" path="emailRepeat" />
                </td>
            </tr>
</c:if>
			<tr>
				<th>
					<soak:label key="registration.phone" />
				</th>
				<td>
					<form:input path="phone" />
					<form:errors cssClass="fieldError" path="phone" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.mobilePhone" />
				</th>
				<td>
					<form:input path="mobilePhone" />
					<form:errors cssClass="fieldError" path="mobilePhone" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.closestLeader" />
				</th>
				<td>
					<form:input path="closestLeader" />
					<form:errors cssClass="fieldError" path="closestLeader" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.jobTitle" />
				</th>
				<td>
					<form:input path="jobTitle" />
					<form:errors cssClass="fieldError" path="jobTitle" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.workplace" />
				</th>
				<td>
					<form:input path="workplace" />
					<form:errors cssClass="fieldError" path="workplace" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.organization" />
				</th>
				<td>
					<form:select path="organizationid" items="${organizations}"
						itemLabel="name" itemValue="id" onchange="fillSelect(this);" />
					<form:errors cssClass="fieldError" path="organizationid" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.serviceArea" />
				</th>
				<td>
					<spring:bind path="registration.serviceAreaid">
						<select name="<c:out value="${status.expression}"/>">
							<c:forEach var="servicearea" items="${serviceareas}">
								<c:choose>
									<c:when test="${empty servicearea.id}">
										<option value="<c:out value="${servicearea.id}"/>"
											<c:if test="${servicearea.id == registration.serviceAreaid}"> selected="selected"</c:if>>
											<c:out value="${servicearea.name}" />
										</option>
									</c:when>
									<c:otherwise>
										<c:if
											test="${servicearea.organizationid == registration.organizationid}">
											<option value="<c:out value="${servicearea.id}"/>"
												<c:if test="${servicearea.id == registration.serviceAreaid}"> selected="selected"</c:if>>
												<c:out value="${servicearea.name}" />
											</option>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
						<span class="fieldError"><c:out
								value="${status.errorMessage}" escapeXml="false" /> </span>
					</spring:bind>
				</td>
			</tr>


			<tr>
				<th>
					<soak:label key="registration.comment" />
				</th>
				<td>
					<form:textarea path="comment" cols="50" rows="2" />
					<form:errors cssClass="fieldError" path="comment" />
				</td>
			</tr>

			<c:if test="${!freeCourse}">
				<tr>
					<th>
						<soak:label key="registration.invoiceAddress" />
					</th>
				</tr>

				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.name" />
					</th>
					<td>
						<form:input path="invoiceName" />
						<form:errors cssClass="fieldError" path="invoiceName" />
					</td>
				</tr>

				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.address" />
					</th>
					<td>
						<form:input path="invoiceAddress.address" />
						<form:errors cssClass="fieldError" path="invoiceAddress.address" />
					</td>
				</tr>
				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.city" />
					</th>
					<td>
						<form:input path="invoiceAddress.city" />
						<form:errors cssClass="fieldError" path="invoiceAddress.city" />
					</td>
				</tr>
				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.postalCode" />
					</th>
					<td>
						<form:input path="invoiceAddress.postalCode" />
						<form:errors cssClass="fieldError"
							path="invoiceAddress.postalCode" />
					</td>
				</tr>
			</c:if>
			<c:if test="${!empty courseList}">
			<tr>
				<th>
					<soak:label key="registration.changeCourse" />
				</th>
					<td>
				<c:if
					test="${isAdmin || isEducationResponsible || isCourseResponsible}">
					<display:table name="${courseList}" cellspacing="0" cellpadding="0"
						id="courseList" pagesize="25" class="list" export="false"
						requestURI="listCourses.html">

						<display:column media="html" sortable="false"
							headerClass="sortable" titleKey="button.heading">
							<input type="radio" name="changeCourse" value="${courseList.id}"
								onclick="chooseCourse(<c:out value="${courseList.id}"/>);" />
						</display:column>

						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.name" sortProperty="name">
							<c:if test="${courseList.status == 3}">
								<img src="<c:url value="/images/cancel.png"/>"
									alt="<fmt:message key="icon.warning"/>" class="icon" />
								<fmt:message key="course.cancelled.alert" />
								<br />
							</c:if>
							<a
								href="<c:url value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>"
								title="<c:out value="${courseList.description}"/>"><c:out
									value="${courseList.name}" /> </a>
						</display:column>

						<display:column sortable="true" headerClass="sortable"
							titleKey="course.startTime" sortProperty="startTime">
							<fmt:formatDate value="${courseList.startTime}" type="both"
								pattern="${dateformat} ${timeformat}" />
						</display:column>

						<display:column property="availableAttendants" sortable="true"
							headerClass="sortable" titleKey="course.availableAttendants" />

						<display:column property="duration" sortable="true"
							headerClass="sortable" titleKey="course.duration" />

						<display:column property="organization.name" sortable="true"
							headerClass="sortable" titleKey="course.organization" />

						<display:column property="serviceArea.name" sortable="true"
							headerClass="sortable" titleKey="course.serviceArea" />

						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.location">
							<a
								href="<c:url value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>"
								title="<c:out value="${courseList.location.description}"/>"><c:out
									value="${courseList.location.name}" /> </a>
						</display:column>
						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.responsible">
							<a
								href="<c:url value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out
									value="${courseList.responsible.fullName}" /> </a>
						</display:column>

						<display:setProperty name="paging.banner.item_name"
							value="${item}" />
						<display:setProperty name="paging.banner.items_name"
							value="${items}" />

					</display:table>
				</c:if>
				</td>


			</tr>
			</c:if>


			<tr>
				<td></td>
				<td class="buttonBar">
					<c:if
						test="${isAdmin || isEducationResponsible || isCourseResponsible || isCourseParticipant}">
						<input type="submit" class="button" name="save"
							onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
					</c:if>
					<c:if test="${!empty registration.id}">
						<c:if test="${isAdmin}">
							<input type="submit" class="button" name="delete"
								onclick="bCancel=true;return confirmDeleteRegistration()"
								value="<fmt:message key="button.delete"/>" />
						</c:if>
						<input type="submit" class="button" name="unregister"
							onclick="bCancel=true;return confirmUnregistration()"
							value="<fmt:message key="button.unregister"/>" />
					</c:if>
					<input type="submit" class="button" name="cancel"
						onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
				</td>
			</tr>
		</table>
	</form:form>
</c:if>

<h4>
	<fmt:message key="registrationComplete.coursedetails" /> 
</h4>

<table class="detail">
	<jsp:include page="course.jsp">
		<jsp:param name="course" value="${course}" />
	</jsp:include>
</table>


<script type="text/javascript">
<!--

// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
 var orgid=obj.options[obj.selectedIndex].value;
 var temp= document.registration.serviceAreaid;
  
 while(temp.firstChild){
 	temp.removeChild(temp.firstChild);
 }
 
 var j = 0;
	<c:forEach var="servicearea" items="${serviceareas}">
		if ("<c:out value="${servicearea.id}"/>" == ""){
			temp.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>", true);
			j++
		}
		else if ("<c:out value="${servicearea.organizationid}"/>" == orgid){
			temp.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>");
			j++ 
		}
	</c:forEach>
}

function chooseCourse(courseId){
	document.getElementById('courseid').value = courseId;
}

// -->
</script>

<v:javascript formName="registration" cdata="false"
	dynamicJavascript="true" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>
