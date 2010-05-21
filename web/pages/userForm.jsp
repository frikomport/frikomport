<%@ include file="/common/taglibs.jsp"%>


<spring:bind path="user.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<form:form commandName="user" onsubmit="return onFormSubmit(this)"
	name="user">

	<form:hidden path="version" />

	<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />

	<c:if test="${cookieLogin == 'true'}">
		<form:hidden path="password" />
		<form:hidden path="confirmPassword" />
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
					<input type="button" class="button" name="cancel"
						onclick="javascript:history.go(-1)"
						value="<fmt:message key="button.cancel"/>" />

				</td>
			</tr>
		</c:set>
		<tr>
			<th>
				<soak:label key="user.username" />
			</th>
			<td>
				<c:choose>
					<c:when test="${empty user.username}">
						<form:input path="username" />
						<form:errors cssClass="fieldError" path="username" />
					</c:when>
					<c:otherwise>
						<c:out value="${user.username}" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>

<c:if test="${showEmployeeFields}">
		<tr>
			<th>
				<soak:label key="user.employeeNumber" />
			</th>
			<td>
				<form:input path="employeeNumber" />
				<form:errors cssClass="fieldError" path="employeeNumber" />
			</td>
		</tr>
</c:if>

		<tr>
			<th>
				<soak:label key="user.firstName" />
			</th>
			<td>
				<c:choose>
					<c:when test="${user.hashuser}">
						<form:input path="firstName" />
						<form:errors cssClass="fieldError" path="firstName" />
					</c:when>
					<c:otherwise>
						<c:out value="${user.firstName}" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.lastName" />
			</th>
			<td>
				<c:choose>
					<c:when test="${user.hashuser}">
						<form:input path="lastName" />
						<form:errors cssClass="fieldError" path="lastName" />
					</c:when>
					<c:otherwise>
						<c:out value="${user.lastName}" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
<c:if test="${showAddress}">
		<tr>
			<th>
				<soak:label key="user.address.address" />
			</th>
			<td>
				<form:input path="address.address" />
				<form:errors cssClass="fieldError" path="address.address" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.address.postalCode" />
			</th>
			<td>
				<form:input path="address.postalCode" />
				<form:errors cssClass="fieldError" path="address.postalCode" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.address.city" />
			</th>
			<td>
				<form:input path="address.city" />
				<form:errors cssClass="fieldError" path="address.city" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.address.country" />
			</th>
			<td>
				<form:input path="address.country" />
				<form:errors cssClass="fieldError" path="address.country" />
			</td>
		</tr>
</c:if>
		
		<tr>
			<th>
				<soak:label key="user.email" />
			</th>
			<td>
                <c:choose>
					<c:when test="${user.hashuser}">
						<form:input path="email" />
						<form:errors cssClass="fieldError" path="email" />
					</c:when>
					<c:otherwise>
						<c:out value="${user.email}" />
				        <form:hidden path="email" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.phoneNumber" />
			</th>
			<td>
				<form:input path="phoneNumber" />
				<form:errors cssClass="fieldError" path="phoneNumber" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.mobilePhone" />
			</th>
			<td>
				<form:input path="mobilePhone" />
				<form:errors cssClass="fieldError" path="mobilePhone" />
			</td>
		</tr>

<c:if test="${showEmployeeFields}">
		<tr>
			<th>
				<soak:label key="user.closestLeader" />
			</th>
			<td>
				<form:input path="closestLeader" />
				<form:errors cssClass="fieldError" path="closestLeader" />
			</td>
		</tr>
</c:if>

<c:if test="${showJobTitle}">
		<tr>
			<th>
				<soak:label key="user.jobTitle" />
			</th>
			<td>
				<form:input path="jobTitle" />
				<form:errors cssClass="fieldError" path="jobTitle" />
			</td>
		</tr>
</c:if>

<c:if test="${showWorkplace}">
		<tr>
			<th>
				<soak:label key="user.workplace" />
			</th>
			<td>
				<form:input path="workplace" />
				<form:errors cssClass="fieldError" path="workplace" />
			</td>
		</tr>
