<%@ include file="/common/taglibs.jsp"%>

<SCRIPT LANGUAGE="JavaScript" ID="js1">
var cal1 = new CalendarPopup();
cal1.setMonthNames('Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'); 
cal1.setDayHeaders('S','M','T','O','T','F','L'); 
cal1.setWeekStartDay(1); 
cal1.setTodayText("Idag");
</script>

<title><fmt:message key="statistics.name"/></title>
<content tag="heading">
<fmt:message key="statistics.name"/>
</content>

<c:choose>
<c:when test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">

<form method="post" action="<c:url value="/statistics.html"/>" name="statisticsForm" id="statisticsForm">
<div class="searchForm"><ul>
<li>
<label class="required" for="dateBeginInclusive">Fra dato:</label>
<input id="dateBeginInclusive" name="dateBeginInclusive" value="<c:out value="${dateBeginInclusive}"/>" type="text"/>
<a href="#" name="a1" id="Anch_dateBeginInclusive"
	onClick="cal1.select(document.statisticsForm.dateBeginInclusive,'Anch_dateBeginInclusive','<fmt:message key="date.format"/>'); return false;"
	title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
</li>
<li>
<label class="required" for="dateEndInclusive">Til dato:</label>
<input id="dateEndInclusive" name="dateEndInclusive" value="<c:out value="${dateEndInclusive}"/>" type="text"/>
<a href="#" name="a1" id="Anch_dateEndInclusive"
	onClick="cal1.select(document.statisticsForm.dateEndInclusive,'Anch_dateEndInclusive','<fmt:message key="date.format"/>'); return false;"
	title="<fmt:message key="course.calendar.title"/>"><img src="<c:url value="/images/calendar.png"/>"></a>
</li>
<li>
<input type="submit" name="Vis_statistikk" value="Vis statistikk" class="button large"/>
</li>
</ul>
</div>
</form>

<table class="list" style="width:auto;">
<thead>
<tr>
<th><fmt:message key="statistics.unit"/></th>
<th><fmt:message key="statistics.numCourses"/></th>
<th><fmt:message key="statistics.numRegistrations"/></th>
<th><fmt:message key="statistics.numRegistered"/></th>
<th><fmt:message key="statistics.numAttended"/></th>
</tr>
</thead>

<c:forEach items="${statisticsRows}" var="statRow">
<tr class="<c:out value="${statRow.cssClass}"/>">
<td><c:if test="${empty statRow.cssClass}"><div class="indentleft"></c:if><c:out value="${statRow.unit}"/><c:if test="${empty statRow.cssClass}"></div></c:if></td>
<td align="right">
<c:out value="${statRow.numCourses}"/>
</td>
<td align="right">
<c:out value="${statRow.numRegistrations}"/>
</td>
<td align="right">
<c:out value="${statRow.numRegistered}"/>
</td>
<td align="right">
<c:out value="${statRow.numAttendants}"/>
</td>
</tr>
</c:forEach>

</table>

<p/><br/>
<h2>Forklaring</h2>
<ul class="bulletlist">
<li>"<b>Ant. fremmøtte</b>": Tas fra oppmøteregistreringen som er gjort for møtet. Dersom denne ikke er gjort, tas summen av "ant. påmeldte" for møtets påmeldinger. Tallet beregnes for møter innen området/regionen i statistikkperioden.
</li>

<li>"<b>Ant. påmeldte</b>": Summen av feltet "Antall deltakere" for påmeldingene til møter innen området/regionen i statistikkperioden.
</li>
<li>"<b>Ant. representerte førerkortkandidater</b>": Antall påmeldinger til møter innen området/regionen i statistikkperioden.
</li>

<li>Upubliserte møter teller med på linje med publiserte møter.
</li>
</ul>

<br/>

<c:if test="${!empty courseList}">
<h2>Informasjonsmøter uten påmeldinger/registering av deltakere</h2>

<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>
<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>

