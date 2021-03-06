<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

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

<fmt:message key="signup.message"/>

<div class="separator"></div>

<form method="post" action="<c:url value="/signup.html"/>" name="signupForm" onsubmit="return validateUser(this)">
<table class="detail">
    <tr>
        <th>
            <soak:label key="user.username"/>
        </th>
        <td>
            <spring:bind path="user.username">
                <input type="text" name="username" value="<c:out value="${status.value}"/>" id="username"/>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.password"/>
        </th>
        <td>
            <spring:bind path="user.password">
            <input type="password" id="password" name="password" size="40" 
                value="<c:out value="${status.value}"/>"/>
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
    <tr>
        <th>
            <soak:label key="user.firstName"/>
        </th>
        <td>
            <spring:bind path="user.firstName">
            <input type="text" name="firstName" value="<c:out value="${status.value}"/>" id="firstName"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.lastName"/>
        </th>
        <td>
            <spring:bind path="user.lastName">
            <input type="text" name="lastName" value="<c:out value="${status.value}"/>" id="lastName"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.address"/>
        </th>
        <td>
            <spring:bind path="user.address.address">
            <input type="text" name="address.address" value="<c:out value="${status.value}"/>" id="address.address" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.city"/>
        </th>
        <td>
            <spring:bind path="user.address.city">
            <input type="text" name="address.city" value="<c:out value="${status.value}"/>" id="address.city" size="40"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.province"/>
        </th>
        <td>
            <spring:bind path="user.address.province">
            <input type="text" name="address.province" value="<c:out value="${status.value}"/>" id="address.province" size="40"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.country"/>
        </th>
        <td>
            <spring:bind path="user.address.country">
            <soak:country name="address.country" prompt="" default="${user.address.country}"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.address.postalCode"/>
        </th>
        <td>
            <spring:bind path="user.address.postalCode">
            <input type="text" name="address.postalCode" value="<c:out value="${status.value}"/>" id="address.postalCode" size="10"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.email"/>
        </th>
        <td>
            <spring:bind path="user.email">
            <input type="text" name="email" value="<c:out value="${status.value}"/>" id="email" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.phoneNumber"/>
        </th>
        <td>
            <spring:bind path="user.phoneNumber">
            <input type="text" name="phoneNumber" value="<c:out value="${status.value}"/>" id="phoneNumber"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.website"/>
        </th>
        <td>
            <spring:bind path="user.website">
            <input type="text" name="website" value="<c:out value="${status.value}"/>" id="website" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
            <c:if test="${!empty user.website}">
            <a href="<c:out value="${user.website}"/>"><fmt:message key="user.visitWebsite"/></a>
            </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="user.passwordHint"/>
        </th>
        <td>
            <spring:bind path="user.passwordHint">
            <input type="text" name="passwordHint" value="<c:out value="${status.value}"/>" id="passwordHint" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>" />

            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</table>
</form>

<script type="text/javascript">
highlightFormElements();
document.forms["signupForm"].username.focus();
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>


