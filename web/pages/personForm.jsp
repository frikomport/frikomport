<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="personEdit.title"/></title>
<content tag="heading"><fmt:message key="personEdit.heading"/></content>

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
            <soak:label key="person.name"/>
        </th>
        <td>
            <spring:bind path="person.name">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.email"/>
        </th>
        <td>
            <spring:bind path="person.email">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.phone"/>
        </th>
        <td>
            <spring:bind path="person.phone">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.mobilePhone"/>
        </th>
        <td>
            <spring:bind path="person.mobilePhone">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.detailURL"/>
        </th>
        <td>
            <spring:bind path="person.detailURL">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.mailAddress"/>
        </th>
        <td>
            <spring:bind path="person.mailAddress">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.description"/>
        </th>
        <td>
            <spring:bind path="person.description">
                <textarea cols="50" rows="3" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="person.selectable"/>
        </th>
        <td>
            <spring:bind path="person.selectable">
				<input type="hidden" name="_<c:out value="${status.expression}"/>" value="visible"/>
				<input type="checkbox" name="<c:out value="${status.expression}"/>" value="true" <c:if test="${status.value}">checked</c:if>>
				<span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin || isEducationResponsible}">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
			<c:if test="${!empty person.id}">
	            <input type="submit" class="button" name="delete"
    	            onclick="bCancel=true;return confirmDelete('<fmt:message key="personList.theitem"/>')" 
        	        value="<fmt:message key="button.delete"/>" />
        	</c:if>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form>

<v:javascript formName="person" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>