<display:table name="${courseList}" cellspacing="0" cellpadding="0"
    id="courseList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="listCourses.html">

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible && username == courseList.responsible.username || (isSVV && courseList.organization2id == user.organization2id)}">
        <a href='<c:url value="/editCourse.html"><c:param name="id" value="${courseList.id}"/><c:param name="from" value="list"/></c:url>' target="_blank">
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
</c:if>
    </display:column>
</c:if>

<c:if test="${showCourseName}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
        <c:if test="${courseList.status == 3}"><img src="<c:url value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseList.description}"/>"><c:out value="${courseList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.status">
        <c:if test="${courseList.status == 0}"><img src="<c:url value="/images/add.png"/>" alt="<fmt:message key="course.status.created"/>" title="<fmt:message key="course.status.created"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 1}"><img src="<c:url value="/images/stop.png"/>" alt="<fmt:message key="course.status.finished"/>" title="<fmt:message key="course.status.finished"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 2}"><img src="<c:url value="/images/accept.png"/>" alt="<fmt:message key="course.status.published"/>" title="<fmt:message key="course.status.published"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 3}"><img src="<c:url value="/images/cancel.png"/>" alt="<fmt:message key="course.status.cancelled"/>" title="<fmt:message key="course.status.cancelled"/>" class="icon"/></c:if>
    </display:column>
</c:if>

<c:choose>
<c:when test="${showCourseName}">
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
		<fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:when>
<c:otherwise>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseList.description}"/>"><fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
		<fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:otherwise>    
</c:choose>

<c:if test="${useRegisterBy}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.registerBy" sortProperty="registerBy">
		<c:choose>
			<c:when test="${courseList.expired}">
				<i><fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}" /></i>
			</c:when>
			<c:otherwise>
				<fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}" />
			</c:otherwise>
		</c:choose>
	</display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.registerBy.export">
        <fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}" />
    </display:column>
</c:if>

<c:if test="${showDuration}">
    <display:column property="duration" sortable="true" headerClass="sortable"
         titleKey="course.duration"/>
</c:if>
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="course.organization"/>

<c:if test="${useOrganization2 && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
    <display:column property="organization2.name" sortable="true" headerClass="sortable"
         titleKey="course.organization2"/>
</c:if>

<c:if test="${useServiceArea}">
    <display:column property="serviceArea.name" sortable="true" headerClass="sortable"
         titleKey="course.serviceArea"/>
</c:if>
         
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>
	
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out value="${courseList.responsible.fullName}"/></a>
    </display:column>
    
    <display:column media="csv excel xml pdf" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:column media="excel" property="instructor.name" sortable="true" headerClass="sortable" titleKey="course.instructor.export"/>

    <display:column media="excel" property="maxAttendants" sortable="true" headerClass="sortable" titleKey="course.maxAttendants.export"/>

    <display:column media="excel" property="reservedInternal" sortable="true" headerClass="sortable" titleKey="course.reservedInternal.export"/>

    <display:column media="excel" property="feeInternal" sortable="true" headerClass="sortable" titleKey="course.feeInternal.export"/>

    <display:column media="excel" property="feeExternal" sortable="true" headerClass="sortable" titleKey="course.feeExternal.export"/>

    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.registerStart.export" sortProperty="registerStart">
         <fmt:formatDate value="${courseList.registerStart}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

	<c:if test="${useRegisterBy}">
    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.registerBy.export" sortProperty="registerBy">
         <fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
	</c:if>

    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.reminder.export" sortProperty="reminder">
         <fmt:formatDate value="${courseList.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column media="excel" property="description" sortable="true" headerClass="sortable" titleKey="course.description.export"/>

    <display:column media="excel" property="detailURL" sortable="true" headerClass="sortable" titleKey="course.detailURL.export"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>
</c:if>

</c:when> 
<c:otherwise>
    <div class="message" style="font-size: 12px"><fmt:message key="access.denied" /></div>
</c:otherwise>
</c:choose>