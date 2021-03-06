<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="registrationList.title"/></title>
<content tag="heading"><fmt:message key="registrationList.heading"/></content>

<script type="text/javascript">
// Code to change the select list for service aera based on organization id.
function fillSelect(obj){
 var orgid=obj.options[obj.selectedIndex].value;
 var serviceArea= document.registrationList.serviceAreaid;

    while(serviceArea.firstChild){
        serviceArea.removeChild(serviceArea.firstChild);
    }

    var j = 0;
<c:forEach var="servicearea" items="${serviceareas}">
    if ("<c:out value="${servicearea.id}"/>" == ""){
        serviceArea.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>", true);
        j++;
    }
else if ("<c:out value="${servicearea.organizationid}"/>" == orgid){
        serviceArea.options[j]=new Option("<c:out value="${servicearea.name}"/>", "<c:out value="${servicearea.id}"/>");
        j++ ;
    }
</c:forEach>
}
</script>

<fmt:message key="date.format" var="dateformat"/>
<fmt:message key="time.format" var="timeformat"/>
<fmt:message key="registrationList.item" var="item"/>
<fmt:message key="registrationList.items" var="items"/>

<c:choose>
<c:when test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
<div class="searchForm">
    <form method="post" action="<c:url value="/listRegistrations.html"/>" id="registrationList" name="registrationList">
    <INPUT type="hidden" id="ispostbackregistrationlist" name="ispostbackregistrationlist" value="1"/> 
    <ul>

<c:if test="${!isSVV}">
        <li>
            <spring:bind path="registration.organizationid">
                  <select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" onchange="fillSelect(this);">
                    <c:forEach var="organization" items="${organizations}">
                      <option value="<c:out value="${organization.id}"/>"
                          <c:if test="${organization.id == registration.organizationid}"> selected="selected"</c:if>>
                        <c:out value="${organization.name}"/>
                      </option>
                    </c:forEach>
                  </select>
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </li>
</c:if>

<c:if test="${useServiceArea}">
        <li>
            <spring:bind path="registration.serviceAreaid">
                <select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <c:forEach var="servicearea" items="${serviceareas}">
                        <c:choose>
                            <c:when test="${empty servicearea.id}">
                                <option value="<c:out value="${servicearea.id}"/>"
                                    <c:if test="${servicearea.id == registration.serviceAreaid}"> selected="selected"</c:if>>
                                    <c:out value="${servicearea.name}" />
                                </option>
                            </c:when>
                            <c:otherwise>
                                <c:if
                                    test="${servicearea.organizationid == registration.organizationid}">
                                    <option value="<c:out value="${servicearea.id}"/>"
                                        <c:if test="${servicearea.id == registration.serviceAreaid}"> selected="selected"</c:if>>
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
        </li>
</c:if>

<c:if test="${!isSVV}">
        <li>
            <soak:label key="registration.status" styleClass="required"/>
            <select id="statusField" name="statusField">
                <option value="2" <c:if test="${statusValue == 2}"> selected </c:if> /> <c:out value='${status["2"]}'/></option>
                <option value="1" <c:if test="${statusValue == 1}"> selected </c:if> /> <c:out value='${status["1"]}'/></option>
                <option value="3" <c:if test="${statusValue == 3}"> selected </c:if> /> <c:out value='${status["3"]}'/></option>
                <option value="0" <c:if test="${statusValue == 0}"> selected </c:if> /> <c:out value='${status["null"]}'/></option>
            </select>
        </li>
</c:if>    

<c:if test="${usePayment}">
        <li>
            <soak:label key="registration.invoiced" styleClass="required"/>
            <select id="invoicedField" name="invoicedField">
                <option value="2" <c:if test="${invoicedValue == 2}"> selected </c:if> /> <c:out value='${invoiced["null"]}'/></option>
                <option value="1" <c:if test="${invoicedValue == 1}"> selected </c:if> /> <c:out value='${invoiced["true"]}'/></option>
                <option value="0" <c:if test="${invoicedValue == 0}"> selected </c:if> /> <c:out value='${invoiced["false"]}'/></option>
            </select>
        </li>
