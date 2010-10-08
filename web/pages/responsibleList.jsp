<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="responsibleList.title"/></title>
<content tag="heading"><fmt:message key="responsibleList.heading"/></content>

<fmt:message key="responsibleList.item" var="item"/>
<fmt:message key="responsibleList.items" var="items"/>

<display:table name="${responsibleList}" cellspacing="0" cellpadding="0"
    id="responsibleList" pagesize="${itemCount}" class="list" 
    export="${!isSVV}" requestURI="">

	<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<c:if test="${isAdmin || isEducationResponsible || (isEventResponsible && currentUserForm.organization2id == responsibleList.organization2id)}">
        <a href='<c:url context="${urlContext}" value="/editUser.html"><c:param name="username" value="${responsibleList.username}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
		</c:if>
    </display:column>
	</c:if>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="user.fullname" sortProperty="fullName">
         <a href="<c:url context="${urlContext}" value="/detailsUser.html"><c:param name="username" value="${responsibleList.username}"/></c:url>" 
         title="responsibleListDescription"><c:out value="${responsibleList.fullName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="fullName" sortable="true" headerClass="sortable" titleKey="user.fullname"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="user.email">
         <a href="mailto:<c:out value="${responsibleList.email}"/>"><c:out value="${responsibleList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="user.email"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column property="phoneNumber" sortable="true" headerClass="sortable"
         titleKey="user.phoneNumber"/>

    <display:column property="mobilePhone" sortable="true" headerClass="sortable"
         titleKey="user.mobilePhone"/>
         
    <display:column property="organization.name" sortable="true" headerClass="sortable"
         titleKey="user.organization"/>

    <display:column property="organization2.name" sortable="true" headerClass="sortable"
         titleKey="user.organization2"/>
    
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

