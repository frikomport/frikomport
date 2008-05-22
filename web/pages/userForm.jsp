<%@ include file="/common/taglibs.jsp"%>

<spring:bind path="user.*">
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

<form:form commandName="user" onsubmit="return onFormSubmit(this)">

<form:hidden path="version"/>
    
<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />

<c:if test="${cookieLogin == 'true'}">
    <form:hidden path="password"/>
    <form:hidden path="confirmPassword"/>
</c:if>

<c:if test="${empty user.username}">
    <input type="hidden" name="encryptPass" value="true" />
</c:if>

<table class="detail">
<c:set var="pageButtons">
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <%-- <c:if test="${param.from == 'list'}">
            <input type="submit" class="button" name="delete"
                onclick="bCancel=true;return confirmDelete('user')" 
                value="<fmt:message key="button.delete"/>" />
        </c:if> --%>
    
       	    <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</c:set>
    <tr>
        <th>
            <soak:label key="user.username"/>
        </th>
        <td>
        <c:choose>
            <c:when test="${empty user.username}">
                <form:input path="username"/>
                <form:errors cssClass="fieldError" path="username"/>
            </c:when>
            <c:otherwise>
                <c:out value="${user.username}"/>
                <form:hidden path="username"/>
            </c:otherwise>
        </c:choose>
        </td>
    </tr>
    <%--
    <c:if test="${cookieLogin != 'true'}">
    <tr>
        <th>
           <soak:label key="user.password"/>
        </th>
        <td>
            <spring:bind path="user.password">
            <input type="password" id="password" name="password" size="40" 
                value="<c:out value="${status.value}"/>" onchange="passwordChanged(this)"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.confirmPassword"/>
        </th>
        <td>
            <spring:bind path="user.confirmPassword">
            <input type="password" name="confirmPassword" id="confirmPassword"
                value="<c:out value="${status.value}"/>" size="40"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    </c:if>
    --%>
    <tr>
        <th>
            <soak:label key="user.firstName"/>
        </th>
        <td>
            <form:input path="firstName"/>
            <form:errors cssClass="fieldError" path="firstName"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.lastName"/>
        </th>
        <td>
            <form:input path="lastName"/>
            <form:errors cssClass="fieldError" path="lastName"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.address"/>
        </th>
        <td>
            <form:input path="address.address"/>
            <form:errors cssClass="fieldError" path="address.address"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.city"/>
        </th>
        <td>
            <form:input path="address.city"/>
            <form:errors cssClass="fieldError" path="address.city"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.province"/>
        </th>
        <td>
            <form:input path="address.province"/>
            <form:errors cssClass="fieldError" path="address.province"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.country"/>
        </th>
        <td>
            <form:input path="address.country"/>
            <form:errors cssClass="fieldError" path="address.country"/>>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.postalCode"/>
        </th>
        <td>
            <form:input path="address.postalCode"/>
            <form:errors cssClass="fieldError" path="address.postalCode"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.email"/>
        </th>
        <td>
            <form:input path="email"/>
            <form:errors cssClass="fieldError" path="email"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.phoneNumber"/>
        </th>
        <td>
            <form:input path="phoneNumber"/>
            <form:errors cssClass="fieldError" path="phoneNumber"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.website"/>
        </th>
        <td>
            <form:input path="website"/>
            <form:errors cssClass="fieldError" path="website"/>
            <c:if test="${!empty user.website}">
            <a href="<c:out value="${user.website}"/>"><fmt:message key="user.visitWebsite"/></a>
            </c:if>
        </td>
    </tr>
    <%--<tr>
        <th>
            <soak:label key="user.passwordHint"/>
        </th>
        <td>
            <spring:bind path="user.passwordHint">
            <input type="text" name="passwordHint" value="<c:out value="${status.value}"/>" id="passwordHint" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr> --%>
    <tr>
        <th>
            <soak:label key="user.organization"/>
        </th>
        <td>
            <form:select path="organizationid">
                <form:options items="${organizations}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors cssClass="fieldError" htmlEscape="false" path="organizationid"/>
        </td>
    </tr>    
<c:if test="${param.from == 'list' or param.method == 'Add'}">
    <tr>
        <th>
            <label for="enabled"><fmt:message key="user.enabled"/>?</label>
        </th>
        <td>
            <spring:bind path="user.enabled">
                <input type="hidden" name="_<c:out value="${status.expression}"/>"  value="visible" /> 
                <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true" 
                    <c:if test="${status.value}">checked="checked"</c:if> /> 
            </spring:bind>
        </td>
    </tr>
</c:if>
<%--
   <tr>
        <td></td>
        <td>
            <fieldset class="pickList">
                <legend>
                    <fmt:message key="userProfile.assignRoles"/>
                </legend>
	            <table class="pickList">
	                <tr>
	                    <th class="pickLabel">
	                        <soak:label key="user.availableRoles" 
	                            colon="false" styleClass="required"/>
	                    </th>
	                    <td>
	                    </td>
	                    <th class="pickLabel">
	                        <soak:label key="user.roles"
	                            colon="false" styleClass="required"/>
	                    </th>
	                </tr>
	                <c:set var="leftList" value="${availableRoles}" scope="request"/>
	                <c:set var="rightList" value="${user.roleList}" scope="request"/>
	                <c:import url="/WEB-INF/pages/pickList.jsp">
	                    <c:param name="listCount" value="1"/>
	                    <c:param name="leftId" value="availableRoles"/>
	                    <c:param name="rightId" value="userRoles"/>
	                </c:import>
	            </table>
            </fieldset>
        </td>
    </tr>
    </c:when>
    <c:when test="${not empty user.username}">
    <tr>
        <th>
            <soak:label key="user.roles"/>
        </th>
        <td>
        <c:forEach var="role" items="${user.roleList}" varStatus="status">
            <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userRoles" 
                value="<c:out value="${role.label}"/>" />
        </c:forEach>
            <spring:bind path="user.enabled">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" /> 
            </spring:bind>
        </td>
    </tr>
    </c:when>
</c:choose> 
--%>

    
    <%-- Print out buttons - defined at top of form --%>
    <%-- This is so you can put them at the top and the bottom if you like --%>
	<c:out value="${pageButtons}" escapeXml="false" />

</table>
</form:form>

<script type="text/javascript">
<!--
highlightFormElements();
<%-- if we're doing an add, change the focus --%>
<c:choose><c:when test="${user.username == null}"><c:set var="focus" value="username"/></c:when>
<c:when test="${cookieLogin == 'true'}"><c:set var="focus" value="firstName"/></c:when>
<c:otherwise><c:set var="focus" value="password"/></c:otherwise></c:choose>

var focusControl = document.forms["userForm"].elements["<c:out value="${focus}"/>"];

if (focusControl.type != "hidden" && !focusControl.disabled) {
    focusControl.focus();
}

function passwordChanged(passwordField) {
    var origPassword = "<c:out value="${user.password}"/>";
    if (passwordField.value != origPassword) {
        createFormElement("input", "hidden", 
                          "encryptPass", "encryptPass", 
                          "true", passwordField.form);
    }
}

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
<c:if test="${param.from == 'list'}">
    selectAll('userRoles');
</c:if>
    return validateUser(theForm);
}
// -->
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>


