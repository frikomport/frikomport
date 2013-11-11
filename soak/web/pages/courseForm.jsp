<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="date.format" var="dateformat" />
<fmt:message key="time.format" var="timeformat" />
<fmt:message key="date.format.localized" var="datelocalized" />
<fmt:message key="time.format.localized" var="timelocalized" />

<SCRIPT LANGUAGE="JavaScript" ID="js1">
var cal1 = new CalendarPopup();
cal1.setMonthNames('Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'); 
cal1.setDayHeaders('S','M','T','O','T','F','L'); 
cal1.setWeekStartDay(1); 
cal1.setTodayText("Idag");
cal1.setReturnFunction("handleCalResult");

function handleCalResult(y,m,d) {
	if (window.CP_targetInput == document.course.startTimeDate) {
		document.course.startTimeDate.value = PadDigits(d, 2) + "." + PadDigits(m, 2) + "." + PadDigits(y, 4);
		changeGUIBasedOnField("startTimeDate");
		document.course.stopTimeDate.value = document.course.startTimeDate.value;
	} else {
		window.CP_targetInput.value = PadDigits(d, 2) + "." + PadDigits(m, 2) + "." + PadDigits(y, 4);
	}
}

function PadDigits(n, totalDigits) { 
    n = n.toString(); 
    var pd = ''; 
    if (totalDigits > n.length) { 
        for (i=0; i < (totalDigits-n.length); i++) { 
            pd += '0'; 
        } 
    } 
    return pd + n.toString(); 
} 

function changeGUIBasedOnField(fieldName) {
	if (fieldName == "startTimeDate" && !($p('publishButton') === null)) {
		dateStr= $p('startTimeDate').value;
		startTimeDate = new Date(dateStr.substring(6), (Number(dateStr.substring(3,5))-1), dateStr.substring(0,2), 23, 59, 0, 0);
		
		if (new Date() > startTimeDate) {
			Element.hide($p('publishButton'));
		} else {
			Element.show($p('publishButton'));
		}
	}
}
		
function setStopDate() {
	if (document.getElementById('stopTimeDate').value == ""){
		document.getElementById('stopTimeDate').value = document.getElementById('startTimeDate').value
	}
}

function setMaxAttendants(obj) {
	var locid = obj.options[obj.selectedIndex].value;
 	
	<c:forEach var="location" items="${locations}">
        <c:if test="${location.maxAttendants != null}">
        if (("<c:out value="${location.id}"/>" != "") && ("<c:out value="${location.id}"/>" == locid)){
			document.getElementById('maxAttendants').value = <c:out value="${location.maxAttendants}"/>;
		}
        </c:if>
    </c:forEach>

}

// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
 var orgid=obj.options[obj.selectedIndex].value;
 var serviceArea = document.course.serviceAreaid;

 if(serviceArea != null){
    while(serviceArea.firstChild){
        serviceArea.removeChild(serviceArea.firstChild);
    }

    var j = 0;
	<c:forEach var="servicearea" items="${serviceareas}">
	    if ("<c:out value="${servicearea.organizationid}"/>" == orgid || "<c:out value="${servicearea.id}"/>" == ""){
		    if ("<c:out value="${servicearea.id}"/>" == "<c:out value="${course.serviceAreaid}"/>") {
		        select = true;
		    } else {
		        select = false;
		    } 
	    	serviceArea.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>", select, select);
	        j++;
	    }
	</c:forEach>
 }
 
<c:if test="${filterlocation || isSVV}">
    var location = document.course.locationid;
    while(location.firstChild){
        location.removeChild(location.firstChild);
    }
    var k = 0;
<c:forEach var="location" items="${locations}">
    if ("<c:out value="${location.organizationid}"/>" == orgid || "<c:out value="${location.id}"/>" == "") {
	    if ("<c:out value="${location.id}"/>" == "<c:out value="${course.locationid}"/>") {
	        select = true;
	    } else {
	        select = false;
	    } 
        location.options[k]=new Option("<c:out value="${location.name}"/>", "<c:out value="${location.id}"/>", select, select);
        k++ ;
    }
</c:forEach>
 </c:if>
}

</SCRIPT>

