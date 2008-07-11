<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="serviceAreaDetail.title"/></title>
<content tag="heading"><fmt:message key="serviceAreaDetail.heading"/></content>

<spring:bind path="serviceArea.*">
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

<form:form commandName="serviceArea" onsubmit="return validateServiceArea(this)">
<table class="detail">

    <form:hidden path="id"/>

    <tr>
        <th>
            <soak:label key="serviceArea.name"/>
        </th>
        <td>
            <form:input path="name"/>
            <form:errors cssClass="fieldError" path="name"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="serviceArea.selectable"/>
        </th>
        <td>
            <form:checkbox path="selectable"/>
            <form:errors cssClass="fieldError" path="selectable"/>
        </td>
    </tr>
    <tr>
			<th>
				<soak:label key="serviceArea.organization" />
			</th>
			<td>
				<form:select path="organizationid">
					<form:options items="${organizations}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="organizationid" />
			</td>
		</tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin}">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
			<c:if test="${!empty serviceArea.id}">
	            <input type="submit" class="button" name="delete"
	                onclick="bCancel=true;return confirmDelete('<fmt:message key="serviceAreaList.theitem"/>')" 
	                value="<fmt:message key="button.delete"/>" />
			</c:if>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form:form>

<v:javascript formName="serviceArea" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>

