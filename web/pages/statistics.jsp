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
<tr>
<td>
<c:out value="${statRow.unit}"/>
</td>
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
<li>"<b>Ant. oppm�tte</b>": Tas fra oppm�teregistreringen som er gjort for m�tet. Dersom denne ikke er gjort, tas summen av "ant. p�meldte" for m�tets p�meldinger. Tallet beregnes for m�ter innen omr�det/regionen i statistikkperioden.
</li>

<li>"<b>Ant. p�meldte</b>": Summen av feltet "Antall deltakere" for p�meldingene til m�ter innen omr�det/regionen i statistikkperioden.
</li>
<li>"<b>Ant. representerte f�rerkortkandidater</b>": Antall p�meldinger til m�ter innen omr�det/regionen i statistikkperioden.
</li>

<li>Upubliserte m�ter teller med p� linje med publiserte m�ter.
</li>
</ul>