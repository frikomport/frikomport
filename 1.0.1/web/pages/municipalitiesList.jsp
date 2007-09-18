<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="municipalitiesList.title"/></title>
<content tag="heading"><fmt:message key="municipalitiesList.heading"/></content>

<fmt:message key="municipalitiesList.item" var="item"/>
<fmt:message key="municipalitiesList.items" var="items"/>

<c:set var="buttons">
<c:if test="${isAdmin}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editMunicipalities.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${municipalitiesList}" cellspacing="0" cellpadding="0"
    id="municipalitiesList" pagesize="25" styleClass="list" 
    export="true" requestURI="">

    <display:column property="name" sort="true" headerClass="sortable"
         titleKey="municipalities.name"/>
         
    <display:column sort="true" headerClass="sortable"
         titleKey="municipalities.number">
         <fmt:formatNumber value="${municipalitiesList.number}" minFractionDigits="0"/>
    </display:column>
    
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column sort="true" headerClass="sortable"
         titleKey="municipalities.selectable">
		<c:if test="${municipalitiesList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${municipalitiesList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>

<c:if test="${isAdmin}">
    <display:column media="html" sort="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editMunicipalities.html"><c:param name="id" value="${municipalitiesList.id}"/></c:url>'">
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
highlightTableRows("municipalitiesList");
</script>
--%>