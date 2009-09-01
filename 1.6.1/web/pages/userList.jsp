<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userList.title" />
</title>
<content tag="heading">
<fmt:message key="userList.heading" />
</content>

<%-- 
<c:set var="buttons">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html"/>?method=Add&from=list'">
        <fmt:message key="button.add"/>
    </button>
    
    <button type="button" onclick="location.href='<c:url value="/mainMenu.html" />'">
        <fmt:message key="button.cancel"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />
--%>

<form:form commandName="user" name="user">
<div class="searchForm">
    <ul>
        <li>
            <label class="required"><fmt:message key="user.username"/>:</label>
            <form:input path="username" size="15"/>
        </li>
        <li>
            <label class="required"><fmt:message key="user.firstName"/>:</label>
            <form:input path="firstName" size="15"/>
        </li>
        <li>
            <label class="required"><fmt:message key="user.lastName"/>:</label>
            <form:input path="lastName" size="15"/>
        </li>
        <li>
            <label class="required"><fmt:message key="user.email"/>:</label>
            <form:input path="email" size="15"/>
        </li>
        <li>
            <button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
                <fmt:message key="button.search"/>
            </button>
        </li>
    </ul>
</div>
</form:form>

<display:table name="${userList}" cellspacing="0" cellpadding="0"
    requestURI="listUsers.html" defaultsort="1" id="userList" pagesize="${itemCount}"
    class="list userList" export="true">

    <%-- Table columns --%>
    <display:column property="username" sortable="true"
        headerClass="sortable" url="/detailsUser.html?from=list"
        paramId="username" paramProperty="username" titleKey="user.username" />
    <display:column property="firstName" sortable="true"
        headerClass="sortable" titleKey="user.firstName" />
    <display:column property="lastName" sortable="true"
        headerClass="sortable" titleKey="user.lastName" />
    <display:column property="email" sortable="true" headerClass="sortable"
        autolink="true" titleKey="user.mail" />
    <c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <button type="button" onclick="location.href='<c:url value="/editUser.html"><c:param name="username" value="${userList.username}"/><c:param name="from" value="list"/></c:url>'">
            <fmt:message key="button.edit"/>
        </button>
    </display:column>
</c:if>

    <fmt:message var="user" key="userList.user" />
    <fmt:message var="users" key="userList.users" />

    <display:setProperty name="paging.banner.item_name" value="${user}" />
    <display:setProperty name="paging.banner.items_name" value="${users}" />

    <display:setProperty name="export.excel.filename" value="User List.xls" />
    <display:setProperty name="export.csv.filename" value="User List.csv" />
    <display:setProperty name="export.pdf.filename" value="User List.pdf" />
</display:table>

<%-- 
<c:out value="${buttons}" escapeXml="false" />
%-->

<%--
<script type="text/javascript">
highlightTableRows("users");
</script>
--%>
