<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="no.unified.soak.webapp.util.SumBuild"%>
<%@page import="no.unified.soak.model.Course"%>

<title><fmt:message key="courseList.title"/></title>
<content tag="heading"><fmt:message key="courseList.heading"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>

<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>

<SCRIPT LANGUAGE="JavaScript" ID="js1">
var cal1 = new CalendarPopup();
cal1.setMonthNames('Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'); 
cal1.setDayHeaders('S','M','T','O','T','F','L'); 
cal1.setWeekStartDay(1); 
cal1.setTodayText("Idag");
</script>

<script type="text/javascript">
// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
	var orgid=obj.options[obj.selectedIndex].value;
	<c:if test="${useServiceArea}">
		var serviceArea= document.courseList.serviceAreaid;
	    while(serviceArea.firstChild){
	        serviceArea.removeChild(serviceArea.firstChild);
	    }
	
	    var j = 0;
		<c:forEach var="servicearea" items="${serviceareas}">
		    if ("<c:out value="${servicearea.id}"/>" == ""){
		        serviceArea.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>", true);
		        j++;
		    }
		    else if ("<c:out value="${servicearea.organizationid}"/>" == orgid){
		        serviceArea.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>");
		        j++ ;
		    }
		</c:forEach>
	</c:if>

	<c:if test="${filterlocation || isSVV}">
	    var location = document.courseList.locationid;
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
	        location.options[k]=new Option("<c:out value="${location.name}"/>", "<c:out value="${location.id}"/>", select);
	        k++ ;
	    }
		</c:forEach>
	</c:if>
}
</script>

<form:form commandName="course" name="courseList" action="listCourses.html">
    <div class="searchForm">
        <ul>
<c:if test="${useOrganization2 && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
            <li>
                <form:select  path="organization2id">
                    <form:options items="${organizations2}" itemValue="id" itemLabel="name" />
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="organization2id" />
            </li>
</c:if>

            <li>
                <form:select  path="organizationid" onchange="fillSelect(this);" id="organizationidElement">
                    <form:options items="${organizations}" itemValue="id" itemLabel="name" />
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="organizationid" />
            </li>

<c:if test="${useServiceArea}">
            <li>
                <spring:bind path="course.serviceAreaid">
					<select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
						<c:forEach var="servicearea" items="${serviceareas}">
							<c:choose>
								<c:when test="${empty servicearea.id}">
									<option value="<c:out value="${servicearea.id}"/>"
										<c:if test="${servicearea.id == course.serviceAreaid}"> selected="selected"</c:if>>
										<c:out value="${servicearea.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<c:if
										test="${servicearea.organizationid == course.organizationid}">
										<option value="<c:out value="${servicearea.id}"/>"
											<c:if test="${servicearea.id == course.serviceAreaid}"> selected="selected"</c:if>>
											<c:out value="${servicearea.name}" />
										</option>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
            </li>
</c:if>
            
<c:if test="${categories[2] != null}">
            <li>
                <soak:label key="course.category" styleClass="required"/>
                <form:select path="categoryid" items="${categories}" itemLabel="name" itemValue="id">
                    <form:options items="${categories}" itemLabel="name" itemValue="id"/>
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="categoryid" />
            </li>
</c:if>
            <li>
                <form:select  path="locationid">
                    <form:options items="${locations}" itemValue="id" itemLabel="name" />
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="locationid" />
            </li>

<c:if test="${showCourseName}">
            <li>
                <soak:label key="courseSearch.name" styleClass="required"/>
                <form:input path="name"/>
                <form:errors cssClass="fieldError" htmlEscape="false" path="name" />
            </li>
</c:if>

			<li>
				<label class="required">Fra:</label>
				<input id="startTime" name="startTime" value="<fmt:formatDate value="${startTime}" pattern="${dateformat}"/>" type="text" size="12" />
				<a href="#" name="a1" id="Anch_startTime"
					onClick="cal1.select(document.courseList.startTime,'Anch_startTime','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url context="${urlContext}" value="/images/calendar.png"/>"></a>
			</li>
			<li>
				<label class="required">Til:</label>
				<input id="stopTime" name="stopTime" value="<fmt:formatDate value="${stopTime}" pattern="${dateformat}"/>" type="text" size="12"/>
				<a href="#" name="a1" id="Anch_stopTime"
					onClick="cal1.select(document.courseList.stopTime,'Anch_stopTime','<fmt:message key="date.format"/>'); return false;"
					title="<fmt:message key="course.calendar.title"/>"><img src="<c:url context="${urlContext}" value="/images/calendar.png"/>"></a>
			</li>

