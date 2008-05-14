<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="extuserDetail.title"/></title>
<content tag="heading"><c:out value="${user.name}"/></content>

<table class="detail">
    <tr>
        <th>
            <fmt:message key="extuser.firstName"/>
        </th>
        <td>
	    	<c:out value="${user.first_name}"/>
        </td>
    </tr>
    
    <tr>
        <th>
            <fmt:message key="extuser.lastName"/>
        </th>
        <td>
	    	<c:out value="${user.last_name}"/>
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
          	<c:out value="${user.kommune}"/>
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
