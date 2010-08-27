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
				<img src="<c:url context="${urlContext}" value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>


<fmt:message key="date.format" var="dateformat" />
<fmt:message key="date.format.localized" var="datelocalized" />
<fmt:message key="time.format" var="timeformat" />

<SCRIPT LANGUAGE="JavaScript" ID="js1">
	var cal1 = new CalendarPopup();
	cal1.setMonthNames('Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'); 
	cal1.setDayHeaders('S','M','T','O','T','F','L'); 
	cal1.setWeekStartDay(1); 
	cal1.setTodayText("Idag");
	cal1.showYearNavigation();


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
		if(courseId == "<c:out value="${registration.courseid}"/>"){
			document.getElementById('savebutton').value = "<fmt:message key="button.register.update"/>";
			if (!(document.getElementById('savebutton2') === null)) {
				document.getElementById('savebutton2').value = "<fmt:message key="button.register.update"/>";
			}
		}
		else {
			document.getElementById('savebutton').value = "<fmt:message key="button.register.change"/>";
			if (!(document.getElementById('savebutton2') === null)) {
				document.getElementById('savebutton2').value = "<fmt:message key="button.register.change"/>";
			}
		}
	}
</SCRIPT>	
		



<c:if test="${!illegalRegistration}">
	
	<c:if test="${!isSVV}">
	<div class="message" style="font-size: 12px">
		<fmt:message key="registrationDetail.emailexplanation" />
	</div>
	</c:if>

	<form:form commandName="registration" onsubmit="return validateRegistration(this)" name="registration">
		<table class="detail">
			<form:hidden path="id" />
			<form:hidden path="courseid" />

			<tr>
				<th>
					<soak:label key="course.name" />
				</th>
				<td>
					<c:out value="${course.name}" escapeXml="false" />, <c:out value="${course.location.name}"/> - <fmt:formatDate value="${course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
				</td>
			</tr>

			<c:if test="${!empty registration.id}">
			<tr>
				<th>
					<soak:label key="registration.status" />
				</th>
				<td>
					<c:if test="${registration.status == 0}">&nbsp;<fmt:message key="registration.status.invited" /></c:if>
					<c:if test="${registration.status == 1}">&nbsp;<fmt:message key="registration.status.waiting" /></c:if>
					<c:if test="${registration.status == 2}">&nbsp;<fmt:message key="registration.status.reserved" /></c:if>
					<c:if test="${registration.status == 3}">&nbsp;<fmt:message key="registration.status.cancelled" /></c:if>
				</td>
			</tr>
			</c:if>

			<c:if test="${showEmployeeFields}">
			<tr>
				<th>
					<soak:label key="registration.employeeNumber" />
				</th>
				<td>
					<form:input path="employeeNumber" />
					<form:errors cssClass="fieldError" path="employeeNumber" />
				</td>
			</tr>
			</c:if>
			<tr>
				<th>
					<soak:label key="registration.firstName" />
				</th>
				<td>
					<form:input path="firstName" maxlength="100"/>
					<form:errors cssClass="fieldError" htmlEscape="false" path="firstName" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.lastName" />
				</th>
				<td>
					<form:input path="lastName" maxlength="100"/>
					<form:errors cssClass="fieldError" htmlEscape="false" path="lastName" />
				</td>
			</tr>

			<tr>
				<th>
					<soak:label key="registration.email" />
				</th>
				<td>
					<form:input path="email" maxlength="50"/>
					<form:errors cssClass="fieldError" htmlEscape="false" path="email" />
				</td>
			</tr>
            <c:if test="${emailrepeat}">
            <tr>
                <th>
                    <soak:label key="registration.emailRepeat" />
                </th>
                <td>
                    <form:input path="emailRepeat" maxlength="50"/>
                    <form:errors cssClass="fieldError" path="emailRepeat" />
                </td>
            </tr>
            </c:if>

            <c:if test="${useBirthdateForRegistration}">
			<tr>
				<th>
					<soak:label key="registration.birthdate" />
				</th>
				<td>
					<fmt:formatDate value="${startYear}" type="date" pattern="${dateformat}" var="init" />
					<fmt:formatDate value="${registration.birthdate}" type="date" pattern="${dateformat}" var="birthdate" />
					<input type="text" size="12" name="birthdate" id="birthdate" value="<c:choose><c:when test="${empty birthdateFromRequest}" ><c:out value="${birthdate}"/></c:when><c:otherwise><c:out value="${birthdateFromRequest}"/></c:otherwise></c:choose>" 
						title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" />
					<a href="#" name="a1" id="Anch_birthdate"
						onClick="cal1.select(document.registration.birthdate,'Anch_birthdate','<fmt:message key="date.format"/>',(document.registration.birthdate.value=='')?'<c:out value="${init}"/>':document.registration.birthdate.value,''); return false;"
						title="<fmt:message key="course.calendar.title"/>"><img src="<c:url context="${urlContext}" value="/images/calendar.png"/>"></a>
	                    <form:errors cssClass="fieldError" path="birthdate" />
				</td>
			</tr>
			</c:if>
			
			<c:if test="${useAttendants}">
			<tr>
				<th>
					<soak:label key="registration.participants" />
				</th>
				<td>
					<form:input path="participants" />
					<form:errors cssClass="fieldError" htmlEscape="false" path="participants" />
				</td>
			</tr>
			</c:if>
			
            <c:if test="${!isSVV}">
			<tr>
				<th>
					<soak:label key="registration.phone" />
				</th>
				<td>
					<form:input path="phone" maxlength="30"/>
					<form:errors cssClass="fieldError" path="phone" />
				</td>
			</tr>
			</c:if>

			<tr>
				<th>
					<soak:label key="registration.mobilePhone" />
				</th>
				<td>
					<form:input path="mobilePhone" maxlength="30"/>
					<form:errors cssClass="fieldError" htmlEscape="false" path="mobilePhone" />
				</td>
			</tr>
            <c:if test="${showEmployeeFields}">
			<tr>
				<th>
					<soak:label key="registration.closestLeader" />
				</th>
				<td>
					<form:input path="closestLeader" maxlength="150"/>
					<form:errors cssClass="fieldError" path="closestLeader" />
				</td>
			</tr>
            </c:if>
            <c:if test="${showJobTitle}">
			<tr>
				<th>
					<soak:label key="registration.jobTitle" />
				</th>
				<td>
					<form:input path="jobTitle" maxlength="100"/>
					<form:errors cssClass="fieldError" path="jobTitle" />
				</td>
			</tr>
            </c:if>

            <c:if test="${showWorkplace}">
			<tr>
				<th>
					<soak:label key="registration.workplace" />
				</th>
				<td>
					<form:input path="workplace" maxlength="100"/>
					<form:errors cssClass="fieldError" path="workplace" />
				</td>
			</tr>
            </c:if>

			<c:if test="${!isSVV}">
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
			</c:if>
			
            <c:if test="${showServiceArea}">
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
            </c:if>
            <c:if test="${showComment}">
			<tr>
				<th>
					<soak:label key="registration.comment" />
				</th>
				<td>
					<form:textarea path="comment" cols="50" rows="2" />
					<form:errors cssClass="fieldError" path="comment" />
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
						<soak:label key="registration.invoiceAddress.name" />
					</th>
					<td>
						<form:input path="invoiceName" maxlength="100"/>
						<form:errors cssClass="fieldError" path="invoiceName" />
					</td>
				</tr>
				</c:if>

				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.address" />
					</th>
					<td>
						<form:input path="invoiceAddress.address" maxlength="100"/>
						<form:errors cssClass="fieldError" path="invoiceAddress.address" />
					</td>
				</tr>
				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.postalCode" />
					</th>
					<td>
						<form:input path="invoiceAddress.postalCode" maxlength="15"/>
						<form:errors cssClass="fieldError"
							path="invoiceAddress.postalCode" />
					</td>
				</tr>
				<tr>
					<th>
						<soak:label key="registration.invoiceAddress.city" />
					</th>
					<td>
						<form:input path="invoiceAddress.city" maxlength="50"/>
						<form:errors cssClass="fieldError" path="invoiceAddress.city" />
					</td>
				</tr>
			</c:if>
			
			<c:if test="${isSVV}">
			<tr>
				<th>
					<soak:label key="registration.organization" />
				</th>
				<td>
					<form:select path="organizationid" items="${organizations}" itemLabel="name" itemValue="id" />
					<form:errors cssClass="fieldError" path="organizationid" />
				</td>
			</tr>
			</c:if>
		</table>
		&nbsp;<br/>

		<table>	
			<tr>
				<td class="buttonBar" colspan="2" align="left">
					<c:if test="${empty registration.id && isCourseFull == false}">
	                    <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register.save"/>" />
    	            </c:if>
					<c:if test="${!empty registration.id && registration.status < 3}">
                    <input type="submit" class="button" name="save" id="savebutton" onclick="bCancel=false" value="<fmt:message key="button.register.update"/>" />
						<c:if test="${isAdmin}">
						<input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDeleteRegistration()"
							value="<fmt:message key="button.delete"/>" />
						</c:if>
						<input type="submit" class="button" name="unregister" onclick="bCancel=true;return confirmUnregistration()"
							value="<fmt:message key="button.unregister"/>" />
					</c:if>
					<input type="submit" class="button" name="cancel"
						onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
				</td>
			</tr>
			<c:if test="${!empty courseList && (isAdmin || isEducationResponsible || isEventResponsible || isReader || isSVV) && registration.status < 3}">
			<tr>
				<th>
					<soak:label key="registration.changeCourse" />
				</th>
			</tr>
			<tr>
			<td>				
				<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isSVV}">
					<display:table name="${courseList}" cellspacing="0" cellpadding="0"
						id="courseList" pagesize="${itemCount}" class="list" export="false"
						requestURI="performRegistration.html">

						<display:column media="html" sortable="false"
							headerClass="sortable" titleKey="button.heading">
							<input type="radio" name="changeCourse" value="${courseList.id}"
								onclick="chooseCourse(<c:out value="${courseList.id}"/>);"
								<c:if test="${courseList.id == registration.courseid}">checked</c:if> />
						</display:column>

						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.name" sortProperty="name">
							<c:if test="${courseList.status == 3}">
								<img src="<c:url context="${urlContext}" value="/images/cancel.png"/>"
									alt="<fmt:message key="icon.warning"/>" class="icon" />
								<fmt:message key="course.cancelled.alert" />
								<br />
							</c:if>
							<a
								href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>"
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