<c:if test="${!isSVV}">
        <input type="hidden" id="past" name="past" 
        <c:if test="${past == true}"> value="1" </c:if>
        <c:if test="${past == false}"> value="0" </c:if> />
		<c:if test="${past == true}">
	    	<input type="hidden" id="historic" name="historic"
	    	<c:if test="${historic == true}"> value="1" </c:if>
	    	<c:if test="${historic == false}"> value="0" </c:if>
	    	/>
		</c:if>
		<c:if test="${past == false}">
            <li>
                <label class="required"><fmt:message key="course.includeHistoric"/>:</label>
                <input type="hidden" name="_historic" value="0"/>
		    	<input type="checkbox" id="historic" name="historic" value="1"
		    	<c:if test="${historic == true}"> checked </c:if> />
            </li>
		</c:if>
</c:if>

            <li>
                <button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
					<fmt:message key="button.search"/>
				</button>
            </li>
        </ul>
    </div>
</form:form>
<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url context="${urlContext}" value="/editCourse.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>
<%

SumBuild sums = new SumBuild();
SumBuild sumsTotal = new SumBuild();
SumBuild sumsExport = new SumBuild();
%>
<c:out value="${buttons}" escapeXml="false"/>
<display:table name="${courseList}" cellspacing="0" cellpadding="0" id="courseList" pagesize="${itemCount}" 
	class="list" export="${enableExport}" requestURI="listCourses.html" varTotals="totals">
    <c:set var="colIdx" value="0"/>
<%Course theCourse = (Course)pageContext.getAttribute("courseList");%>
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible && username == courseList.responsible.username || (isSVV && courseList.organization2id == currentUserForm.organization2id)}">
        <a href='<c:url context="${urlContext}" value="/editCourse.html"><c:param name="id" value="${courseList.id}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
</c:if>
        <% sums.addToNextSum("sum-pencil.png", null); %>
    </display:column>
        <% sumsTotal.addToNextSum("tot-pencil.png", null); %>
</c:if>

<c:choose>
<c:when test="${showCourseName}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="name">
		<c:set var="courseDescription" value="" />
		<c:if test="${showDescriptionToPublic || isAdmin || isEducationResponsible || isEventResponsible || isReader}">
			<c:set var="courseDescription"><c:out value="${courseList.description}"/></c:set>
		</c:if>
        <c:if test="${courseList.status == 3}"><img src="<c:url context="${urlContext}" value="/images/cancel.png"/>"
               		alt="<fmt:message key="icon.warning"/>" class="icon" /><fmt:message key="course.cancelled.alert"/><br/></c:if>
         <a href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseDescription}"/>"><c:out value="${courseList.name}"/></a>
        <% sums.addToNextSum(null, null); %>
    </display:column>
    <% sumsTotal.addToNextSum(null, null); %>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="course.name"/>
</c:when>
<c:otherwise>
   <c:if test="${containsUnfinished}">
   <display:column media="html" sortable="false" class="mediumButtonWidth" titleKey="button.signup">
		<c:if test="${courseList.status == 2 && (courseList.availableAttendants > 0 || useWaitlists)}">
		    <button type="button"
		        onclick="location.href='<c:url context="${urlContext}" value="/performRegistration.html"><c:param name="courseId" value="${courseList.id}" /></c:url>'">
		        <fmt:message key="button.signup"/>
		    </button>
		</c:if>
        <% sums.addToNextSum("sum-button.signup", null); %>
    </display:column>
    <% sumsTotal.addToNextSum("tot-button.signup", null); %>
	</c:if>
</c:otherwise>
</c:choose>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.status">
        <c:if test="${courseList.status == 0}"><img src="<c:url context="${urlContext}" value="/images/add.png"/>" alt="<fmt:message key="course.status.created"/>" title="<fmt:message key="course.status.created"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 1}"><img src="<c:url context="${urlContext}" value="/images/stop.png"/>" alt="<fmt:message key="course.status.finished"/>" title="<fmt:message key="course.status.finished"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 2}"><img src="<c:url context="${urlContext}" value="/images/accept.png"/>" alt="<fmt:message key="course.status.published"/>" title="<fmt:message key="course.status.published"/>" class="icon"/></c:if>
        <c:if test="${courseList.status == 3}"><img src="<c:url context="${urlContext}" value="/images/cancel.png"/>" alt="<fmt:message key="course.status.cancelled"/>" title="<fmt:message key="course.status.cancelled"/>" class="icon"/></c:if>
        <% sums.addToNextSum("sum-status", null); %>
    </display:column>
    <% sumsTotal.addToNextSum("tot-status", null); %>
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
    <% sums.addToNextSum(null, null); %>
    </display:column>
    <% sumsTotal.addToNextSum(null, null); %>
