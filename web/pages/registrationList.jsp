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
        <li>
            <soak:label key="registration.organization" styleClass="required"/>
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

<c:if test="${useServiceArea}">
        <li>
            <soak:label key="registration.serviceArea" styleClass="required"/>
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

        <li>
            <soak:label key="registration.course" styleClass="required"/>
            <spring:bind path="registration.courseid">
                  <select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <c:forEach var="theCourse" items="${courses}">
                      <option value="<c:out value="${theCourse.id}"/>"
                          <c:if test="${theCourse.id == registration.courseid}"> selected="selected"</c:if>>
                          <fmt:formatDate value="${theCourse.startTime}" type="both" pattern="${dateformat} - "/><c:out value="${theCourse.name}"/>
                      </option>
                    </c:forEach>
                  </select>
                <span class="fieldError"><c:out value="${status.errorMessage}" escapeXml="false"/></span>
            </spring:bind>
        </li>

<c:if test="${!isSVV}">
    </ul>
    <ul>
        <li>
            <soak:label key="registration.reserved" styleClass="required"/>
            <select id="reservedField" name="reservedField">
                <option value="2" <c:if test="${reservedValue == 2}"> selected </c:if> /> <c:out value='${reserved["null"]}'/></option>
                <option value="1" <c:if test="${reservedValue == 1}"> selected </c:if> /> <c:out value='${reserved["true"]}'/></option>
                <option value="0" <c:if test="${reservedValue == 0}"> selected </c:if> /> <c:out value='${reserved["false"]}'/></option>
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
    export="true" requestURI="">

	<c:choose> 
		<c:when test="${registrationList.canceled}">
		<c:set var="tdClass" value="canceled" />
		</c:when>
		<c:otherwise>
			<c:set var="tdClass" value="" />
		</c:otherwise>
	</c:choose>
    
<c:if test="${isAdmin || isEducationResponsible || isEventResponsible || isReader}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
		<c:if test="${(isAdmin || isEducationResponsible || (!isSVV && isEventResponsible && registrationList.course.responsible.username == username) || (isSVV && isEventResponsible && registrationList.course.organization2id == user.organization2id)) && !registrationList.canceled}">
        <a href='<c:url value="/performRegistration.html"><c:param name="id" value="${registrationList.id}"/><c:param name="courseId" value="${registrationList.courseid}"/></c:url>'>
            <img src="<c:url value="/images/pencil.png"/>" alt="<fmt:message key="button.edit"/>" title="<fmt:message key="button.edit"/>"></img>
        </a>
		</c:if>
    </display:column>
</c:if>

<c:if test="${showCourseName}">
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.name" sortProperty="course.name" class="${tdClass}">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${registrationList.course.id}"/></c:url>" 
         title="<c:out value="${registrationList.course.description}"/>"><c:out value="${registrationList.course.name}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="course.name" sortable="true" headerClass="sortable" titleKey="course.name" class="${tdClass}"/>
</c:if>

<c:choose>
<c:when test="${showCourseName}">
    <display:column sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="course.startTime" class="${tdClass}">
         <fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:when>
<c:otherwise>
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
         <a href="<c:url value="/detailsCourse.html"><c:param name="id" value="${registrationList.course.id}"/></c:url>" 
         title="<c:out value="${registrationList.course.description}"/>"><fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" sortable="true" headerClass="sortable" titleKey="course.startTime" sortProperty="startTime">
		<fmt:formatDate value="${registrationList.course.startTime}" type="both" pattern="${dateformat} ${timeformat}"/>
    </display:column>
</c:otherwise>    
</c:choose>
    
    <display:column property="firstName" sortable="true" headerClass="sortable"
         titleKey="registration.firstName" class="${tdClass}"/>
         
    <display:column property="lastName" sortable="true" headerClass="sortable"
         titleKey="registration.lastName" class="${tdClass}"/>
         
    <display:column media="html" sortable="true" headerClass="sortable" titleKey="registration.email" class="${tdClass}">
         <a href="mailto:<c:out value="${registrationList.email}"/>"><c:out value="${registrationList.email}"/></a>
    </display:column>
    <display:column media="csv excel xml pdf" property="email" sortable="true" headerClass="sortable" titleKey="registration.email"/>
    
    <display:column property="phone" sortable="true" headerClass="sortable"
         titleKey="registration.phone" class="${tdClass}"/>
         
    <display:column property="mobilePhone" sortable="true" headerClass="sortable"
         titleKey="registration.mobilePhone" class="${tdClass}"/>
         
    <display:column property="course.organization.name" sortable="true" headerClass="sortable"
         titleKey="course.organization" class="${tdClass}"/>

    <display:column property="course.organization2.name" sortable="true" headerClass="sortable"
         titleKey="course.organization2" class="${tdClass}"/>

	
<c:if test="${useServiceArea}">
	<display:column property="serviceArea.name" sortable="true" headerClass="sortable" 
		titleKey="registration.serviceArea" class="${tdClass}"/>
</c:if>
         
<c:if test="${usePayment}">
    <display:column sortable="true" headerClass="sortable"
         titleKey="registration.invoiced">
		<c:if test="${registrationList.invoiced == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.invoiced == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
</c:if>
    
<c:if test="${!isSVV}">
    <display:column sortable="true" headerClass="sortable"
         titleKey="registration.attended">
		<c:if test="${registrationList.attended == true}"><fmt:message key="checkbox.checked"/></c:if>
		<c:if test="${registrationList.attended == false}"><fmt:message key="checkbox.unchecked"/></c:if>
    </display:column>
</c:if>
    
<c:if test="${usePayment}">
    <display:column media="csv excel xml pdf" property="invoiceName" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.name"/>
    <display:column media="csv excel xml pdf" property="invoiceAddress.address" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.address"/>
    <display:column media="csv excel xml pdf" property="invoiceAddress.city" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.city"/>
    <display:column media="csv excel xml pdf" property="invoiceAddress.postalCode" sortable="true" headerClass="sortable" titleKey="registrationList.invoiceAddress.postalCode"/>
</c:if>
    
    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
</display:table>
</c:if>

<c:out value="${buttons}" escapeXml="false"/>

<%--
<script type="text/javascript">
highlightTableRows("registrationList");
</script>
--%>