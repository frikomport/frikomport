<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="activeUsers.title"/></title>
<content tag="heading"><fmt:message key="activeUsers.heading"/></content>
<body id="activeUsers"/>

<fmt:message key="activeUsers.message"/>

<div class="separator"></div>

<button type="button" onclick="location.href='mainMenu.html'">
    <fmt:message key="button.cancel"/>
</button>
    
<display:table name="${userNames}" id="user" cellspacing="0" cellpadding="0"
    defaultsort="1" class="list activeUserList" pagesize="${itemCount}" requestURI="">
  
    <%-- Table columns --%>
    <display:column escapeXml="true" property="username" width="30%" headerClass="sortable"
        titleKey="user.username" sortable="true"/>
    <display:column titleKey="activeUsers.fullName" headerClass="sortable"
        sortable="true">
        <c:out value="${user.firstName} ${user.lastName}"/>
        <c:if test="${not empty user.email}">
        <a href="mailto:<c:out value="${user.email}"/>">
            <img src="<c:url value="/images/iconEmail.gif"/>" 
                alt="<fmt:message key="icon.email"/>" class="icon"/></a>
        </c:if>
    </display:column>
        
    <display:setProperty name="paging.banner.item_name" value="user" />
    <display:setProperty name="paging.banner.items_name" value="users" />
</display:table>