</c:when>
<c:otherwise>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
		<c:set var="courseDescription" value="" />
		<c:if test="${showDescriptionToPublic || isAdmin || isEducationResponsible || isEventResponsible || isReader}">
			<c:set var="courseDescription"><c:out value="${courseList.description}"/></c:set>
		</c:if>

         <a href="<c:url context="${urlContext}" value="/detailsCourse.html"><c:param name="id" value="${courseList.id}"/></c:url>" 
         title="<c:out value="${courseDescription}"/>"><fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/></a>
    <% sums.addToNextSum("sum-startTime", null); %>
    </display:column>
    <% sumsTotal.addToNextSum("tot-startTime", null); %>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
		<fmt:formatDate value="${courseList.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
        <% sumsExport.addToNextSum(null, null); %>
    </display:column>
</c:otherwise>    
</c:choose>


<c:if test="${!isSVV}">
    <display:column media="csv excel" sortable="true" headerClass="sortable" titleKey="course.stopTime" sortProperty="stopTime">
         <fmt:formatDate value="${courseList.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    <% sumsExport.addToNextSum(null, null); %>
    </display:column>
</c:if>
    
    
<c:if test="${containsUnfinished}">
    <display:column property="availableAttendants" sortable="true" headerClass="sortable" titleKey="course.availableAttendants" total="true">
    <%sums.addToNextSum("availableSum", theCourse, "getAvailableAttendants");%>
    <%sumsExport.addToNextSum("availableSum", theCourse, "getAvailableAttendants");%>
    </display:column>
    <%sumsTotal.addToNextSum("availableSum", theCourse, "getAvailableAttendants");%>
</c:if>
    
<c:if test="${showAttendantDetails && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">

	<c:if test="${containsFinished}">
    <display:column media="html csv excel pdf" property="numberOfRegistrations" sortable="true" headerClass="sortable" titleKey="statistics.numRegistrations">
    <%sums.addToNextSum("registrationsSum", theCourse, "getNumberOfRegistrations");%>
    <%sumsExport.addToNextSum("registrationsSum", theCourse, "getNumberOfRegistrations");%>
    </display:column>
    <%sumsTotal.addToNextSum("registrationsSum", theCourse, "getNumberOfRegistrations");%>
	</c:if>    

    <display:column media="html csv excel pdf" property="numberOfParticipants" sortable="true" headerClass="sortable" titleKey="statistics.numRegistered">
    <%sums.addToNextSum("participantsSum", theCourse, "getNumberOfParticipants");%>
    <%sumsExport.addToNextSum("participantsSum", theCourse, "getNumberOfParticipants");%>
    </display:column>
    <%sumsTotal.addToNextSum("participantsSum", theCourse, "getNumberOfParticipants");%>

	<c:if test="${containsFinished}">
    <display:column media="html csv excel pdf" property="attendants" sortable="true" headerClass="sortable" titleKey="statistics.numAttended">
    <%sums.addToNextSum("attendedSum", theCourse, "getAttendants");%>
    <%sumsExport.addToNextSum("attendedSum", theCourse, "getAttendants");%>
    </display:column>
    <%sumsTotal.addToNextSum("attendedSum", theCourse, "getAttendants");%>
	</c:if>

</c:if>

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
    <%sums.addToNextSum(null, null);%>
	</display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.registerBy.export">
        <fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}" />
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${showDuration}">
    <display:column property="duration" sortable="true" headerClass="sortable"
         titleKey="course.duration">
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <%sumsExport.addToNextSum(null, null);%>
</c:if>
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="course.organization">
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <%sumsExport.addToNextSum(null, null);%>

<c:if test="${useOrganization2 && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
    <display:column property="organization2.name" sortable="true" headerClass="sortable"
         titleKey="course.organization2">
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <%sumsExport.addToNextSum(null, null);%>
</c:if>

<c:if test="${useServiceArea}">
    <display:column property="serviceArea.name" sortable="true" headerClass="sortable"
         titleKey="course.serviceArea">
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <%sumsExport.addToNextSum(null, null);%>
</c:if>
         
    <display:column media="html" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location">
         <a href="<c:url context="${urlContext}" value="/detailsLocation.html"><c:param name="id" value="${courseList.location.id}"/></c:url>" title="<c:out value="${courseList.location.description}"/>"><c:out value="${courseList.location.name}"/></a>
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
    <display:column media="csv excel xml pdf" property="location.name" sortable="true" headerClass="sortable" titleKey="course.location">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
	<display:column media="html" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible">
         <a href="<c:url context="${urlContext}" value="/detailsUser.html"><c:param name="username" value="${courseList.responsible.username}"/></c:url>"><c:out value="${courseList.responsible.fullName}"/></a>
    <%sums.addToNextSum(null, null);%>
    </display:column>
    <%sumsTotal.addToNextSum(null, null);%>
