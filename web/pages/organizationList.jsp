<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="organizationList.title"/></title>
<content tag="heading"><fmt:message key="organizationList.heading"/></content>

<fmt:message key="organizationList.item" var="item"/>
<fmt:message key="organizationList.items" var="items"/>

<c:set var="buttons">
<authz:authorize ifAnyGranted="admin">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editOrganization.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</authz:authorize>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${organizationList}" cellspacing="0" cellpadding="0"
    id="organizationList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="">
<authz:authorize ifAnyGranted="admin">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<a href='<c:url value="/editOrganization.html"><c:param name="id" value="${organizationList.id}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
    </display:column>
</authz:authorize>
    <display:column property="name" sortable="true" headerClass="sortable"
         titleKey="organization.name"/>
         
    <display:column sortable="true" headerClass="sortable"
         titleKey="organization.number">
         <fmt:formatNumber value="${organizationList.number}" minFractionDigits="0"/>
    </display:column>
    
<authz:authorize ifAnyGranted="admin">
    <display:column property="invoiceName" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.name"/>
    <display:column property="invoiceAddress.address" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.address"/>
    <display:column property="invoiceAddress.city" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.city"/>
   <display:column property="invoiceAddress.postalCode" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.postalCode"/>
</authz:authorize>   
    
<authz:authorize ifAnyGranted="admin,instructor,editor">
    <display:column sortable="true" headerClass="sortable"
         titleKey="organization.selectable">
		<c:if test="${organizationList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${organizationList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</authz:authorize>



    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("organizationList");
</script>
--%>