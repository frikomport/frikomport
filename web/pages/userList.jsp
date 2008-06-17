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

<display:table name="${userList}" cellspacing="0" cellpadding="0"
	requestURI="" defaultsort="1" id="users" pagesize="25"
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
