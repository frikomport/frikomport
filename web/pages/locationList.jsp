<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="locationList.title"/></title>
<content tag="heading"><fmt:message key="locationList.heading"/></content>

<fmt:message key="locationList.item" var="item"/>
<fmt:message key="locationList.items" var="items"/>

<form method="post" action="<c:url context="/${urlContext}" value="/listLocations.html"/>" id="locationList">
    <INPUT type="hidden" id="ispostbacklocationlist" name="ispostbacklocationlist" value="1"/> 
    <div class="searchForm">
	<ul>
<c:if test="${useOrganization2 && (isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
		<li>
            <spring:bind path="location.organization2id">
                  <select name="<c:out value="${status.expression}"/>">
                    <c:forEach var="organization" items="${organizations2}">
                      <option value="<c:out value="${organization.id}"/>"
                          <c:if test="${organization.id == location.organization2id}"> selected="selected"</c:if>>
                        <c:out value="${organization.name}"/>
                      </option>
                    </c:forEach>
                  </select>            
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
		</li>
</c:if>
		<li>
            <spring:bind path="location.organizationid">
                  <select name="<c:out value="${status.expression}"/>">
                    <c:forEach var="organization" items="${organizations}">
                      <option value="<c:out value="${organization.id}"/>"
                          <c:if test="${organization.id == location.organizationid}"> selected="selected"</c:if>>
                        <c:out value="${organization.name}"/>
                      </option>
                    </c:forEach>
                  </select>            
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
		</li>
		<li>        
	        <button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
	            <fmt:message key="button.search"/>
	        </button>
        </li>
	</ul>
	</div>
</form>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url context="${urlContext}" value="/editLocation.html"/>'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<c:choose>
<c:when test="${isSVV}">
<c:set var="doExport">false</c:set>
</c:when>
<c:otherwise>
<c:set var="doExport">true</c:set>
</c:otherwise>
</c:choose>

    <display:table name="${locationList}" cellspacing="0" cellpadding="0" id="locationList" pagesize="${itemCount}" class="list" export="${doExport}" requestURI="">
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
        <a href='<c:url context="${urlContext}" value="/editLocation.html"><c:param name="id" value="${locationList.id}"/><c:param name="from" value="list"/></c:url>'>
            <img src="<c:url context="${urlContext}" value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
    </display:column>
</c:if>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="location.name" sortProperty="name">
         <a href="<c:url context="${urlContext}" value="/detailsLocation.html"><c:param name="id" value="${locationList.id}"/></c:url>" 
         title="<c:out value="${locationList.description}"/>"><c:out value="${locationList.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="name" sortable="true" headerClass="sortable" titleKey="location.name.export"/>
    
    <display:column media="html" headerClass="sortable" titleKey="location.address">
        <c:choose>
            <c:when test="${not empty locationList.mapURL}">
                <a href="<c:out value="${locationList.mapURL}"/>" target="_blank"><c:out value="${locationList.address}"/></a>
            </c:when>
            <c:otherwise>
                <c:out value="${locationList.address}"/>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column media="csv excel xml pdf" property="address" sortable="true" headerClass="sortable" titleKey="location.address"/>

<c:if test="${isSVV}">
    <display:column property="postalCode" sortable="true" headerClass="sortable" titleKey="location.postalCode"/>
</c:if>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column property="organization.name" sortable="true" headerClass="sortable" titleKey="location.organization"/>


<c:if test="${useOrganization2}">
    <display:column property="organization2.name" sortable="true" headerClass="sortable" titleKey="location.organization2"/>
</c:if>

    <display:column media="html" sortable="true" headerClass="sortable" titleKey="location.contactName" sortProperty="contactName">
         <a href="mailto:<c:out value="${locationList.email}"/>"><c:out value="${locationList.contactName}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="contactName" sortable="true" headerClass="sortable" titleKey="location.contactName.export"/>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="location.email"/>
    
    <display:column property="phone" sortable="true" headerClass="sortable" titleKey="location.phone"/>
         
    <display:column sortable="true" headerClass="sortable" titleKey="location.selectable">
        <c:if test="${locationList.selectable == true}"><fmt:message key="checkbox.checked"/></c:if>
        <c:if test="${locationList.selectable == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
</c:if>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>
