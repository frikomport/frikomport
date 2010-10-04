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

<form method="post" action="<c:url context="${urlContext}" value="/statistics.html"/>" name="statisticsForm" id="statisticsForm">
<div class="searchForm"><ul>
<li>
<label class="required" for="dateBeginInclusive">Fra dato:</label>
<input id="dateBeginInclusive" name="dateBeginInclusive" value="<c:out value="${dateBeginInclusive}"/>" type="text"/>
<a href="#" name="a1" id="Anch_dateBeginInclusive"
	onClick="cal1.select(document.statisticsForm.dateBeginInclusive,'Anch_dateBeginInclusive','<fmt:message key="date.format"/>'); return false;"
	title="<fmt:message key="course.calendar.title"/>"><img src="<c:url context="${urlContext}" value="/images/calendar.png"/>"></a>
</li>
<li>
<label class="required" for="dateEndInclusive">Til dato:</label>
<input id="dateEndInclusive" name="dateEndInclusive" value="<c:out value="${dateEndInclusive}"/>" type="text"/>
<a href="#" name="a1" id="Anch_dateEndInclusive"
	onClick="cal1.select(document.statisticsForm.dateEndInclusive,'Anch_dateEndInclusive','<fmt:message key="date.format"/>'); return false;"
	title="<fmt:message key="course.calendar.title"/>"><img src="<c:url context="${urlContext}" value="/images/calendar.png"/>"></a>
</li>
<li>
<input type="submit" name="Vis_statistikk" value="Vis statistikk" class="button large"/>
</li>
</ul>
</div>
</form>

<c:if test="${not empty statisticsRows}">
	<fmt:message key="statisticsList.item" var="item"/>
	<fmt:message key="statisticsList.items" var="items"/>

	<display:table name="${statisticsRows}" cellspacing="0" cellpadding="0" 
	    id="statisticsRows" pagesize="${itemCount}" class="list" export="true" requestURI="statistics.html">
		
		<!--  REGION -->
		<c:set var="levelTd" value="level2" />
		<display:setProperty name="css.tr.even" value=""/>
		<display:setProperty name="css.tr.odd" value=""/>

		<!--  HELE LANDET / SUM -->
		<c:if test="${empty statisticsRows.unitParent}">
			<c:set var="levelTd" value="level1" />
		</c:if>

		<!--  OMR�DE -->
		<c:if test="${not empty statisticsRows.unitParent && empty statisticsRows.cssClass}">
			<c:set var="levelTd" value="level3_name" />
		</c:if>
	
	
	    <display:column titleKey="statistics.unit" class="${levelTd}">
	    	<c:out value="${statisticsRows.unit}"/>
	    </display:column>

		<!--  OMR�DE -->
		<c:if test="${not empty statisticsRows.unitParent && empty statisticsRows.cssClass}">
			<c:set var="levelTd" value="level3" />
		</c:if>

	    <display:column titleKey="statistics.numCourses" class="${levelTd}">
	    	<c:out value="${statisticsRows.numCourses}"/>
	    </display:column>
	
	    <display:column titleKey="statistics.numRegistrations" class="${levelTd}">
	    	<c:out value="${statisticsRows.numRegistrations}"/>
	    </display:column>
	
	    <display:column titleKey="statistics.numRegistered" class="${levelTd}">
	    	<c:out value="${statisticsRows.numRegistered}"/>
	    </display:column>
	
	    <display:column titleKey="statistics.numAttended" class="${levelTd}">
	    	<c:out value="${statisticsRows.numAttendants}"/>
	    </display:column>

	    <display:setProperty name="paging.banner.item_name" value="${item}"/>
	    <display:setProperty name="paging.banner.items_name" value="${items}"/>
	    <display:setProperty name="export.xml" value="false"/>
	</display:table>
</c:if>

<h2>Forklaring</h2>
<ul class="bulletlist">
	<li>"<b>Antall p�meldinger</b>": Antall p�meldinger (representerte f�rerkortkandidater) til m�ter innen omr�det/regionen i statistikkperioden.</li>
	<li>"<b>Antall p�meldte</b>": Summen av feltet "Antall deltakere" for p�meldingene til m�ter innen omr�det/regionen i statistikkperioden.</li>
	<li>"<b>Antall fremm�tte</b>": Tas fra oppm�teregistreringen som er gjort for m�tet. Dersom denne ikke er gjort, tas summen av "Antall p�meldte" for m�tets p�meldinger. Tallet beregnes for m�ter innen omr�det/regionen i statistikkperioden.</li>
	<li>Upubliserte m�ter teller med p� linje med publiserte m�ter.</li>
</ul>

<p/><br/>

<c:if test="${!empty courseList}">
<h2>Informasjonsm�ter som b�de er uten p�meldinger og etterregistering av fremm�tte deltakere</h2>

<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>
<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>

<display:table name="${courseList}" cellspacing="0" cellpadding="0"
    id="courseList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="statistics.html">

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible && username == courseList.responsible.username || (isSVV && courseList.organization2id == user.organization2id)}">
        <a href='<c:url context="${urlContext}" value="/editCourse.html"><c:param name="id" value="${courseList.id}"/><c:param name="from" value="list"/></c:url>' target="_blank">
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
</c:if>
    </display:column>
</c:if>

<c:if test="${showCourseName}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
        <c:if test="${courseList.status == 3}"><img src="<c:url context="${urlContext}" value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseList.description}"/>"><c:out value="${courseList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.status">
        <c:if test="${courseList.status == 0}"><img src="<c:url context="${urlContext}" value="/images/add.png"/>" alt="<fmt:message key="course.status.created"/>" title="<fmt:message key="course.status.created"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 1}"><img src="<c:url context="${urlContext}" value="/images/stop.png"/>" alt="<fmt:message key="course.status.finished"/>" title="<fmt:message key="course.status.finished"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 2}"><img src="<c:url context="${urlContext}" value="/images/accept.png"/>" alt="<fmt:message key="course.status.published"/>" title="<fmt:message key="course.status.published"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 3}"><img src="<c:url context="${urlContext}" value="/images/cancel.png"/>" alt="<fmt:message key="course.status.cancelled"/>" title="<fmt:message key="course.status.cancelled"/>" class="icon"/></c:if>
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.status">
        <c:if test="${courseList.status == 0}"><fmt:message key="course.status.created"/></c:if>
        <c:if test="${courseList.status == 1}"><fmt:message key="course.status.finished"/></c:if>
        <c:if test="${courseList.status == 2}"><fmt:message key="course.status.published"/></c:if>
        <c:if test="${courseList.status == 3}"><fmt:message key="course.status.cancelled"/></c:if>
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
         <a href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
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
         <a href="<c:url context="${urlContext}" value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>
	
	<display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url context="${urlContext}" value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out value="${courseList.responsible.fullName}"/></a>
    </display:column>
    
    <display:column media="csv excel xml pdf" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
    <display:setProperty name="export.xml" value="false"/>

</display:table>
</c:if>

</c:when> 
<c:otherwise>
    <div class="message" style="font-size: 12px"><fmt:message key="access.denied" /></div>
</c:otherwise>
</c:choose>