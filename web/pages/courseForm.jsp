<%@ include file="/common/taglibs.jsp"%>

<SCRIPT LANGUAGE="JavaScript" ID="js1">
var cal1 = new CalendarPopup();
cal1.setMonthNames('Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'); 
cal1.setDayHeaders('S','M','T','O','T','F','L'); 
cal1.setWeekStartDay(1); 
cal1.setTodayText("Idag");
</SCRIPT>

<title><fmt:message key="courseEdit.title"/></title>
<c:choose>
<c:when test="${empty course.id && !empty course.name}">
	<content tag="heading"><fmt:message key="courseEdit.heading.copy"/></content>
</c:when>
<c:when test="${empty course.id && empty course.name}">
	<content tag="heading"><fmt:message key="courseEdit.heading.new"/></content>
</c:when>
<c:when test="${!empty course.id && !empty course.name}">
	<content tag="heading"><fmt:message key="courseEdit.heading.edit"/></content>
</c:when>
<c:otherwise>
	<content tag="heading"><fmt:message key="courseEdit.heading"/></content>
</c:otherwise>
</c:choose>

<spring:bind path="course.*">
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

<form:form commandName="course" onsubmit="return validateCourse(this)">

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="date.format.localized" var="datelocalized"/>
<fmt:message key="time.format.localized" var="timelocalized"/>
<fmt:message key="access.course.singleprice" var="singleprice"/>

