<%@ include file="/common/taglibs.jsp"%>

<c:set var="admin" value="${false}"/>
<c:if test="${isAdmin}">
    <c:set var="admin" value="${true}"/>
</c:if>

<fmt:message key="configurationList.item" var="item"/>
<fmt:message key="configurationList.items" var="items"/>

<title><fmt:message key="configurationAdministration.title"/></title>
<content tag="heading">
<fmt:message key="configurationAdministration.heading"/>
</content>


<c:if test="${updated}">
    <div class="message" style="font-size: 12px"><fmt:message key="configuration.updated" /></div>
</c:if>
<c:if test="${cancelled}">
    <div class="message" style="font-size: 12px"><fmt:message key="configuration.cancelled" /></div>
</c:if>

<form method="post" action="<c:url value="/administerConfiguration.html"/>" name="configurationForm" id="configurationForm">

<display:table name="${configurationsBackingObject.configurations}" cellspacing="0" cellpadding="0"
    pagesize="${itemCount}" class="list" export="true" id="configurationList" requestURI="">

    <display:column property="key" sortable="true" headerClass="sortable" 
        titleKey="configuration.key"/>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="configuration.active">
        <input type="hidden" name="_key<c:out value="${configurationList.key}"/>" value="visible"/>
        <input type="checkbox" name="key_<c:out value="${configurationList.key}"/>"
                    <c:if test="${configurationList.active == true}"> checked="checked" </c:if> />
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="configuration.active">
        <c:if test="${configurationList.active == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${configurationList.active == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>

    <!--  display:column property="value" sortable="true" headerClass="sortable" 
        titleKey="configuration.value"/ -->

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
<input type="submit" class="button" name="docancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
</form>