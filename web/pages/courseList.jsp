<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseList.title"/></title>
<content tag="heading"><fmt:message key="courseList.heading"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>

<form method="post" action="<c:url value="/listCourses.html"/>" id="courseList">
   	<input type="hidden" id="ispostbackcourselist" name="ispostbackcourselist" value="1"/> 
	<table>
		<tr>
		    <th>
		        <soak:label key="course.organization"/>
		    </th>
		    <td>
		        <spring:bind path="course.organizationid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="organization" items="${organizations}">
					      <option value="<c:out value="${organization.id}"/>"
						      <c:if test="${organization.id == course.organizationid}"> selected="selected"</c:if>>
					        <c:out value="${organization.name}"/>
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

		    <th>
		        <soak:label key="courseSearch.name"/>
		    </th>
		    <td>
		    	<spring:bind path="course.name">
		    		<input type="text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15"/>
		    	</spring:bind>
		    </td>
		</tr>
		<tr>
	    	<input type="hidden" id="past" name="past" 
	    	<c:if test="${past == true}"> value="1" </c:if>
	    	<c:if test="${past == false}"> value="0" </c:if> 
	    	/>

<c:if test="${past == true}">
	    	<input type="hidden" id="historic" name="historic" 
	    	<c:if test="${historic == true}"> value="1" </c:if>
	    	<c:if test="${historic == false}"> value="0" </c:if> 
	    	/>
</c:if> 		    
<c:if test="${past == false}">
		    <th>
				<label><fmt:message key="course.includeHistoric"/></label>
		    </th>
		    <td>
		    	<input type="hidden" name="_historic" value="0"/>
		    	<input type="checkbox" id="historic" name="historic" value="1" 
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
				<soak:label key="courseSearch.availableAttendants"/>
			</th>
			<td>
		        <spring:bind path="course.availableAttendants">
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
    
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
         <fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column media="excel" property="stopTime" sortable="true" headerClass="sortable" titleKey="course.stopTime.export"/>
    
    <display:column property="availableAttendants" sortable="true" headerClass="sortable" titleKey="course.availableAttendants"/>

    <display:column property="duration" sortable="true" headerClass="sortable"
         titleKey="course.duration"/>

    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="course.organization"/>

    <display:column property="serviceArea.name" sortable="true" headerClass="sortable"
         titleKey="course.serviceArea"/>
         
    <display:column media="csv excel xml pdf" property="type" sortable="true" headerClass="sortable" titleKey="course.type.export"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url value="/detailsUser.html"><c:param name="id" value="${courseList.responsible.id}"/></c:url>"><c:out value="${courseList.responsible.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="responsible.name" sortable="true" headerClass="sortable" titleKey="course.responsible"/>

    <display:column media="excel" property="instructor.name" sortable="true" headerClass="sortable" titleKey="course.instructor.export"/>

    <display:column media="excel" property="maxAttendants" sortable="true" headerClass="sortable" titleKey="course.maxAttendants.export"/>

    <display:column media="excel" property="reservedMunicipal" sortable="true" headerClass="sortable" titleKey="course.reservedMunicipal.export"/>

    <display:column media="excel" property="feeMunicipal" sortable="true" headerClass="sortable" titleKey="course.feeMunicipal.export"/>

    <display:column media="excel" property="feeExternal" sortable="true" headerClass="sortable" titleKey="course.feeExternal.export"/>

    <display:column media="excel" property="registerStart" sortable="true" headerClass="sortable" titleKey="course.registerStart.export"/>

    <display:column media="excel" property="registerBy" sortable="true" headerClass="sortable" titleKey="course.registerBy.export"/>

    <display:column media="excel" property="reminder" sortable="true" headerClass="sortable" titleKey="course.reminder.export"/>

    <display:column media="excel" property="freezeAttendance" sortable="true" headerClass="sortable" titleKey="course.freezeAttendance.export"/>

    <display:column media="excel" property="description" sortable="true" headerClass="sortable" titleKey="course.description.export"/>

    <display:column media="excel" property="detailURL" sortable="true" headerClass="sortable" titleKey="course.detailURL.export"/>

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
