<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="locationDetail.title"/></title>
<content tag="heading"><fmt:message key="locationDetail.heading"/></content>

<spring:bind path="location.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post" action="<c:url value="/editLocation.html"/>" id="locationForm"
    onsubmit="return validateLocation(this)">
<table class="detail">

    <spring:bind path="location.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
    </spring:bind>

    <tr>
        <th>
            <fmt:message key="location.name"/>
        </th>
        <td>
			<c:choose>
				<c:when test="${not empty location.detailURL}">
		          	<a class="external" href="<c:out value="${location.detailURL}"/>" target="_blank" title="<c:out value="${location.description}"/>"><c:out value="${location.name}"/></a>
				</c:when>
				<c:otherwise>
	          		<c:out value="${location.name}"/>
				</c:otherwise>
			</c:choose>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.maxAttendants"/>
        </th>
        <td>
        	<fmt:formatNumber value="${location.maxAttendants}" minFractionDigits="0"/>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <tr>
        <th>
            <fmt:message key="location.owner"/>
        </th>
        <td>
            <spring:bind path="location.owner">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.feePerDay"/>
        </th>
        <td>
        	<fmt:formatNumber value="${location.feePerDay}" minFractionDigits="2"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="location.description"/>
        </th>
        <td>
            <spring:bind path="location.description">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.address"/>
        </th>
        <td>
        	<c:choose>
		    	<c:when test="${not empty location.mapURL}">
	          		<a class="external" href="<c:out value="${location.mapURL}"/>" target="_blank"><c:out value="${location.address}"/></a>
		    	</c:when>
		    	<c:otherwise>
	          		<c:out value="${location.address}"/>
		    	</c:otherwise>
		    </c:choose>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <tr>
        <th>
            <fmt:message key="location.mailAddress"/>
        </th>
        <td>
            <spring:bind path="location.mailAddress">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.contactName"/>
        </th>
        <td>
            <spring:bind path="location.contactName">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.phone"/>
        </th>
        <td>
            <spring:bind path="location.phone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.email"/>
        </th>
        <td>
          	<a href="<c:out value="mailto:${location.email}"/>"><c:out value="${location.email}"/></a>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.organization"/>
        </th>
        <td>
            <spring:bind path="location.organization.name">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="location.selectable"/>
        </th>
        <td>
            <spring:bind path="location.selectable">
				<c:if test="${status.value == true}"><fmt:message key="checkbox.checked"/></c:if>
				<c:if test="${status.value == false}"><fmt:message key="checkbox.unchecked"/></c:if>
            </spring:bind>
        </td>
    </tr>
</c:if>

    <tr>
        <td class="buttonBar">            
            <input type="submit" class="button" name="return" onclick="bCancel=true"
                value="<fmt:message key="button.location.list"/>" />
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
		    <button type="button" onclick="location.href='<c:url value="/editLocation.html"><c:param name="id" value="${location.id}"/></c:url>'">
	    	    <fmt:message key="button.edit"/>
		    </button>
</c:if>
        </td>
    </tr>
</table>
</form>

<v:javascript formName="location" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>