<title><fmt:message key="courseEdit.title" />
</title>
<c:choose>
	<c:when test="${empty course.id && !empty course.name && not empty status.errorMessages}">
		<content tag="heading">
		<fmt:message key="courseEdit.heading.copy" />
		</content>
	</c:when>
	<c:when test="${empty course.id && empty course.name}">
		<content tag="heading">
		<fmt:message key="courseEdit.heading.new" />
		</content>
	</c:when>
	<c:when test="${!empty course.id && !empty course.name}">
		<content tag="heading">
		<fmt:message key="courseEdit.heading.edit" />
		</content>
	</c:when>
	<c:otherwise>
		<content tag="heading">
		<fmt:message key="courseEdit.heading" />
		</content>
	</c:otherwise>
</c:choose>

<spring:bind path="course.*">
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

<form:form commandName="course" onsubmit="return validateCourse(this)" name="course">

    <table class="detail">

		<form:hidden path="id" />

<c:choose>
    <c:when test="${showCourseName}">
		<tr>
			<th>
				<soak:label key="course.name" />
			</th>
			<td>
				<form:input size="50" maxlength="100" path="name" />
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="name" />
			</td>
		</tr>
    </c:when>
    <c:otherwise>
        		<form:hidden path="name"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${categories[1] != null}">
        <tr>
            <th>
                <soak:label key="course.category" />
            </th>
            <td>
                <form:select path="categoryid" items="${categories}" itemValue="id"
                             itemLabel="name" />
                <form:errors cssClass="fieldError" htmlEscape="false" path="categoryid" />
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        		<form:hidden path="categoryid"/>
    </c:otherwise>
</c:choose>


<c:choose>
<c:when test="${showDescription && ((isReader || isEventResponsible || isEducationResponsible || isAdmin) || (showDescriptionToPublic))}">
		<tr>
			<th>
				<c:choose>
				<c:when test="${showDescriptionToPublic}">
					<soak:label key="course.description" />
				</c:when>
				<c:otherwise>
					<soak:label key="course.description.internal" />
				</c:otherwise>
				</c:choose>
			</th>

			<td>
				<form:textarea cols="50" rows="3" path="description"/>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="description" />
			</td>
		</tr>
</c:when>
<c:otherwise>
				<form:hidden path="description" />
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${showRole}">
		<tr>
			<th>
				<soak:label key="course.role" />
			</th>
			<td>
				<form:select path="role" items="${roles}" itemValue="name"
					itemLabel="description" />
				<form:errors cssClass="fieldError" htmlEscape="false" path="role" />
			</td>
		</tr>
</c:when>
<c:otherwise>
				<form:hidden path="role" />
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${showType}">
		<tr>
			<th>
				<soak:label key="course.type" />
			</th>
			<td>
				<form:input path="type" maxlength="100" />
				<form:errors cssClass="fieldError" htmlEscape="false" path="type" />
			</td>
		</tr>
</c:when>
<c:otherwise>
				<form:hidden path="type" />
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${showRestricted}">
        <tr>
			<th>
				<soak:label key="course.restricted" />
			</th>
			<td>
				<form:checkbox path="restricted" />
				<form:errors cssClass="fieldError" htmlEscape="false" path="restricted" />
			</td>
		</tr>
</c:when>
<c:otherwise>
				<form:hidden path="restricted" />
