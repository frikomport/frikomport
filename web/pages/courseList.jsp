<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="courseList.title"/></title>
<content tag="heading"><fmt:message key="courseList.heading"/></content>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="courseList.item" var="item"/>
<fmt:message key="courseList.items" var="items"/>

<script type="text/javascript">
// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
 var orgid=obj.options[obj.selectedIndex].value;
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
}
</script>

<c:if test="${alreadyRegistered}">
	<div class="message" style="font-size: 12px">
		<fmt:message key="courseList.alreadyRegistered" />
	</div>
</c:if>

<form:form commandName="course" name="courseList" action="listCourses.html">
    <div class="searchForm">
        <ul>
            <li>
                <soak:label key="course.organization" styleClass="required"/>
                <form:select  path="organizationid" onchange="fillSelect(this);">
                    <form:options items="${organizations}" itemValue="id" itemLabel="name" />
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="organizationid" />
            </li>

<c:if test="${!user.hashuser}">
            <li>
                <soak:label key="course.organization2" styleClass="required"/>
                <form:select  path="organization2id">
                    <form:options items="${organizations2}" itemValue="id" itemLabel="name" />
                </form:select>
                <form:errors cssClass="fieldError" htmlEscape="false" path="organization2id" />
            </li>
</c:if>

<c:if test="${useServiceArea}">
            <li>
                <soak:label key="course.serviceArea" styleClass="required"/>
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
                <soak:label key="courseSearch.name" styleClass="required"/>
                <form:input path="name"/>
                <form:errors cssClass="fieldError" htmlEscape="false" path="name" />
            </li>
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
        onclick="location.href='<c:url value="/editCourse.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>
<display:table name="${courseList}" cellspacing="0" cellpadding="0"
    id="courseList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="listCourses.html">

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible && username == courseList.responsible.username || (isSVV && courseList.organization2id == user.organization2id)}">
        <a href='<c:url value="/editCourse.html"><c:param name="id" value="${courseList.id}"/><c:param name="from" value="list"/></c:url>'>
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


    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.stopTime" sortProperty="stopTime">
         <fmt:formatDate value="${courseList.stopTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
    
    <display:column property="availableAttendants" sortable="true" headerClass="sortable" titleKey="course.availableAttendants"/>

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

<c:if test="${showDuration}">
    <display:column property="duration" sortable="true" headerClass="sortable"
         titleKey="course.duration"/>
</c:if>
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="course.organization"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
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

    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.registerBy.export" sortProperty="registerBy">
         <fmt:formatDate value="${courseList.registerBy}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column media="excel" sortable="true" headerClass="sortable" titleKey="course.reminder.export" sortProperty="reminder">
         <fmt:formatDate value="${courseList.reminder}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>

    <display:column media="excel" property="description" sortable="true" headerClass="sortable" titleKey="course.description.export"/>

    <display:column media="excel" property="detailURL" sortable="true" headerClass="sortable" titleKey="course.detailURL.export"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
    <display:setProperty name="export.ics" value="true"/>
    <display:setProperty name="export.rss" value="true"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
<c:set var="parameters">
	<fmt:message key="javaapp.baseurl"/><fmt:message key="javaapp.courselisturl"/>
	<c:if test="${course.name != null && course.name ne ''}">name=<c:out value="${course.name}"/>%26</c:if>
	<c:if test="${course.organizationid > 0}">organizationid=<c:out value="${course.organizationid}"/>%26</c:if>
	<c:if test="${course.serviceAreaid > 0}">serviceArea=<c:out value="${course.serviceAreaid}"/>%26</c:if>
	<c:if test="${course.categoryid > 0}">categoryid=<c:out value="${course.categoryid}"/>%26</c:if>
	<c:if test="${past == true}">past=1</c:if>
	<c:if test="${historic == true}">historic=1</c:if>
</c:set>
<div class="searchUrl" style="padding:3px;">
    <fmt:message key="url-to-this-search"/>: <a class="external" href="<c:out value="${parameters}"/>" target="_blank"><c:out value="${parameters}"/></a>
</div>
</c:if>

<%--
<script type="text/javascript">
highlightTableRows("courseList");
</script>
--%>