</c:if>

<c:if test="${!isSVV}">
        <li>
            <soak:label key="registration.attended" styleClass="required"/>
            <select id="attendedField" name="attendedField">
				<option value="2" <c:if test="${attendedValue == 2}"> selected </c:if> /> <c:out value='${attended["null"]}'/></option>
				<option value="1" <c:if test="${attendedValue == 1}"> selected </c:if> /> <c:out value='${attended["true"]}'/></option>
				<option value="0" <c:if test="${attendedValue == 0}"> selected </c:if> /> <c:out value='${attended["false"]}'/></option>
			</select>
        </li>
</c:if>

        <li>
            <label class="required"><fmt:message key="registration.lastName"/>:</label>
            <input type="text" name="lastName" id="lastName" value="<c:out value="${registration.lastName}"/>" size="15"/>
        </li>
        <li>
            <label class="required"><fmt:message key="registration.firstName"/>:</label>
            <input type="text" name="firstName" id="firstName" value="<c:out value="${registration.firstName}"/>" size="15"/>
        </li>


        <li>
            <soak:label key="course.includeHistoric" styleClass="required"/>
            <INPUT type="hidden" name="_historic" value="0"/>
		    <INPUT type="checkbox" id="historic" name="historic" value="1"
		    <c:if test="${historic == true}"> checked </c:if> />
        </li>
        
        <li>
            <button type="submit" name="search" onclick="bCancel=false" style="margin-right: 5px">
				<fmt:message key="button.search"/>
			</button>
        </li>
    </ul>
    </form>
</div>
</c:when>
<c:otherwise>
    <div class="message" style="font-size: 12px"><fmt:message key="access.denied" /></div>
</c:otherwise>
</c:choose>



<c:set var="buttons">
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
<display:table name="${registrationList}" cellspacing="0" cellpadding="0"
    id="registrationList" pagesize="${itemCount}" class="list" 
    export="true" requestURI="listRegistrations.html">

	<c:choose> 
		<c:when test="${!empty registrationList && registrationList.canceled}">
		<c:set var="tdClass" value="canceled" />
		</c:when>
		<c:otherwise>
			<c:set var="tdClass" value="" />
		</c:otherwise>
	</c:choose>
	
	<c:choose> 
		<c:when test="${!empty registrationList && registrationList.course.status == 3}">
		<c:set var="tdClass2" value="canceled" />
		</c:when>
		<c:otherwise>
			<c:set var="tdClass2" value="" />
		</c:otherwise>
	</c:choose>
    
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<c:if test="${(isAdmin || isEducationResponsible || (!isSVV && isEventResponsible && registrationList.course.responsible.username == currentUserForm.username) || (isSVV && isEventResponsible && (registrationList.course.organization2id == currentUserForm.organization2id || registrationList.course.responsible.username == currentUserForm.username))) && !registrationList.canceled}">
        <a href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseId" value="${registrationList.courseid}"/></c:url>'>
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
		</c:if>
    </display:column>
</c:if>

<c:if test="${showCourseName}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="course.name" class="${tdClass2}">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${registrationList.course.id}"/></c:url>" 
         title="<c:out value="${registrationList.course.description}"/>"><c:out value="${registrationList.course.name}"/></a>
    </display:column>
    <display:column escapeXml="true" media="csv excel xml pdf" property="course.name" sortable="true" headerClass="sortable" titleKey="course.name" class="${tdClass2}"/>
</c:if>

<c:choose>
<c:when test="${showCourseName}">
    <display:column escapeXml="true" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime" class="${tdClass2}">
         <fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:when>