</c:otherwise>
</c:choose>

        <tr>
			<th>
				<soak:label key="course.startTime" />
			</th>
			<td>
				<fmt:formatDate value="${course.startTime}" type="date" pattern="${dateformat}" var="startTimeDate" />
				<fmt:formatDate value="${course.startTime}" type="time" pattern="${timeformat}" var="startTimeTime" />
				<input type="text" size="12" name="startTimeDate" id="startTimeDate" value="<c:out value="${startTimeDate}"/>" 
					title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" onChange="changeGUIBasedOnField('startTimeDate');"/>
				<a href="#" name="a1" id="Anch_startTimeDate"
					onClick="cal1.select(document.course.startTimeDate,'Anch_startTimeDate','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
				<soak:label key="course.time" />
				<input type="text" size="6" name="startTimeTime" id="startTimeTime" value="<c:out value="${startTimeTime}"/>"
					title="<fmt:message key="time.format.title"/>: <fmt:message key="time.format.localized"/>" />
				<spring:bind path="course.startTime">
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="course.stopTime" />
			</th>
			<td>
				<fmt:formatDate value="${course.stopTime}" type="date" pattern="${dateformat}" var="stopTimeDate" />
				<fmt:formatDate value="${course.stopTime}" type="time" pattern="${timeformat}" var="stopTimeTime" />
				<input type="text" size="12" name="stopTimeDate" id="stopTimeDate" value="<c:out value="${stopTimeDate}"/>"
					title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" />
				<a href="#" name="a1" id="Anch_stopTimeDate"
					onClick="cal1.select(document.course.stopTimeDate,'Anch_stopTimeDate','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
				<soak:label key="course.time" />
				<input type="text" size="6" name="stopTimeTime" id="stopTimeTime" value="<c:out value="${stopTimeTime}"/>"
					title="<fmt:message key="time.format.title"/>: <fmt:message key="time.format.localized"/>" />
				<spring:bind path="course.stopTime">
					<span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>

		<c:choose>
			<c:when test="${showDuration}">
				<tr>
					<th>
						<soak:label key="course.duration" />
					</th>
					<td>
						<form:input path="duration" size="50" maxlength="100"/>
						<form:errors cssClass="fieldError" htmlEscape="false" path="duration" />
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<form:hidden path="duration" />
			</c:otherwise>
		</c:choose>

		<tr>
			<th>
				<soak:label key="course.organizationid" />
			</th>
			<td>
				<form:select path="organizationid" onchange="fillSelect(this);" id="organizationidElement">
					<form:options items="${organizations}" itemValue="id" itemLabel="name" htmlEscape="true"/>
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false" path="organizationid" />
			</td>
		</tr>

<c:choose>
	<c:when test="${useOrganization2}">
		<tr>
			<th>
				<soak:label key="course.organization2id" />
			</th>
			<td>
				<form:select path="organization2id">
					<form:options items="${organizations2}" itemValue="id" itemLabel="name" htmlEscape="true"/>
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false" path="organization2id" />
			</td>
		</tr>
	</c:when>
	<c:otherwise>
		<form:hidden path="organization2id" />
	</c:otherwise>
</c:choose>

