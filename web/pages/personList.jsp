<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="personList.title"/></title>
<content tag="heading"><fmt:message key="personList.heading"/></content>

<fmt:message key="personList.item" var="item"/>
<fmt:message key="personList.items" var="items"/>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editPerson.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${personList}" cellspacing="0" cellpadding="0"
    id="personList" pagesize="25" styleClass="list" 
    export="true" requestURI="">

    <display:column media="html" sort="true" headerClass="sortable" titleKey="person.name" sortProperty="name">
         <a href="<c:url value="/detailsPerson.html"><c:param name="id" value="${personList.id}"/></c:url>" 
         title="<c:out value="${personList.description}"/>"><c:out value="${personList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sort="true" headerClass="sortable" titleKey="person.name"/>

    <display:column media="html" sort="true" headerClass="sortable" titleKey="person.email">
         <a href="mailto:<c:out value="${personList.email}"/>"><c:out value="${personList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sort="true" headerClass="sortable" titleKey="person.email"/>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column property="phone" sort="true" headerClass="sortable"
         titleKey="person.phone"/>

    <display:column property="mobilePhone" sort="true" headerClass="sortable"
         titleKey="person.mobilePhone"/>

    <display:column property="mailAddress" sort="true" headerClass="sortable"
         titleKey="person.mailAddress"/>

    <display:column sort="true" headerClass="sortable"
         titleKey="person.selectable">
		<c:if test="${personList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${personList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
	</display:column>
</c:if>

<c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sort="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editPerson.html"><c:param name="id" value="${personList.id}"/></c:url>'">
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
highlightTableRows("personList");
</script>
--%>