<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="organizationDetail.title" />
</title>
<content tag="heading">
<fmt:message key="organizationDetail.heading" />
</content>

<spring:bind path="organization.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url context="${urlContext}" value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<form:form commandName="organization" onsubmit="return validateOrganization(this)">
	<table class="detail">

		<form:hidden path="id" />

		<tr>
			<th>
				<soak:label key="organization.name" />
			</th>
			<td>
				<form:input path="name" size="50" />
				<form:errors cssClass="fieldError" path="name" />
			</td>
		</tr>

<c:if test="${!isSVV}">
		<tr>
			<th>
				<soak:label key="organization.number" />
			</th>
			<td>
				<form:input path="number" />
				<form:errors cssClass="fieldError" path="number" />
			</td>
		</tr>
</c:if>

		<tr>
			<th>
				<soak:label key="organization.type" />
			</th>
			<td>
			<c:if test="${!isSVV}">
				<form:select path="type" items="${types}" itemLabel="displayName" itemValue="typeDBValue" />
				<form:errors cssClass="fieldError" path="type" />
			</c:if>
			<c:if test="${isSVV}">
				<c:out value="${organization.typeAsEnum.displayName}" />
			</c:if>
			</td>
			
		</tr>

		<tr>
			<th>
				<soak:label key="organization.selectable" />
			</th>
			<td>
				<form:checkbox path="selectable" />
				<form:errors cssClass="fieldError" path="selectable" />
			</td>
		</tr>

<c:if test="${!isSVV}">
		<tr>
			<th>
				<soak:label key="organization.invoiceAddress" />
			</th>
		</tr>

		<tr>
			<th>
				<soak:label key="organization.invoiceAddress.name" />
			</th>
			<td>
				<form:input path="invoiceName" />
				<form:errors cssClass="fieldError" path="invoiceName" />
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="organization.invoiceAddress.address" />
			</th>
			<td>
				<form:input path="invoiceAddress.address" />
				<form:errors cssClass="fieldError" path="invoiceAddress.address" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="organization.invoiceAddress.city" />
			</th>
			<td>
				<form:input path="invoiceAddress.city" />
				<form:errors cssClass="fieldError" path="invoiceAddress.city" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="organization.invoiceAddress.postalCode" />
			</th>
			<td>
				<form:input path="invoiceAddress.postalCode" />
				<form:errors cssClass="fieldError" path="invoiceAddress.postalCode" />
			</td>
		</tr>
</c:if>
			
		<tr>
			<td></td>
			<td class="buttonBar">
				<c:if test="${isAdmin}">
					<input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
					<c:if test="${!empty organization.id && !isSVV}">
						<input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('<fmt:message key="organizationList.theitem"/>')"
							value="<fmt:message key="button.delete"/>" />
					</c:if>
				</c:if>
				<input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
			</td>
		</tr>
	</table>
</form:form>