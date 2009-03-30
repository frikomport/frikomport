<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="locationList.title"/></title>
<content tag="heading"><fmt:message key="locationList.heading"/></content>

<fmt:message key="locationList.item" var="item"/>
<fmt:message key="locationList.items" var="items"/>

<form method="post" action="<c:url value="/listLocations.html"/>" id="locationList">
   	<INPUT type="hidden" id="ispostbacklocationlist" name="ispostbacklocationlist" value="1"/> 

	<table>
	    <th>
	        <soak:label key="location.organization"/>
	    </th>
	    <td>
	        <spring:bind path="location.organizationid">
				  <select name="<c:out value="${status.expression}"/>">
				    <c:forEach var="organization" items="${organizations}">
				      <option value="<c:out value="${organization.id}"/>"
					      <c:if test="${organization.id == location.organizationid}"> selected="selected"</c:if>>
				        <c:out value="${organization.name}"/>
				      </option>
				    </c:forEach>
				  </select>            
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	        </spring:bind>
	    </td>
        <td class="buttonBar">            
		<button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
			<fmt:message key="button.search"/>
		</button>
	</table>
</form>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editLocation.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${locationList}" cellspacing="0" cellpadding="0"
    id="locationList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="location.name" sortProperty="name">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${locationList.id}"/></c:url>" 
         title="<c:out value="${locationList.description}"/>"><c:out value="${locationList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="location.name"/>
    
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="location.address">
    	<c:choose>
	    	<c:when test="${not empty locationList.mapURL}">
          		<a href="<c:out value="${locationList.mapURL}"/>"><c:out value="${locationList.address}"/></a>
	    	</c:when>
	    	<c:otherwise>
          		<c:out value="${locationList.address}"/>
	    	</c:otherwise>
	    </c:choose>
    </display:column>
    <display:column media="csv excel xml pdf" property="address" sortable="true" headerClass="sortable" titleKey="location.address"/>
    
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="location.contactName" sortProperty="contactName">
         <a href="mailto:<c:out value="${locationList.email}"/>"><c:out value="${locationList.contactName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="contactName" sortable="true" headerClass="sortable" titleKey="location.contactName"/>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="location.email"/>
    
    <display:column property="phone" sortable="true" headerClass="sortable"
         titleKey="location.phone"/>
         
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="location.organization"/>

    <display:column sortable="true" headerClass="sortable"
         titleKey="location.selectable">
		<c:if test="${locationList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${locationList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>

<c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editLocation.html"><c:param name="id" value="${locationList.id}"/><c:param name="from" value="list"/></c:url>'">
    	    <fmt:message key="button.edit"/>
	    </button>
    </display:column>
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("locationList");
</script>
--%>