<c:if test="${showDuration}">
						<display:column property="duration" sortable="true"
							headerClass="sortable" titleKey="course.duration" />
</c:if>
						<display:column property="organization.name" sortable="true"
							headerClass="sortable" titleKey="course.organization" />

<c:if test="${useServiceArea}">
						<display:column property="serviceArea.name" sortable="true"
							headerClass="sortable" titleKey="course.serviceArea" />
</c:if>

<c:if test="${useOrganization2 && !isCourseParticipant}">
						<display:column property="organization2.name" sortable="true"
							headerClass="sortable" titleKey="course.organization2" />
</c:if>

						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.location">
							<a
								href="<c:url context="${urlContext}" value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>"
								title="<c:out value="${courseList.location.description}"/>"><c:out
									value="${courseList.location.name}" /> </a>
						</display:column>

<c:if test="${!isCourseParticipant}">
						<display:column media="html" sortable="true"
							headerClass="sortable" titleKey="course.responsible">
							<a
								href="<c:url context="${urlContext}" value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out
									value="${courseList.responsible.fullName}" /> </a>
						</display:column>
</c:if>
						<display:setProperty name="paging.banner.item_name" value="${item}" />
						<display:setProperty name="paging.banner.items_name" value="${items}" />

					</display:table>
				</c:if>
				</td>

			</tr>

			<tr>
				<td class="buttonBar" colspan="2" align="left">
					<c:if test="${empty registration.id}">
	                    <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register.save"/>" />
    	            </c:if>
					<c:if test="${!empty registration.id}">
                    <input type="submit" class="button" name="save" id="savebutton2" onclick="bCancel=false" value="<fmt:message key="button.register.update"/>" />
						<c:if test="${isAdmin}">
						<input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDeleteRegistration()"
							value="<fmt:message key="button.delete"/>" />
						</c:if>
						<c:if test="${registration.status != 3}">
						<input type="submit" class="button" name="unregister" onclick="bCancel=true;return confirmUnregistration()"
							value="<fmt:message key="button.unregister"/>" />
						</c:if>							
					</c:if>
					<input type="submit" class="button" name="cancel"
						onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
				</td>
			</tr>
			</c:if>
		</table>
	</form:form>
</c:if>

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

<v:javascript formName="registration" cdata="false"
	dynamicJavascript="true" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url context="${urlContext}" value="/scripts/validator.jsp"/>"></script>