<c:if test="${useServiceArea}">

		<tr>
			<th>
				<soak:label key="course.serviceArea" />
			</th>
			<td>
				<spring:bind path="course.serviceAreaid">
					<select name="<c:out value="${status.expression}"/>">
						<c:forEach var="servicearea" items="${serviceareas}">
							<c:choose>
								<c:when test="${empty servicearea.id}">
									<option value="<c:out value="${servicearea.id}"/>"
										<c:if test="${servicearea.id == course.serviceAreaid}"> selected="selected"</c:if>>
										<c:out value="${servicearea.name}" />(he1)
									</option>
								</c:when>
								<c:otherwise>
									<c:if test="${servicearea.organizationid == course.organizationid}">
										<option value="<c:out value="${servicearea.id}"/>"
											<c:if test="${servicearea.id == course.serviceAreaid}"> selected="selected"</c:if>>
											<c:out value="${servicearea.name}" />(he2)
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
		
		<tr>
			<th>
				<soak:label key="course.locationid" />
			</th>
			<td>
                <c:choose>
                    <c:when test="${filterlocation}">
                <spring:bind path="course.locationid">
					<select name="<c:out value="${status.expression}"/>">
						<c:forEach var="location" items="${locations}">
							<c:choose>
								<c:when test="${empty location.id}">
									<option value="<c:out value="${location.id}"/>"
										<c:if test="${location.id == course.locationid}"> selected="selected"</c:if>>
										<c:out value="${location.name}" />(he1)
									</option>
								</c:when>
								<c:otherwise>
									<c:if
										test="${location.organizationid == course.organizationid}">
										<option value="<c:out value="${location.id}"/>"
											<c:if test="${location.id == course.locationid}"> selected="selected"</c:if>>
											<c:out value="${location.name}" />(he2)
										</option>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<span class="fieldError"><c:out	value="${status.errorMessage}" escapeXml="false" />
                </spring:bind>
                    </c:when>
                    <c:otherwise>
                <form:select path="locationid" onchange="setMaxAttendants(this)">
					<form:options items="${locations}" itemValue="id" itemLabel="name" htmlEscape="true" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false" path="locationid" />
                    </c:otherwise>
                </c:choose>
            </td>
		</tr>

		<tr>
			<th>
				<soak:label key="course.responsible" />
			</th>
			<td>
				<form:select path="responsibleUsername">
					<form:options items="${responsible}" itemValue="username"
						itemLabel="fullName" htmlEscape="true" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="responsibleUsername" />
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="course.instructor" />
			</th>
			<td>
				<form:select path="instructorid">
					<form:options htmlEscape="true" items="${instructors}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="instructorid" />
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="course.maxAttendants" />
			</th>
			<td>
				<form:input path="maxAttendants" />
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="maxAttendants" />
			</td>
		</tr>

	<c:if test="${useAttendants}">
		<tr>
			<th>
				<soak:label key="course.attendants" />
			</th>
			<td>
				<form:input path="attendants" />
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="attendants" />
			</td>
		</tr>
	</c:if>

		<c:choose>
			<c:when test="${!singleprice && usePayment}">
				<tr>
					<th>
						<soak:label key="course.reservedInternal" />
					</th>
					<td>
						<form:input path="reservedInternal" />
						<form:errors cssClass="fieldError" htmlEscape="false"
							path="reservedInternal" />
					</td>
				</tr>

				<tr>
					<th>
						<soak:label key="course.feeInternal" />
					</th>
					<td>
						<form:input path="feeInternal" />
						<form:errors cssClass="fieldError" htmlEscape="false"
							path="feeInternal" />
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<form:hidden path="reservedInternal" />
				<form:hidden path="feeInternal" />
			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when test="${usePayment}">
				<tr>
					<th>
						<soak:label key="course.feeExternal" />
					</th>
					<td>
						<form:input path="feeExternal" />
						<form:errors cssClass="fieldError" htmlEscape="false"
							path="feeExternal" />
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<form:hidden path="feeExternal" />
			</c:otherwise>
		</c:choose>


	<c:if test="${!isSVV}">
		<tr>
			<th>
				<soak:label key="course.registerStart" />
			</th>
			<td>
				<fmt:formatDate value="${course.registerStart}" type="date"
					pattern="${dateformat}" var="registerStartDate" />
				<fmt:formatDate value="${course.registerStart}" type="time"
					pattern="${timeformat}" var="registerStartTime" />
				<input type="text" size="12" name="registerStartDate"
					id="registerStartDate"
					value="<c:out value="${registerStartDate}"/>"
					title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" />
				<a href="#" name="a1" id="Anch_registerStartDate"
					onClick="cal1.select(document.course.registerStartDate,'Anch_registerStartDate','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
				<soak:label key="course.time" />
				<input type="text" size="6" name="registerStartTime"
					id="registerStartTime"
					value="<c:out value="${registerStartTime}"/>"
					title="<fmt:message key="time.format.title"/>: <fmt:message key="time.format.localized"/>" />
				<spring:bind path="course.registerStart">
					<input type="hidden" name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value="<c:out value="${status.value}"/>" />
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>
	</c:if>

		<tr>
			<th>
				<soak:label key="course.reminder" />
			</th>
			<td>
				<fmt:formatDate value="${course.reminder}" type="date"
					pattern="${dateformat}" var="reminderDate" />
				<fmt:formatDate value="${course.reminder}" type="time"
					pattern="${timeformat}" var="reminderTime" />
				<input type="text" size="12" name="reminderDate" id="reminderDate"
					value="<c:out value="${reminderDate}"/>"
					title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" />
				<a href="#" name="a1" id="Anch_reminderDate"
					onClick="cal1.select(document.course.reminderDate,'Anch_reminderDate','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
				<soak:label key="course.time" />
				<input type="text" size="6" name="reminderTime" id="reminderTime"
					value="<c:out value="${reminderTime}"/>"
					title="<fmt:message key="time.format.title"/>: <fmt:message key="time.format.localized"/>" />
				<spring:bind path="course.reminder">
					<input type="hidden" name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value="<c:out value="${status.value}"/>" />
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>

