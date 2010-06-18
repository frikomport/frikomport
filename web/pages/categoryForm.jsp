<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="categoryForm.title"/></title>
<content tag="heading"><fmt:message key="categoryForm.heading"/></content>

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
            <form:input path="name"/>
            <form:errors cssClass="fieldError" path="name"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="category.selectable"/>
        </th>
        <td>
            <form:checkbox path="selectable"/>
            <form:errors cssClass="fieldError" path="selectable"/>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
            <authz:authorize ifAnyGranted="admin">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
            <c:if test="${!empty category.id}">
                <input type="submit" class="button" name="delete"
                    onclick="bCancel=true;return confirmDelete('<fmt:message key="categoryList.theitem"/>')" 
                    value="<fmt:message key="button.delete"/>" />
            </c:if>
            </authz:authorize>
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