<c:otherwise>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime" class="${tdClass2}">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${registrationList.course.id}"/></c:url>" 
         title="<c:out value="${registrationList.course.description}"/>"><fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/></a>
    </display:column>
    <display:column escapeXml="true" media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime" class="${tdClass2}">
		<fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:otherwise>    
</c:choose>
    
    <display:column escapeXml="true" property="lastName" sortable="true" headerClass="sortable" titleKey="registration.lastName" class="${tdClass}"/>
         
    <display:column escapeXml="true" property="firstName" sortable="true" headerClass="sortable" titleKey="registration.firstName" class="${tdClass}"/>

<c:if test="${useBirthdateForRegistration}">
    <display:column escapeXml="true" sortable="true" headerClass="sortable" titleKey="registration.birthdate" sortProperty="birthdate" class="${tdClass}">
         <fmt:formatDate value="${registrationList.birthdate}" type="date" pattern="${dateformat}"/>
    </display:column>
</c:if>         
         
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.email" class="${tdClass}">
         <a href="mailto:<c:out value="${registrationList.email}"/>"><c:out value="${registrationList.email}"/></a>
    </display:column>
    <display:column escapeXml="true" media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="registration.email"/>
    
	<c:if test="${!isSVV}">
    <display:column escapeXml="true" property="phone" sortable="true" headerClass="sortable" titleKey="registration.phone" class="${tdClass}"/>
	</c:if>
         
    <display:column escapeXml="true" property="mobilePhone" sortable="true" headerClass="sortable" titleKey="registration.mobilePhone" class="${tdClass}"/>
         
	<c:if test="${!isSVV}">
    <display:column escapeXml="true" property="organization.name" sortable="true" headerClass="sortable" titleKey="registration.organization" class="${tdClass}"/>
	</c:if>

	<c:if test="${isSVV}">
    <display:column sortable="true" headerClass="sortable" titleKey="registration.invoiceAddress.postalCode.short" class="${tdClass}">
    	<c:out value="${registrationList.invoiceAddress.postalCode}"/>
    </display:column>
	</c:if>
	
<c:if test="${useServiceArea}">
	<display:column escapeXml="true" property="serviceArea.name" sortable="true" headerClass="sortable" titleKey="registration.serviceArea.export" class="${tdClass}"/>
</c:if>

    <display:column escapeXml="true" sortable="true" headerClass="sortable" titleKey="registration.status">
		<c:if test="${registrationList.status == 1}"><fmt:message key="registrationList.status.1"/></c:if>
		<c:if test="${registrationList.status == 2}"><fmt:message key="registrationList.status.2"/></c:if>
		<c:if test="${registrationList.status == 3}"><fmt:message key="registrationList.status.3"/></c:if>
    </display:column>
         
<c:if test="${usePayment}">
    <display:column sortable="true" headerClass="sortable"
         titleKey="registration.invoiced">
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
</c:if>
    
<c:if test="${!isSVV}">
    <display:column sortable="true" headerClass="sortable" titleKey="registration.attended.export">
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
</c:if>

<c:if test="${useParticipants}">
	<display:column escapeXml="true" property="participants" sortable="true" headerClass="sortable" titleKey="registration.participants" class="${tdClass}"/>
</c:if>
    
<c:if test="${usePayment}">
    <display:column escapeXml="true" media="csv excel xml pdf" property="invoiceName" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.name"/>
    <display:column escapeXml="true" media="csv excel xml pdf" property="invoiceAddress.address" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.address"/>
    <display:column escapeXml="true" media="csv excel xml pdf" property="invoiceAddress.city" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.city"/>
    <display:column escapeXml="true" media="csv excel xml pdf" property="invoiceAddress.postalCode" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.postalCode"/>
</c:if>
    
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

<c:if test="${!isSVV}">
    <display:setProperty name="export.ics" value="true"/>
</c:if>

<c:if test="${isSVV}">
    <display:setProperty name="export.xml" value="false"/>
</c:if>


</display:table>
</c:if>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("registrationList");
</script>
--%>