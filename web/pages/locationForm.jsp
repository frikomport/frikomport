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

<form method="post" action="<c:url value="/editLocation.html"/>" id="locationForm"
    onsubmit="return validateLocation(this)">
<table class="detail">

	<spring:bind path="location.id">
	<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
	</spring:bind>

    <tr>
        <th>
            <soak:label key="location.name"/>
        </th>
        <td>
            <spring:bind path="location.name">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.maxAttendants"/>
        </th>
        <td>
            <spring:bind path="location.maxAttendants">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.owner"/>
        </th>
        <td>
            <spring:bind path="location.owner">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.feePerDay"/>
        </th>
        <td>
            <spring:bind path="location.feePerDay">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.description"/>
        </th>
        <td>
            <spring:bind path="location.description">
                <textarea cols="50" rows="3" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.address"/>
        </th>
        <td>
            <spring:bind path="location.address">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.mailAddress"/>
        </th>
        <td>
            <spring:bind path="location.mailAddress">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.contactName"/>
        </th>
        <td>
            <spring:bind path="location.contactName">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.phone"/>
        </th>
        <td>
            <spring:bind path="location.phone">
                <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.email"/>
        </th>
        <td>
            <spring:bind path="location.email">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.mapURL"/>
        </th>
        <td>
            <spring:bind path="location.mapURL">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.detailURL"/>
        </th>
        <td>
            <spring:bind path="location.detailURL">
                <input type="text" size="50" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" 
                    value="<c:out value="${status.value}"/>" />
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.organization"/>
        </th>
        <td>
            <spring:bind path="location.organizationid">
				  <select name="<c:out value="${status.expression}"/>">
				    <c:forEach var="organization" items="${organizations}">
				      <option value="<c:out value="${organization.id}"/>"
					      <c:if test="${organization.id == status.value}"> selected="selected"</c:if>>
				        <c:out value="${organization.name}"/>
				      </option>
				    </c:forEach>
				  </select>            
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <th>
            <soak:label key="location.selectable"/>
        </th>
        <td>
            <spring:bind path="location.selectable">
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
</form>

<v:javascript formName="location" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>

