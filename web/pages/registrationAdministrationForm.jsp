<%@ include file="/common/taglibs.jsp"%>

<c:set var="admin" value="${false}"/>
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
	<c:set var="admin" value="${true}"/>
</c:if>
<fmt:message var="canDelete" key="access.registration.delete"/>


<title><fmt:message key="registrationAdministration.title"/></title>
<content tag="heading">
<c:if test="${admin == true}"><fmt:message key="registrationAdministration.heading"/></c:if>
<c:if test="${admin == false}"><fmt:message key="displayAdministration.heading"/></c:if>
</content>

<!--
<spring:bind path="registrationsBackingObject.*">
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
-->

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="registrationList.item" var="item"/>
<fmt:message key="registrationList.items" var="items"/>

<table class="detail">
	<tr>
		<th>
			<fmt:message key="course.name"/>
		</th>
		<td>
			<c:choose>
			<c:when test="${not empty course.detailURL}">
				<a href="<c:out value="${course.detailURL}"/>" title="<c:out value="${course.description}"/>"><c:out value="${course.name}"/></a>
				<button type="button" onclick="location.href='<c:out value="${course.detailURL}"/>'">
					<fmt:message key="button.more"/>
				</button>
			</c:when>
			<c:otherwise>
				<c:out value="${course.name}"/>
			</c:otherwise>
		</c:choose>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.type"/>
		</th>
		<td>
			<spring:bind path="course.type">
				<c:out value="${status.value}"/>
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.startTime"/>
		</th>
		<td>
			<fmt:formatDate value="${course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.stopTime"/>
		</th>
		<td>
			<fmt:formatDate value="${course.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.duration"/>
		</th>
		<td>
			<spring:bind path="course.duration">
				<c:out value="${status.value}"/>
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.municipality"/>
		</th>
		<td>
			<spring:bind path="course.municipality.name">
				<c:out value="${status.value}"/>
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.serviceArea"/>
		</th>
		<td>
			<spring:bind path="course.serviceArea.name">
				<c:out value="${status.value}"/>
			</spring:bind>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.location"/>
		</th>
		<td>
			<a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${course.location.id}"/></c:url>" title="<c:out value="${course.location.description}"/>"><c:out value="${course.location.name}"/></a>
			<button type="button" onclick="location.href='<c:url value="/detailsLocation.html"><c:param name="id" value="${course.location.id}"/></c:url>'">
				<fmt:message key="button.details"/>
			</button>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.responsible"/>
		</th>
		<td>
			<a href="<c:url value="/detailsUser.html"><c:param name="id" value="${course.responsible.id}"/></c:url>"><c:out value="${course.responsible.name}"/></a>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.instructor"/>
		</th>
		<td>
			<a href="<c:url value="/detailsPerson.html"><c:param name="id" value="${course.instructor.id}"/></c:url>" title="<c:out value="${course.instructor.description}"/>"><c:out value="${course.instructor.name}"/></a>
			<button type="button" onclick="location.href='<c:url value="/detailsPerson.html"><c:param name="id" value="${course.instructor.id}"/></c:url>'">
				<fmt:message key="button.details"/>
			</button>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.maxAttendants"/>
		</th>
		<td>
			<fmt:formatNumber value="${course.maxAttendants}" minFractionDigits="0"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.reservedMunicipal"/>
		</th>
		<td>
			<fmt:formatNumber value="${course.reservedMunicipal}" minFractionDigits="0"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.feeMunicipal"/>
		</th>
		<td>
			<fmt:formatNumber value="${course.feeMunicipal}" minFractionDigits="2"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.feeExternal"/>
		</th>
		<td>
			<fmt:formatNumber value="${course.feeExternal}" minFractionDigits="2"/>
		</td>
	</tr>

	<tr>
		<th>
			<fmt:message key="course.registerStart"/>
		</th>
		<td>
			<fmt:formatDate value="${course.registerStart}" type="both" pattern="${dateformat} ${timeformat}"/>
		</td>
	</tr>

	<c:if test="${admin == true}">
		<tr>
			<th>
				<fmt:message key="course.reminder"/>
			</th>
			<td>
				<fmt:formatDate value="${course.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
			</td>
		</tr>
	</c:if>

	<tr>
		<th>
			<fmt:message key="course.registerBy"/>
		</th>
		<td>
			<fmt:formatDate value="${course.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
		</td>
	</tr>

	<c:if test="${admin == true}">
		<tr>
			<th>
				<fmt:message key="course.freezeAttendance"/>
			</th>
			<td>
				<fmt:formatDate value="${course.freezeAttendance}" type="both" pattern="${dateformat} ${timeformat}"/>
			</td>
		</tr>
	</c:if>

	<tr>
		<th>
			<fmt:message key="course.description"/>
		</th>
		<td>
			<spring:bind path="course.description">
				<c:out value="${status.value}"/>
			</spring:bind>
		</td>
	</tr>
	
	<c:if test="${allowRegistration == true && isCourseFull == true}">
    <tr>
        <td colspan="2">
            <div class="error">
           		<img src="<c:url value="/images/iconWarning.gif"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" />
           		<fmt:message key="errors.courseFull.warning"/><br />
    		</div>
        </td>
    </tr>
	</c:if>
