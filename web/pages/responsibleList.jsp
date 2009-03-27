<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="responsibleList.title"/></title>
<content tag="heading"><fmt:message key="responsibleList.heading"/></content>

<fmt:message key="responsibleList.item" var="item"/>
<fmt:message key="responsibleList.items" var="items"/>

<display:table name="${responsibleList}" cellspacing="0" cellpadding="0"
    id="responsibleList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="">

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="user.fullname" sortProperty="fullName">
         <a href="<c:url value="/detailsUser.html"><c:param name="username" value="${responsibleList.username}"/></c:url>" 
         title="responsibleListDescription"><c:out value="${responsibleList.fullName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="fullName" sortable="true" headerClass="sortable" titleKey="user.fullname"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="user.email">
         <a href="mailto:<c:out value="${responsibleList.email}"/>"><c:out value="${responsibleList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="user.email"/>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <display:column property="phoneNumber" sortable="true" headerClass="sortable"
         titleKey="user.phoneNumber"/>

    <display:column property="mobilePhone" sortable="true" headerClass="sortable"
         titleKey="user.mobilePhone"/>
         
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="user.organization"/>
    
</c:if>

<c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
	    <button type="button" onclick="location.href='<c:url value="/editUser.html"><c:param name="username" value="${responsibleList.username}"/></c:url>'">
    	    <fmt:message key="button.edit"/>
	    </button>
    </display:column>
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

