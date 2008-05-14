<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="personDetail.title"/></title>
<content tag="heading"><fmt:message key="personDetail.heading"/></content>

<spring:bind path="person.*">
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

<form method="post" action="<c:url value="/editPerson.html"/>" id="personForm"
    onsubmit="return validatePerson(this)">
<table class="detail">

    <spring:bind path="person.id">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
    </spring:bind>

    <tr>
        <th>
            <fmt:message key="person.name"/>
        </th>
        <td>
			<c:choose>
				<c:when test="${not empty person.detailURL}">
		          	<a href="<c:out value="${person.detailURL}"/>" title="<c:out value="${person.description}"/>"><c:out value="${person.name}"/></a>
        		  	<button type="button" onclick="location.href='<c:out value="${person.detailURL}"/>'">
	    	    		<fmt:message key="button.more"/>
				    </button>
				</c:when>
				<c:otherwise>
	          		<c:out value="${person.name}"/>
				</c:otherwise>
			</c:choose>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.email"/>
        </th>
        <td>
          	<a href="<c:out value="mailto:${person.email}"/>"><c:out value="${person.email}"/></a>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="person.phone"/>
        </th>
        <td>
            <spring:bind path="person.phone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.mobilePhone"/>
        </th>
        <td>
            <spring:bind path="person.mobilePhone">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="person.mailAddress"/>
        </th>
        <td>
            <spring:bind path="person.mailAddress">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <fmt:message key="person.description"/>
        </th>
        <td>
            <spring:bind path="person.description">
            	<c:out value="${status.value}"/>
            </spring:bind>
        </td>
    </tr>

<c:if test="${isAdmin || isEducationResponsible || isCourseResponsible}">
    <tr>
        <th>
            <fmt:message key="person.selectable"/>
        </th>
        <td>
            <spring:bind path="person.selectable">
				<c:if test="${status.value == true}"><fmt:message key="checkbox.checked"/></c:if>
				<c:if test="${status.value == false}"><fmt:message key="checkbox.unchecked"/></c:if>
            </spring:bind>
        </td>
    </tr>
</c:if>

    <tr>
        <td class="buttonBar">            
            <input type="submit" class="button" name="return" onclick="bCancel=true"
                value="<fmt:message key="button.return"/>" />
<c:if test="${isAdmin || isEducationResponsible}">
		    <button type="button" onclick="location.href='<c:url value="/editPerson.html"><c:param name="id" value="${person.id}"/></c:url>'">
	    	    <fmt:message key="button.edit"/>
		    </button>
</c:if>
        </td>
    </tr>
</table>
</form>

<v:javascript formName="person" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>