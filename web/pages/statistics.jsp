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

<!--
<c:if test="${!isAdmin}">
    <div class="message" style="font-size: 12px"><fmt:message key="access.denied" /></div>
</c:if> 
-->
<p/><br/>
<h2>Forklaring</h2>
<ul class="bulletlist">
<li>"<b>Ant. oppmøtte</b>": Tas fra oppmøteregistreringen som er gjort for møtet. Dersom denne ikke er gjort, tas summen av "ant. påmeldte" for møtets påmeldinger. Tallet beregnes for møter innen området/regionen i statistikkperioden.
</li>

<li>"<b>Ant. påmeldte</b>": Summen av feltet "Antall deltakere" for påmeldingene til møter innen området/regionen i statistikkperioden.
</li>
<li>"<b>Ant. representerte førerkortkandidater</b>": Antall påmeldinger til møter innen området/regionen i statistikkperioden.
</li>

<li>Upubliserte møter teller med på linje med publiserte møter.
</li>
</ul>