<table class="detail">

    <form:hidden path="id"/>

    <tr>
        <th>
            <soak:label key="course.name"/>
        </th>
        <td>
            <form:input size="50" path="name"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="description"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.description"/>
        </th>
        <td>
            <form:textarea cols="50" rows="3"  path="description"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="description"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.role"/>
        </th>
        <td>
            <form:select path="role" items="${roles}"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="role"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.type"/>
        </th>
        <td>
            <form:input path="type" maxlength="50"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="type"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.startTime"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.startTime}" type="date" pattern="${dateformat}" var="startTimeDate"/>
           	<fmt:formatDate value="${course.startTime}" type="time" pattern="${timeformat}" var="startTimeTime"/>
            <input type="text" size="12" name="startTimeDate" id="startTimeDate" 
                value="<c:out value="${startTimeDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_startTimeDate" 
				onClick="cal1.select(document.forms[0].startTimeDate,'Anch_startTimeDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="startTimeTime" id="startTimeTime" 
                value="<c:out value="${startTimeTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.startTime">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<fmt:formatDate value="${time[0]}" type="date" pattern="${dateformat}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.stopTime"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.stopTime}" type="date" pattern="${dateformat}" var="stopTimeDate"/>
           	<fmt:formatDate value="${course.stopTime}" type="time" pattern="${timeformat}" var="stopTimeTime"/>
            <input type="text" size="12" name="stopTimeDate" id="stopTimeDate" 
                value="<c:out value="${stopTimeDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_stopTimeDate" 
				onClick="cal1.select(document.forms[0].stopTimeDate,'Anch_stopTimeDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="stopTimeTime" id="stopTimeTime" 
                value="<c:out value="${stopTimeTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.stopTime">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<fmt:formatDate value="${time[0]}" type="date" pattern="${dateformat}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.duration"/>
        </th>
        <td>
            <form:input path="duration" size="50"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="duration"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.organization"/>
        </th>
        <td>
            <form:select path="organizationid">
                <form:options items="${organizations}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="organizationid"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.serviceArea"/>
        </th>
        <td>
            <form:select path="serviceAreaid">
                <form:options items="${serviceareas}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="serviceAreaid"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.location"/>
        </th>
        <td>
            <form:select path="locationid">
                <form:options items="${locations}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="locationid"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.responsible"/>
        </th>
        <td>
            <form:select path="responsibleid">
                <form:options items="${responsible}" itemValue="id" itemLabel="fullName"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="responsibleid"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.instructor"/>
        </th>
        <td>
            <form:select path="instructorid">
                <form:options items="${instructors}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="instructorid"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.maxAttendants"/>
        </th>
        <td>
            <form:input path="maxAttendants"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="maxAttendants"/>
        </td>
    </tr>

	<c:choose>
	<c:when test="${!singleprice}">
	<tr>
        <th>
            <soak:label key="course.reservedInternal"/>
        </th>
        <td>
            <form:input path="reservedInternal"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="reservedInternal"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.feeInternal"/>
        </th>
        <td>
            <form:input path="feeInternal"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="feeInternal"/>
        </td>
    </tr>
	</c:when>
	<c:otherwise>
        <form:hidden path="reservedInternal"/>
        <form:hidden path="feeInternal"/>
    </c:otherwise>
	</c:choose>
	
    <tr>
        <th>
            <soak:label key="course.feeExternal"/>
        </th>
        <td>
            <form:input path="feeExternal"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="feeExternal"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.registerStart"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.registerStart}" type="date" pattern="${dateformat}" var="registerStartDate"/>
           	<fmt:formatDate value="${course.registerStart}" type="time" pattern="${timeformat}" var="registerStartTime"/>
            <input type="text" size="12" name="registerStartDate" id="registerStartDate" 
                value="<c:out value="${registerStartDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_registerStartDate" 
				onClick="cal1.select(document.forms[0].registerStartDate,'Anch_registerStartDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="registerStartTime" id="registerStartTime" 
                value="<c:out value="${registerStartTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.registerStart">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.reminder"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.reminder}" type="date" pattern="${dateformat}" var="reminderDate"/>
           	<fmt:formatDate value="${course.reminder}" type="time" pattern="${timeformat}" var="reminderTime"/>
            <input type="text" size="12" name="reminderDate" id="reminderDate" 
                value="<c:out value="${reminderDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_reminderDate" 
				onClick="cal1.select(document.forms[0].reminderDate,'Anch_reminderDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="reminderTime" id="reminderTime" 
                value="<c:out value="${reminderTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.reminder">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.registerBy"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.registerBy}" type="date" pattern="${dateformat}" var="registerByDate"/>
           	<fmt:formatDate value="${course.registerBy}" type="time" pattern="${timeformat}" var="registerByTime"/>
            <input type="text" size="12" name="registerByDate" id="registerByDate" 
                value="<c:out value="${registerByDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_registerByDate" 
				onClick="cal1.select(document.forms[0].registerByDate,'Anch_registerByDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="registerByTime" id="registerByTime" 
                value="<c:out value="${registerByTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.registerBy">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<fmt:formatDate value="${time[0]}" type="date" pattern="${dateformat}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.freezeAttendance"/>
        </th>
        <td>
           	<fmt:formatDate value="${course.freezeAttendance}" type="date" pattern="${dateformat}" var="freezeAttendanceDate"/>
           	<fmt:formatDate value="${course.freezeAttendance}" type="time" pattern="${timeformat}" var="freezeAttendanceTime"/>
            <input type="text" size="12" name="freezeAttendanceDate" id="freezeAttendanceDate" 
                value="<c:out value="${freezeAttendanceDate}"/>" title="Datoformat: <fmt:message key="date.format.localized"/>"/>
            <a href="#" name="a1" id="Anch_freezeAttendanceDate" 
				onClick="cal1.select(document.forms[0].freezeAttendanceDate,'Anch_freezeAttendanceDate','<fmt:message key="date.format"/>'); return false;" 
				title="Vis kalender"><img src="<c:url value="/images/iconCalendar.gif"/>"></a>
            <soak:label key="course.time"/>
            <input type="text" size="6" name="freezeAttendanceTime" id="freezeAttendanceTime" 
                value="<c:out value="${freezeAttendanceTime}"/>" title="Tidsformat: <fmt:message key="time.format.localized"/>"/>
            <spring:bind path="course.freezeAttendance">
                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<fmt:formatDate value="${time[0]}" type="date" pattern="${dateformat}"/>"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="course.detailURL"/>
        </th>
        <td>
            <form:input size="50" path="detailURL"/>
            <form:errors cssClass="fieldError" htmlEscape="false" path="detailURL"/>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />

			<c:if test="${!empty course.id && !empty course.name && canDelete}">
	            <input type="submit" class="button" name="delete"
	                onclick="bCancel=true;return confirmDelete('<fmt:message key="courseList.theitem"/>')" 
	                value="<fmt:message key="button.delete"/>" />
            </c:if>
</c:if>

            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />

			<c:if test="${!empty course.id && !empty course.name}">
<c:choose>
	<c:when test="${isAdmin || isEducationResponsible || isCourseResponsible}">
			    <button type="button" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.administerRegistrations"/>
			    </button>
	</c:when>
	<c:otherwise>
		<c:if test="${isCourseParticipant}">
			    <button type="button" onclick="location.href='<c:url value="/administerRegistration.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.displayRegistrations"/>
			    </button>
		</c:if>
	</c:otherwise>
</c:choose>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
			    <button type="button" onclick="location.href='<c:url value="/editFileCourse.html"><c:param name="courseid" value="${course.id}"/></c:url>'">
		    	    <fmt:message key="button.administerFiles"/>
			    </button>
</c:if>
			</c:if>
        </td>
    </tr>
</table>
</form:form>

<v:javascript formName="course" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>