<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="locationEdit.title"/></title>
<content tag="heading"><fmt:message key="locationEdit.heading"/></content>

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

<!-- form:form commandName="location" onsubmit="return validateLocation(this)"-->
<form:form commandName="location">
<table class="detail">
    <form:hidden path="id"/>
    <tr>
        <th>
            <soak:label key="location.name"/>
        </th>
        <td>
            <form:input path="name" size="50"/>
            <form:errors cssClass="fieldError" path="name"/>
        </td>
    </tr>
    <tr>
        <th>
            <soak:label key="location.detailURL"/>
        </th>
        <td>
            <form:input path="detailURL" size="50"/>
            <form:errors cssClass="fieldError" path="detailURL"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.description"/>
        </th>
        <td>
            <form:textarea path="description" cols="50" rows="3"/>
            <form:errors cssClass="fieldError" path="description"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.maxAttendants"/>
        </th>
        <td>
            <form:input path="maxAttendants"/>
            <form:errors cssClass="fieldError" path="maxAttendants"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.address"/>
        </th>
        <td>
            <form:input path="address" size="50"/>
            <form:errors cssClass="fieldError" path="address"/>
        </td>
    </tr>


<c:if test="${usePostalCode}">
    <tr>
        <th>
            <soak:label key="location.postalCode"/>
        </th>
        <td>
            <form:input path="postalCode" size="8"/>
            <form:errors cssClass="fieldError" path="postalCode"/>
        </td>
    </tr>
</c:if>

    <tr>
        <th>
            <soak:label key="location.mapURL"/>
        </th>
        <td>
            <form:input path="mapURL" size="50"/>
            <form:errors cssClass="fieldError" path="mapURL"/>
        </td>
    </tr>

	<c:if test="${!isSVV}">
    <tr>
        <th>
            <soak:label key="location.mailAddress"/>
        </th>
        <td>
            <form:input path="mailAddress" size="50"/>
            <form:errors cssClass="fieldError" path="mailAddress"/>
        </td>
    </tr>
	</c:if>
	
    <tr>
        <th>
            <soak:label key="location.contactName"/>
        </th>
        <td>
            <form:input path="contactName" size="50"/>
            <form:errors cssClass="fieldError" path="contactName"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.phone"/>
        </th>
        <td>
            <form:input path="phone"/>
            <form:errors cssClass="fieldError" path="phone"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.email"/>
        </th>
        <td>
            <form:input path="email" size="50"/>
            <form:errors cssClass="fieldError" path="email"/>
        </td>
    </tr>

	<c:if test="${!isSVV}">
    <tr>
        <th>
            <soak:label key="location.owner"/>
        </th>
        <td>
            <form:input path="owner" size="50"/>
            <form:errors cssClass="fieldError" path="owner"/>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.feePerDay"/>
        </th>
        <td>
            <form:input path="feePerDay"/>
            <form:errors cssClass="fieldError" path="feePerDay"/>
        </td>
    </tr>
	</c:if>
    
    <tr>
        <th>
            <soak:label key="location.organizationid"/>
        </th>
        <td>
            <form:select path="organizationid" items="${organizations}" itemLabel="name" itemValue="id"/>
            <form:errors cssClass="fieldError" path="organizationid"/>
        </td>
    </tr>


<c:choose>
	<c:when test="${useOrganization2}">
		<tr>
			<th>
				<soak:label key="location.organization2id" />
			</th>
			<td>
				<form:select path="organization2id">
					<form:options items="${organizations2}" itemValue="id" itemLabel="name" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false" path="organization2id" />
			</td>
		</tr>
	</c:when>
	<c:otherwise>
		<form:hidden path="organization2id" />
	</c:otherwise>
</c:choose>

    <tr>
        <th>
            <soak:label key="location.selectable"/>
        </th>
        <td>
            <form:checkbox path="selectable"/>
            <form:errors cssClass="fieldError" path="selectable"/>
        </td>
    </tr>

    <tr>
        <td></td>
        <td class="buttonBar">            
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
			<c:if test="${!empty location.id}">
	            <input type="submit" class="button" name="delete"
	                onclick="bCancel=true;return confirmDelete('<fmt:message key="locationList.theitem"/>')" 
	                value="<fmt:message key="button.delete"/>" />
            </c:if>
</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />        
        </td>
    </tr>
</table>
</form:form>

<v:javascript formName="location" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>

