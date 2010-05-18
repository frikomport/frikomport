<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="serviceAreaList.title" />
</title>
<content tag="heading">
<fmt:message key="serviceAreaList.heading" />
</content>

<fmt:message key="serviceAreaList.item" var="item" />
<fmt:message key="serviceAreaList.items" var="items" />

<form method="post" action="<c:url value="/listServiceAreas.html"/>" id="serviceAreaList">
   	<input type="hidden" id="ispostbackservicearealist" name="ispostbackservicearealist" value="1"/>

	<table>
	    <th>
	        <soak:label key="serviceArea.organization"/>
	    </th>
	    <td>
	        <spring:bind path="serviceArea.organizationid">
				  <select name="<c:out value="${status.expression}"/>">
				    <c:forEach var="organization" items="${organizations}">
				      <option value="<c:out value="${organization.id}"/>"
					      <c:if test="${organization.id == serviceArea.organizationid}"> selected="selected"</c:if>>
				        <c:out value="${organization.name}"/>
				      </option>
				    </c:forEach>
				  </select>
	            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
	        </spring:bind>
	    </td>
        <td class="buttonBar">
		<button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
			<fmt:message key="button.search"/>
		</button>
	</table>
</form>

<c:set var="buttons">
	<c:if test="${isAdmin}">
		<button type="button" style="margin-right: 5px"
			onclick="location.href='<c:url value="/editServiceArea.html"/>'">
			<fmt:message key="button.add" />
		</button>
	</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<display:table name="${serviceAreaList}" cellspacing="0" cellpadding="0"
	id="serviceAreaList" pagesize="${itemCount}" class="list" export="true"
	requestURI="">
    <c:if test="${isAdmin}">
        <display:column media="html" sortable="false" headerClass="sortable"
            titleKey="button.heading">
        <a href='<c:url value="/editServiceArea.html"><c:param name="id" value="${serviceAreaList.id}"/></c:url>'>
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
        </display:column>
    </c:if>
	<display:column property="name" sortable="true" headerClass="sortable"
		titleKey="serviceArea.name" />

	<display:column property="organization.name" sortable="true"
		headerClass="sortable" titleKey="serviceArea.organization" />


	<c:if
		test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
		<display:column sortable="true" headerClass="sortable"
			titleKey="serviceArea.selectable">
			<c:if test="${serviceAreaList.selectable == true}">
				<fmt:message key="checkbox.checked" />
			</c:if>
			<c:if test="${serviceAreaList.selectable == false}">
				<fmt:message key="checkbox.unchecked" />
			</c:if>
		</display:column>
	</c:if>

	<display:setProperty name="paging.banner.item_name" value="${item}" />
	<display:setProperty name="paging.banner.items_name" value="${items}" />
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<%--
<script type="text/javascript">
highlightTableRows("serviceAreaList");
</script>
--%>
