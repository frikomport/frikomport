<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationList.title"/></title>
<content tag="heading"><fmt:message key="registrationList.heading"/></content>

<form method="post" action="<c:url value="/listRegistrations.html"/>" id="registrationList">
   	<INPUT type="hidden" id="ispostbackregistrationlist" name="ispostbackregistrationlist" value="1"/> 

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="registrationList.item" var="item"/>
<fmt:message key="registrationList.items" var="items"/>

	<table>
		<tr>
		    <th>
		        <soak:label key="registration.municipality"/>
		    </th>
		    <td>
		        <spring:bind path="registration.municipalityid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="municipality" items="${municipalities}">
					      <option value="<c:out value="${municipality.id}"/>"
						      <c:if test="${municipality.id == registration.municipalityid}"> selected="selected"</c:if>>
					        <c:out value="${municipality.name}"/>
					      </option>
					    </c:forEach>
					  </select>            
		            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
		        </spring:bind>
		    </td>

		    <th>
		        <soak:label key="registration.serviceArea"/>
		    </th>
		    <td>
		        <spring:bind path="registration.serviceareaid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="servicearea" items="${serviceareas}">
					      <option value="<c:out value="${servicearea.id}"/>"
						      <c:if test="${servicearea.id == registration.serviceareaid}"> selected="selected"</c:if>>
					        <c:out value="${servicearea.name}"/>
					      </option>
					    </c:forEach>
					  </select>
		            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
		        </spring:bind>
		    </td>	

		    <th>
		        <soak:label key="registration.course"/>
		    </th>
		    <td>
		        <spring:bind path="registration.courseid">
					  <select name="<c:out value="${status.expression}"/>">
					    <c:forEach var="theCourse" items="${courses}">
					      <option value="<c:out value="${theCourse.id}"/>"
						      <c:if test="${theCourse.id == registration.courseid}"> selected="selected"</c:if>>
					        <c:out value="${theCourse.name}"/>
					      </option>
					    </c:forEach>
					  </select>
		            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
		        </spring:bind>
		    </td>	

		</tr>
		<tr>
		    <th>
		        <soak:label key="registration.reserved"/>
		    </th>
		    <td>
				  <select id="reservedField" name="reservedField">
				      <option value="2" <c:if test="${reservedValue == 2}"> selected </c:if> /> <c:out value='${reserved["null"]}'/></option>
				      <option value="1" <c:if test="${reservedValue == 1}"> selected </c:if> /> <c:out value='${reserved["true"]}'/></option>
				      <option value="0" <c:if test="${reservedValue == 0}"> selected </c:if> /> <c:out value='${reserved["false"]}'/></option>
				  </select>
		    </td>
	
		    <th>
		        <soak:label key="registration.invoiced"/>
		    </th>
		    <td>
				  <select id="invoicedField" name="invoicedField">
				      <option value="2" <c:if test="${invoicedValue == 2}"> selected </c:if> /> <c:out value='${invoiced["null"]}'/></option>
				      <option value="1" <c:if test="${invoicedValue == 1}"> selected </c:if> /> <c:out value='${invoiced["true"]}'/></option>
				      <option value="0" <c:if test="${invoicedValue == 0}"> selected </c:if> /> <c:out value='${invoiced["false"]}'/></option>
				  </select>
		    </td>
		</tr>
		<tr>
		    <th>
				<soak:label key="course.includeHistoric"/>
		    </th>
		    <td>
		    	<INPUT type="hidden" name="_historic" value="0"/>
		    	<INPUT type="checkbox" id="historic" name="historic" value="1" 
		    	<c:if test="${historic == true}"> checked </c:if> />
		    </td>
	        <td class="buttonBar">            
			<button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
				<fmt:message key="button.search"/>
			</button>
		</tr>
	</table>
</form>

<c:set var="buttons">
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
<display:table name="${registrationList}" cellspacing="0" cellpadding="0"
    id="registrationList" pagesize="25" class="list" 
    export="true" requestURI="">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="course.name">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${registrationList.course.id}"/></c:url>" 
         title="<c:out value="${registrationList.course.description}"/>"><c:out value="${registrationList.course.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="course.name" sortable="true" headerClass="sortable" titleKey="course.name"/>
    
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime">
         <fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
    
    <display:column property="firstName" sortable="true" headerClass="sortable"
         titleKey="registration.firstName"/>
         
    <display:column property="lastName" sortable="true" headerClass="sortable"
         titleKey="registration.lastName"/>
         
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.email">
         <a href="mailto:<c:out value="${registrationList.email}"/>"><c:out value="${registrationList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="registration.email"/>
    
    <display:column property="phone" sortable="true" headerClass="sortable"
         titleKey="registration.phone"/>
         
    <display:column property="mobilePhone" sortable="true" headerClass="sortable"
         titleKey="registration.mobilePhone"/>
         
    <display:column property="municipality.name" sortable="true" headerClass="sortable"
         titleKey="registration.municipality"/>
         
    <display:column sortable="true" headerClass="sortable"
         titleKey="registration.invoiced">
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible || (isCourseResponsible && eZUserid == registrationList.course.responsibleid)}">
	    <button type="button" onclick="location.href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseid" value="${registrationList.courseid}"/></c:url>'">
    	    <fmt:message key="button.edit"/>
	    </button>
</c:if>
    </display:column>
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>
</c:if>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("registrationList");
</script>
--%>