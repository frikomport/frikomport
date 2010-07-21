<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="personList.title"/></title>
<content tag="heading"><fmt:message key="personList.heading"/></content>

<fmt:message key="personList.item" var="item"/>
<fmt:message key="personList.items" var="items"/>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url context="${urlContext}" value="/editPerson.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="${personList}" cellspacing="0" cellpadding="0"
    id="personList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <a href='<c:url context="${urlContext}" value="/editPerson.html"><c:param name="id" value="${personList.id}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
    </display:column>
</c:if>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="person.name" sortProperty="name">
         <a href="<c:url context="${urlContext}" value="/detailsPerson.html"><c:param name="id" value="${personList.id}"/></c:url>" 
         title="<c:out value="${personList.description}"/>"><c:out value="${personList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="person.name"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="person.email">
         <a href="mailto:<c:out value="${personList.email}"/>"><c:out value="${personList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="person.email"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column property="phone" sortable="true" headerClass="sortable"
         titleKey="person.phone"/>

    <display:column property="mobilePhone" sortable="true" headerClass="sortable"
         titleKey="person.mobilePhone"/>

    <display:column property="mailAddress" sortable="true" headerClass="sortable"
         titleKey="person.mailAddress"/>

    <display:column sortable="true" headerClass="sortable"
         titleKey="person.selectable">
        <c:if test="${personList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${personList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
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