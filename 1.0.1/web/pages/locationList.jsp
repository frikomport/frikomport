<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="locationList.title"/></title>
<content tag="heading"><fmt:message key="locationList.heading"/></content>

<fmt:message key="locationList.item" var="item"/>
<fmt:message key="locationList.items" var="items"/>

<form method="post" action="<c:url value="/listLocations.html"/>" id="locationList">
   	<INPUT type="hidden" id="ispostbacklocationlist" name="ispostbacklocationlist" value="1"/> 

	<table>
	    <th>
	        <soak:label key="location.municipality"/>
	    </th>
	    <td>
	        <spring:bind path="location.municipalityid">
				  <select name="<c:out value="${status.expression}"/>">
				    <c:forEach var="municipality" items="${municipalities}">
				      <option value="<c:out value="${municipality.id}"/>"
					      <c:if test="${municipality.id == location.municipalityid}"> selected="selected"</c:if>>
				        <c:out value="${municipality.name}"/>
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
<c:if test="${isAdmin || isEducationResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editLocation.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${locationList}" cellspacing="0" cellpadding="0"
    id="locationList" pagesize="25" styleClass="list" 
    export="true" requestURI="">

    <display:column media="html" sort="true" headerClass="sortable" titleKey="location.name">
         <a href="<c:url value="/detailsLocation.html"><c:param name="id" value="${locationList.id}"/></c:url>" 
         title="<c:out value="${locationList.description}"/>"><c:out value="${locationList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sort="true" headerClass="sortable" titleKey="location.name"/>
    
    <display:column media="html" sort="true" headerClass="sortable" titleKey="location.address">
    	<c:choose>
	    	<c:when test="${not empty locationList.mapURL}">
          		<a href="<c:out value="${locationList.mapURL}"/>"><c:out value="${locationList.address}"/></a>
	    	</c:when>
	    	<c:otherwise>
          		<c:out value="${locationList.address}"/>
	    	</c:otherwise>
	    </c:choose>
    </display:column>
    <display:column media="csv excel xml pdf" property="address" sort="true" headerClass="sortable" titleKey="location.address"/>
    
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column media="html" sort="true" headerClass="sortable" titleKey="location.contactName">
         <a href="mailto:<c:out value="${locationList.email}"/>"><c:out value="${locationList.contactName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="contactName" sort="true" headerClass="sortable" titleKey="location.contactName"/>
    <display:column media="csv excel xml pdf" property="email" sort="true" headerClass="sortable" titleKey="location.email"/>
    
    <display:column property="phone" sort="true" headerClass="sortable"
         titleKey="location.phone"/>
         
    <display:column property="municipality.name" sort="true" headerClass="sortable"
         titleKey="location.municipality"/>

    <display:column sort="true" headerClass="sortable"
         titleKey="location.selectable">
		<c:if test="${locationList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${locationList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>

<c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sort="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editLocation.html"><c:param name="id" value="${locationList.id}"/></c:url>'">
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