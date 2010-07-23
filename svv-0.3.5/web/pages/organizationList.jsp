<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="organizationList.title"/></title>
<content tag="heading"><fmt:message key="organizationList.heading"/></content>

<fmt:message key="organizationList.item" var="item"/>
<fmt:message key="organizationList.items" var="items"/>

<c:choose>
<c:when test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">

<c:set var="buttons">
<c:if test="${isAdmin}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url context="${urlContext}" value="/editOrganization.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${organizationList}" cellspacing="0" cellpadding="0"
    id="organizationList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="">
<c:if test="${isAdmin}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<a href='<c:url context="${urlContext}" value="/editOrganization.html"><c:param name="id" value="${organizationList.id}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
    </display:column>
</c:if>
    <display:column property="name" sortable="true" headerClass="sortable"
         titleKey="organization.name"/>
         
    <display:column sortable="true" headerClass="sortable"
         titleKey="organization.number">
         <fmt:formatNumber value="${organizationList.number}" minFractionDigits="0"/>
    </display:column>
    
    <display:column property="typeAsEnum.displayName" sortable="true" headerClass="sortable"
         titleKey="organization.type">
    </display:column>
    
<c:if test="${(isAdmin || isEducationResponsible || isEventResponsible || isReader) && usePayment}">
    <display:column property="invoiceName" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.name"/>
    <display:column property="invoiceAddress.address" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.address"/>
    <display:column property="invoiceAddress.city" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.city"/>
   <display:column property="invoiceAddress.postalCode" sortable="true" headerClass="sortable"
         titleKey="organizationList.invoiceAddress.postalCode"/>
</c:if>    
    
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column sortable="true" headerClass="sortable"
         titleKey="organization.selectable">
		<c:if test="${organizationList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${organizationList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>



    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

</c:when>
<c:otherwise>
    <div class="message" style="font-size: 12px"><fmt:message key="access.denied" /></div>
</c:otherwise>
</c:choose>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("organizationList");
</script>
--%>