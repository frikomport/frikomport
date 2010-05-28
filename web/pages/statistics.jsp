<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="statistics.name"/></title>
<content tag="heading">
<fmt:message key="statistics.name"/>
</content>

<form method="post" action="<c:url value="/statistics.html"/>" name="statisticsForm" id="statisticsForm">
<div class="searchForm"><ul>
<li>
<label class="required" for="dateBeginInclusive">Fra dato:</label>
<input id="dateBeginInclusive" name="dateBeginInclusive" value="<c:out value="${dateBeginInclusive}"/>" type="text"/>
</li>
<li>
<label class="required" for="dateEndInclusive">Til dato:</label>
<input id="dateEndInclusive" name="dateEndInclusive" value="<c:out value="${dateEndInclusive}"/>" type="text"/>
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
<li>"<b>Ant. oppmøtte</b>": Tas fra oppmøteregistreringen som er gjort for møtet. Dersom denne ikke er gjort, tas summen av "ant. påmeldte" for møtets påmeldinger. Tallet beregnes for møter innen området/regionen i statistikkperioden.
</li>

<li>"<b>Ant. påmeldte</b>": Summen av feltet "Antall deltakere" for påmeldingene til møter innen området/regionen i statistikkperioden.
</li>
<li>"<b>Ant. representerte førerkortkandidater</b>": Antall påmeldinger til møter innen området/regionen i statistikkperioden.
</li>

<li>Upubliserte møter teller med på linje med publiserte møter.
</li>
</ul>