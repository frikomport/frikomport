<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="personEdit.title"/></title>
<content tag="heading"><fmt:message key="personEdit.heading"/></content>

<spring:bind path="person.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url context="${urlContext}" value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<!-- form:form commandName="person" onsubmit="return validatePerson(this)" -->
<form:form commandName="person">

<table class="detail">
    <form:hidden path="id"/>
    <tr>
        <th>
            <soak:label key="person.name"/>
        </th>
        <td>
            <form:input path="name" size="50"/>
            <form:errors path="name" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.email"/>
        </th>
        <td>
            <form:input path="email" size="50"/>
            <form:errors path="email" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.phone"/>
        </th>
        <td>
            <form:input path="phone"/>
            <form:errors path="phone" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.mobilePhone"/>
        </th>
        <td>
            <form:input path="mobilePhone"/>
            <form:errors path="mobilePhone" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.detailURL"/>
        </th>
        <td>
            <form:input path="detailURL"/>
            <form:errors path="detailURL" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.mailAddress"/>
        </th>
        <td>
            <form:input path="mailAddress"/>
            <form:errors path="mailAddress" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.description"/>
        </th>
        <td>
            <form:textarea path="description" cols="50" rows="3"/>
            <form:errors path="description" cssClass="fieldError"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.selectable"/>
        </th>
        <td>
            <form:checkbox path="selectable"/>
            <form:errors path="selectable"/>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
			<c:if test="${!empty person.id}">
	            <input type="submit" class="button" name="delete"
    	            onclick="bCancel=true;return confirmDelete('<fmt:message key="personList.theitem"/>')" 
        	        value="<fmt:message key="button.delete"/>" />
        	</c:if>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form:form>

<v:javascript formName="person" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url context="${urlContext}" value="/scripts/validator.jsp"/>"></script>

