<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="organizationDetail.title"/></title>
<content tag="heading"><fmt:message key="organizationDetail.heading"/></content>

<spring:bind path="organization.*">
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

<form method="post" action="<c:url value="/editOrganization.html"/>" id="organizationForm"
    onsubmit="return validateOrganization(this)">
<table class="detail">

<spring:bind path="organization.id">
<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
</spring:bind>

    <tr>
        <th>
            <soak:label key="organization.name"/>
        </th>
        <td>
            <spring:bind path="organization.name">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="organization.number"/>
        </th>
        <td>
            <spring:bind path="organization.number">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="organization.selectable"/>
        </th>
        <td>
            <spring:bind path="organization.selectable">
				<input type="hidden" name="_<c:out value="${status.expression}"/>" value="visible"/>
				<input type="checkbox" name="<c:out value="${status.expression}"/>" value="true" <c:if test="${status.value}">checked</c:if>>
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin}">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
			<c:if test="${!empty organization.id}">
	            <input type="submit" class="button" name="delete"
	                onclick="bCancel=true;return confirmDelete('<fmt:message key="organizationList.theitem"/>')" 
	                value="<fmt:message key="button.delete"/>" />
			</c:if>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form>

<v:javascript formName="organization" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>


<html:javascript formName="organization" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/script/validators.jsp"/>"></script>
