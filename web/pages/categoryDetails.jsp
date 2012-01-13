<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="categoryDetails.title"/></title>
<content tag="heading"><fmt:message key="categoryDetails.heading"/></content>

<spring:bind path="category.*">
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

<form:form commandName="category" onsubmit="return validateCategory(this)">
<table class="detail">

    <form:hidden path="id"/>

    <tr>
        <th>
            <soak:label key="category.name"/>
        </th>
        <td>
            <c:out value="${category.name}"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="category.selectable"/>
        </th>
        <td>
            <spring:bind path="category.selectable">
                <c:if test="${status.value == true}"><fmt:message key="checkbox.checked"/></c:if>
                <c:if test="${status.value == false}"><fmt:message key="checkbox.unchecked"/></c:if>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin || isEducationResponsible}">
            <button type="button" onclick="location.href='<c:url value="/editCategory.html"><c:param name="id" value="${category.id}"/></c:url>'">
                <fmt:message key="button.edit"/>
            </button>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form:form>

<v:javascript formName="category" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>

