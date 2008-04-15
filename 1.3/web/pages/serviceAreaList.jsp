<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="serviceAreaList.title"/></title>
<content tag="heading"><fmt:message key="serviceAreaList.heading"/></content>

<fmt:message key="serviceAreaList.item" var="item"/>
<fmt:message key="serviceAreaList.items" var="items"/>

<c:set var="buttons">
<c:if test="${isAdmin}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editServiceArea.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${serviceAreaList}" cellspacing="0" cellpadding="0"
    id="serviceAreaList" pagesize="25" class="list" 
    export="true" requestURI="">

    <display:column property="name" sortable="true" headerClass="sortable"
         titleKey="serviceArea.name"/>
         
<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column sortable="true" headerClass="sortable"
         titleKey="serviceArea.selectable">
		<c:if test="${serviceAreaList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${serviceAreaList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>

<c:if test="${isAdmin}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editServiceArea.html"><c:param name="id" value="${serviceAreaList.id}"/></c:url>'">
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
highlightTableRows("serviceAreaList");
</script>
--%>