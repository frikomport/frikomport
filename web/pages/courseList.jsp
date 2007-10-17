<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseList.title"/></title>
<content tag="heading"><fmt:message key="courseList.heading"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>

<form method="post" action="<c:url value="/listCourses.html"/>" id="courseList">
   	<INPUT type="hidden" id="ispostbackcourselist" name="ispostbackcourselist" value="1"/> 
	<table>
		<tr>
		    <th>
		        <soak:label key="course.municipality"/>
		    </th>
		    <td>
		        <spring:bind path="course.municipalityid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="municipality" items="${municipalities}">
					      <option value="<c:out value="${municipality.id}"/>"
						      <c:if test="${municipality.id == course.municipalityid}"> selected="selected"</c:if>>
					        <c:out value="${municipality.name}"/>
					      </option>
					    </c:forEach>
					  </select>            
		            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
		        </spring:bind>
		    </td>
		    <th>
		        <soak:label key="course.serviceArea"/>
		    </th>
		    <td>
		        <spring:bind path="course.serviceAreaid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="servicearea" items="${serviceareas}">
					      <option value="<c:out value="${servicearea.id}"/>"
						      <c:if test="${servicearea.id == course.serviceAreaid}"> selected="selected"</c:if>>
					        <c:out value="${servicearea.name}"/>
					      </option>
					    </c:forEach>
					  </select>
		            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
		        </spring:bind>
		    </td>
		    
	    	<INPUT type="hidden" id="past" name="past" 
	    	<c:if test="${past == true}"> value="1" </c:if>
	    	<c:if test="${past == false}"> value="0" </c:if> 
	    	/>

<c:if test="${past == true}">
	    	<INPUT type="hidden" id="historic" name="historic" 
	    	<c:if test="${historic == true}"> value="1" </c:if>
	    	<c:if test="${historic == false}"> value="0" </c:if> 
	    	/>
</c:if> 		    
<c:if test="${past == false}">
		    <th>
				<label><fmt:message key="course.includeHistoric"/></label>
		    </th>
		    <td>
		    	<INPUT type="hidden" name="_historic" value="0"/>
		    	<INPUT type="checkbox" id="historic" name="historic" value="1" 
		    	<c:if test="${historic == true}"> checked </c:if> />

		    </td>
</c:if> 		    

	        <td class="buttonBar">            
				<button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
					<fmt:message key="button.search"/>
				</button>
			</td>
		</tr>
<%--
		<tr>
			<th>
				<soak:label key="courseSearch.dateStart"/>
			</th>
			<td>
		        <spring:bind path="course.startTime">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>		        
		        </spring:bind>
			</td>
			<th>
				<soak:label key="courseSearch.dateStop"/>
			</th>
			<td>
		        <spring:bind path="course.stopTime">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>		        
		        </spring:bind>
			</td>
			<th>
				<soak:label key="courseSearch.name"/>
			</th>
			<td>
		        <spring:bind path="course.name">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>		        
		        </spring:bind>
			</td>
			<th>
				<soak:label key="courseSearch.type"/>
			</th>
			<td>
		        <spring:bind path="course.type">
	                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
	                    value="<c:out value="${status.value}"/>" />
	                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>		        
		        </spring:bind>
			</td>
		</tr>
--%>
	</table>
</form>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editCourse.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${courseList}" cellspacing="0" cellpadding="0"
    id="courseList" pagesize="25" class="list" 
    export="true" requestURI="listCourses.html">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseList.description}"/>"><c:out value="${courseList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>
    
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime">
         <fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column property="duration" sortable="true" headerClass="sortable"
         titleKey="course.duration"/>

    <display:column property="municipality.name" sortable="true" headerClass="sortable"
         titleKey="course.municipality"/>

    <display:column property="serviceArea.name" sortable="true" headerClass="sortable"
         titleKey="course.serviceArea"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<fmt:message key="course.responsible.urlpart"/>/<c:out value="${courseList.responsible.url_name}"/>" target="_top"><c:out value="${courseList.responsible.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="responsible.name" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || (isCourseResponsible && eZUserid == courseList.responsibleid)}">
	    <button type="button" onclick="location.href='<c:url value="/editCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>'">
    	    <fmt:message key="button.edit"/>
	    </button>
</c:if>
    </display:column>
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("courseList");
</script>
--%>
