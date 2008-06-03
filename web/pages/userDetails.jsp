<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userDetail.title"/></title>
<content tag="heading"><c:out value="${user.fullName}"/></content>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="user.firstName"/>
        </th>
        <td>
	    	<c:out value="${user.firstName}"/>
        </td>
    </tr>
    
    <tr>
        <th>
            <fmt:message key="user.lastName"/>
        </th>
        <td>
	    	<c:out value="${user.lastName}"/>
        </td>
    </tr>

    <tr>
        <th>
            <fmt:message key="user.email"/>
        </th>
        <td>
          	<a href="<c:out value="mailto:${user.email}"/>"><c:out value="${user.email}"/></a>
        </td>
    </tr>
<!-- // venter til dette er rydda opp i.
    <tr>
        <th>
            <fmt:message key="extuser.organisation"/>
        </th>
        <td>
          	<c:out value="${user.organization}"/>
        </td>
    </tr>
-->
    <tr>
        <td class="buttonBar">            
            <input type="button" class="button" name="return" onclick="javascript:history.go(-1)"
                value="<fmt:message key="button.return"/>" />
        </td>
    </tr>
</table>