</c:if>

<c:if test="${!isSVV || (isSVV && (isAdmin || isEducationResponsible || isEventResponsible || isReader))}">
    <display:column media="csv excel xml pdf" property="responsible.fullName" sortable="true" headerClass="sortable" titleKey="course.responsible">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!isSVV}">
    <display:column media="excel" property="instructor.name" sortable="true" headerClass="sortable" titleKey="course.instructor.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!showAttendantDetails && !isSVV}">
    <display:column media="csv excel pdf" property="maxAttendants" sortable="true" headerClass="sortable" titleKey="course.maxAttendants.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!singleprice && usePayment}">
    <display:column media="excel" property="reservedInternal" sortable="true" headerClass="sortable" titleKey="course.reservedInternal.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
    
    <display:column media="excel" property="feeInternal" sortable="true" headerClass="sortable" titleKey="course.feeInternal.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>


<c:if test="${usePayment}">
    <display:column media="excel" property="feeExternal" sortable="true" headerClass="sortable" titleKey="course.feeExternal.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!isSVV}">
    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.registerStart.export" sortProperty="registerStart">
         <fmt:formatDate value="${courseList.registerStart}" type="both" pattern="${dateformat} ${timeformat}"/>
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${useRegisterBy && !isSVV}">
    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.registerBy.export" sortProperty="registerBy">
         <fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!isSVV}">
    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.reminder.export" sortProperty="reminder">
         <fmt:formatDate value="${courseList.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>

<c:if test="${!isSVV}">
    <display:column media="excel" property="description" sortable="true" headerClass="sortable" titleKey="course.description.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>

    <display:column media="excel" property="detailURL" sortable="true" headerClass="sortable" titleKey="course.detailURL.export">
    <%sumsExport.addToNextSum(null, null);%>
    </display:column>
</c:if>
    
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

<c:if test="${!isSVV}">
    <display:setProperty name="export.ics" value="true"/>
</c:if>

<c:if test="${isSVV}">
    <display:setProperty name="export.xml" value="false"/>
</c:if>
<% 
sums.toFirstSum();
sumsTotal.toFirstSum();
sumsExport.toFirstSum();
%>
<c:if test="${isSVV && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
<display:footer media="html">
<%
int preSumColspan=0;
for(int i=0; "".equals(sums.get(i)) && i<sums.size(); i++)  {
preSumColspan++;
}
%>
<tr><td class="sum" colspan="<%=preSumColspan%>"><fmt:message key="courseList.pageSum"/></td> 
<%
for(int i=preSumColspan; i<sums.size(); i++) {
%>
<td class="sum"><%=sums.get(i)%></td>
<%
}
%>
</tr>

<tr><td class="sum" colspan="<%=preSumColspan%>"><fmt:message key="courseList.totalSum"/></td> 
<%
for(int i=preSumColspan; i<sumsTotal.size(); i++) {
%>
<td class="sum"><%=sumsTotal.get(i)%></td>
<%
}
%>
</tr>
</display:footer>

<display:footer media="excel csv pdf">
<tr><td class="sum"><fmt:message key="courseList.totalSum"/></td> 
<%
for(int i=0; i<sumsExport.size(); i++) {
%>
<td class="sum"><%=sumsExport.get(i)%></td>
<%
}
%>
</tr>
</display:footer>
</c:if>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
<c:set var="parameters"><fmt:message key="javaapp.baseurl"/><c:out value="${urlContextAppendix}"/><fmt:message key="javaapp.courselisturl"/>
<c:if test="${course.name != null && course.name ne ''}">name=<c:out value="${course.name}"/>%26</c:if>
<c:if test="${course.organizationid > 0}">organizationid=<c:out value="${course.organizationid}"/>%26</c:if>
<c:if test="${course.serviceAreaid > 0}">serviceArea=<c:out value="${course.serviceAreaid}"/>%26</c:if>
<c:if test="${course.categoryid > 0}">categoryid=<c:out value="${course.categoryid}"/>%26</c:if>
<c:if test="${past == true}">past=1</c:if>
<c:if test="${historic == true}">historic=1</c:if>
</c:set>

<c:if test="${!isSVV}">
<div class="searchUrl" style="padding:3px;">
    <fmt:message key="url-to-this-search"/>: <a class="external" href="<c:out value="${parameters}"/>" target="_blank"><c:out value="${parameters}"/></a>
</div>
</c:if>

</c:if>
<script type="text/javascript">
fillSelect($p("organizationidElement"));
</script>