<c:choose>
	<c:when test="${useRegisterBy}">
		<tr>
			<th>
				<soak:label key="course.registerBy" />
			</th>
			<td>
				<fmt:formatDate value="${course.registerBy}" type="date"
					pattern="${dateformat}" var="registerByDate" />
				<fmt:formatDate value="${course.registerBy}" type="time"
					pattern="${timeformat}" var="registerByTime" />
				<input type="text" size="12" name="registerByDate"
					id="registerByDate" value="<c:out value="${registerByDate}"/>"
					title="<fmt:message key="date.format.title"/>: <fmt:message key="date.format.localized"/>" />
				<a href="#" name="a1" id="Anch_registerByDate"
					onClick="cal1.select(document.course.registerByDate,'Anch_registerByDate','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
				<soak:label key="course.time" />
				<input type="text" size="6" name="registerByTime"
					id="registerByTime" value="<c:out value="${registerByTime}"/>"
					title="<fmt:message key="time.format.title"/>: <fmt:message key="time.format.localized"/>" />
				<spring:bind path="course.registerBy">
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>
	</c:when>
	<c:otherwise>
		<form:hidden path="registerBy" />
	</c:otherwise>
</c:choose>

		<c:choose>
			<c:when test="${usePayment}">
		        <tr>
		            <th>
		                <soak:label key="course.chargeoverdue" />
		            </th>
		            <td>
		                <form:checkbox path="chargeoverdue" />
		                <form:errors cssClass="fieldError" htmlEscape="false" path="chargeoverdue" />
		            </td>
		        </tr>
			</c:when>
			<c:otherwise>
				<form:hidden path="chargeoverdue" />
			</c:otherwise>
		</c:choose>
		
		<c:if test="${showAdditionalInfo}">
		<tr>
			<th>
				<soak:label key="course.additionalinfo" />
			</th>

			<td>
				<form:textarea cols="50" rows="3" path="additionalinfo"/>
				<form:errors cssClass="fieldError" htmlEscape="false" path="additionalinfo" />
			</td>
		</tr>
		</c:if>
		
		<tr>
			<th>
				<soak:label key="course.detailURL" />
			</th>
			<td>
				<form:input size="50" path="detailURL" maxlength="200"/>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="detailURL" />
			</td>
		</tr>

		<tr>
			<td></td>
			<td class="buttonBar">

				<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
					
					<input type="submit" class="button" name="save"
						onclick="bCancel=false"
						value="<fmt:message key="button.course.save"/>" />
					
					<c:if test="${(!isPublished && !isCancelled && !isSVV) || (!isPublished && !isCancelled && isSVV)}">
						<input type="submit" class="button large" name="publish"
							onclick="bCancel=false" id="publishButton"
							value="<fmt:message key="button.course.publish"/>" />
					</c:if>

					<c:if test="${(!isPublished && isCancelled && rePublishAllowed && !isSVV)}">
						<input type="submit" class="button large" name="publish"
							onclick="bCancel=false" id="publishButton"
							value="<fmt:message key="button.course.republishcancelled"/>" />
					</c:if>
					
					<c:if test="${isPublished && !isCancelled}">
						<input type="submit" class="button" name="cancelled"
							onclick="bCancel=true;return confirmCancellation('<fmt:message key="courseList.theitem"/>', '<c:out value="${cancelPrefix}"/>')"
							value="<fmt:message key="button.course.cancel"/>" />
					</c:if>
					
					<c:if test="${isPublished && canUnpublish}">
                        <input type="submit" class="button large" name="unpublish"
                            onclick="bCancel=false"
                            value="<fmt:message key="button.course.unpublish"/>" />
                    </c:if>
					
					<c:if test="${!empty course.id && !empty course.name && canDelete}">
						<input type="submit" class="button large" name="delete"
							onclick="bCancel=true;return confirmDelete('<fmt:message key="courseList.theitem"/>')"
							value="<fmt:message key="button.course.delete"/>" />
					</c:if>
				</c:if>

				<input type="submit" class="button" name="cancel"
					onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />

			</td>
		</tr>
	</table>
</form:form>

<v:javascript formName="course" cdata="false" dynamicJavascript="true" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>">
</script>

<script language="JavaScript">
	changeGUIBasedOnField('startTimeDate');
	fillSelect($p("organizationidElement"));
</script>