</c:if>

		<tr>
			<th>
				<soak:label key="user.website" />
			</th>
			<td>
				<form:input path="website" />
				<form:errors cssClass="fieldError" path="website" />
				<c:if test="${!empty user.website}">
					<a href="<c:out value="${user.website}"/>"><fmt:message
							key="user.visitWebsite" /> </a>
				</c:if>
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="course.organization" />
			</th>
			<td>
				<form:select path="organizationid" onchange="fillSelect(this);">
					<form:options items="${organizations}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="organizationid" />
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="course.organization2" />
			</th>
			<td>
				<form:select path="organization2id" onchange="fillSelect(this);">
					<form:options items="${organizations2}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors cssClass="fieldError" htmlEscape="false"
					path="organization2id" />
			</td>
		</tr>


<c:if test="${showServiceArea}">
		<tr>
			<th>
				<soak:label key="user.servicearea" />
			</th>
			<td>
				<spring:bind path="user.serviceAreaid">
					<select name="<c:out value="${status.expression}"/>">
						<c:forEach var="servicearea" items="${serviceareas}">
							<c:choose>
								<c:when test="${empty servicearea.id}">
									<option value="<c:out value="${servicearea.id}"/>"
										<c:if test="${servicearea.id == user.serviceAreaid}"> selected="selected"</c:if>>
										<c:out value="${servicearea.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<c:if
										test="${servicearea.organizationid == user.organizationid}">
										<option value="<c:out value="${servicearea.id}"/>"
											<c:if test="${servicearea.id == user.serviceAreaid}"> selected="selected"</c:if>>
											<c:out value="${servicearea.name}" />
										</option>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<span class="fieldError"><c:out
							value="${status.errorMessage}" escapeXml="false" /> </span>
				</spring:bind>
			</td>
		</tr>
</c:if>

<c:if test="${showInvoiceaddress}">
		<tr>
			<th>
				<soak:label key="user.invoiceAddress" />
			</th>
		</tr>

		<tr>
			<th>
				<soak:label key="user.invoiceAddress.name" />
			</th>
			<td>
				<form:input path="invoiceName" />
				<form:errors cssClass="fieldError" path="invoiceName" />
			</td>
		</tr>

		<tr>
			<th>
				<soak:label key="user.invoiceAddress.address" />
			</th>
			<td>
				<form:input path="invoiceAddress.address" />
				<form:errors cssClass="fieldError" path="invoiceAddress.address" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.invoiceAddress.postalCode" />
			</th>
			<td>
				<form:input path="invoiceAddress.postalCode" />
				<form:errors cssClass="fieldError" path="invoiceAddress.postalCode" />
			</td>
		</tr>
		<tr>
			<th>
				<soak:label key="user.invoiceAddress.city" />
			</th>
			<td>
				<form:input path="invoiceAddress.city" />
				<form:errors cssClass="fieldError" path="invoiceAddress.city" />
			</td>
		</tr>
</c:if>
		
		<c:if test="${param.from == 'list' or param.method == 'Add'}">
			<tr>
				<th>
					<label for="enabled">
						<fmt:message key="user.enabled" />
						?
					</label>
				</th>
				<td>
					<spring:bind path="user.enabled">
						<input type="hidden" name="_<c:out value="${status.expression}"/>"
							value="visible" />
						<input type="checkbox"
							name="<c:out value="${status.expression}"/>" value="true"
							<c:if test="${status.value}">checked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>
		</c:if>

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

// var focusControl = document.forms["userForm"].elements["<c:out value="${focus}"/>"];

// if (focusControl.type != "hidden" && !focusControl.disabled) {
//    focusControl.focus();
// }

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

// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
 var orgid=obj.options[obj.selectedIndex].value;
 // var tempopt = document.form.serviceaeraid.options
 var temp= document.user.serviceAreaid;
  
 while(temp.firstChild){
 	temp.removeChild(temp.firstChild);
 }
 
 var j = 0;
	<c:forEach var="servicearea" items="${serviceareas}">
		if ("<c:out value="${servicearea.id}"/>" == ""){
			temp.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>", true);
			j++
		}
		else if ("<c:out value="${servicearea.organizationid}"/>" == orgid){
			temp.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>");
			j++ 
		}
	</c:forEach>
}

// -->


</script>

<v:javascript formName="user" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>