</table>

<form method="post" action="<c:url value="/administerRegistration.html"/>" name="registrationAdministrationForm" id="registrationAdministrationForm">

<input type="hidden" name="<c:out value="courseid"/>" value="<c:out value="${course.id}"/>"/>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible || isCourseParticipant}">
<display:table name="${registrationsBackingObject.registrations}" cellspacing="0" cellpadding="0" 
	pagesize="25" class="list"
	export="true" id="registrationList" requestURI="">

	<display:column property="firstName" sortable="true" headerClass="sortable" 
		titleKey="registration.firstName"/>

	<display:column property="lastName" sortable="true" headerClass="sortable" 
		titleKey="registration.lastName"/>

	<display:column property="municipality.name" sortable="true" headerClass="sortable" 
		titleKey="registration.municipality"/>

	<display:column property="jobTitle" sortable="true" headerClass="sortable" 
		titleKey="registration.jobTitle"/>

	<display:column property="workplace" sortable="true" headerClass="sortable" 
		titleKey="registration.workplace"/>
		
	<display:column property="phone" sortable="true" headerClass="sortable" 
		titleKey="registration.phone"/>

	<display:column property="mobilePhone" sortable="true" headerClass="sortable" 
		titleKey="registration.mobilePhone"/>
		
	<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
	<display:column property="comment" sortable="true" headerClass="sortable" 
		titleKey="registration.comment"/>
	</c:if>

	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.invoiced">
	<c:if test="${admin == true}">
		<input type="hidden" name="_invoiced_<c:out value="${registrationList.id}"/>" value="visible" />
		<input type="checkbox" name="invoiced_<c:out value="${registrationList.id}"/>"
					<c:if test="${registrationList.invoiced == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_invoiced_<c:out value="${registrationList.id}"/>" value="visible" />
		<input type="hidden" name="invoiced_<c:out value="${registrationList.id}"/>" value="${registrationList.invoiced}" />
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="registration.invoiced">
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>

	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.reserved">
	<c:if test="${admin == true}">
		<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="checkbox" name="reserved_<c:out value="${registrationList.id}"/>"
		<c:if test="${registrationList.reserved == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_reserved<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="hidden" name="reserved_<c:out value="${registrationList.id}"/>" value="${registrationList.reserved}" />
		<c:if test="${registrationList.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="registration.reserved">
		<c:if test="${registrationList.reserved == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.reserved == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
	
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.attended">
	<c:if test="${admin == true}">
		<input type="hidden" name="_attended<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="checkbox" name="attended_<c:out value="${registrationList.id}"/>"
		<c:if test="${registrationList.attended == true}"> checked="checked" </c:if> />
	</c:if>
	<c:if test="${admin == false}">
		<input type="hidden" name="_attended<c:out value="${registrationList.id}"/>" value="visible"/>
		<input type="hidden" name="attended_<c:out value="${registrationList.id}"/>" value="${registrationList.attended}" />
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</c:if>
	</display:column>
	<display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="registration.attended">
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>

	<c:if test="${(admin == true || canDelete == true) && allowEditRegistration == true}">
	<display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseid" value="${registrationList.courseid}"/></c:url>'">
			<fmt:message key="button.edit"/>
		</button>
		<button type="submit" name="unregister"	onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmUnregistration()">
			<fmt:message key="button.unregister"/>
		</button>
		<c:if test="${admin == true}">
		<button type="submit" name="delete" onclick="document.registrationAdministrationForm.regid.value=<c:out value="${registrationList.id}"/>;bCancel=true;return confirmDeleteRegistration()">
			<fmt:message key="button.delete"/>
		</button>
		</c:if>
	</display:column>
	</c:if>
	<display:setProperty name="paging.banner.item_name" value="${item}"/>
	<display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>
</c:if> 

<input type="hidden" id="regid" name="regid" value="0"/> 

<c:if test="${admin == true}">
<input type="submit" class="button" name="save" 
	onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
</c:if> 

<input type="submit" class="button" name="docancel"	onclick="bCancel=true" 
	value="<fmt:message key="button.cancel"/>" />
	
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible || isCourseParticipant}">
	<c:if test="${allowRegistration == true}">
		<button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
	    	<fmt:message key="button.signup"/>
		</button>
	</c:if>
</c:if>
</form>