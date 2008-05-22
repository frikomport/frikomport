<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%-- Include common set of meta tags for each layout --%>
        <%@ include file="/common/meta.jsp" %>
        <title><fmt:message key="webapp.prefix"/><decorator:title/></title>
        
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/default.css'/>" /> 
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/helptip.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/print.css'/>" />    

        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script> 
        <script type="text/javascript" src="<c:url value='/scripts/effects.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/helptip.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" /> 
        <script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
        <decorator:head/>
        <style type="text/css" media="all">
            div.standardsNote {background: #FFFFCC; border: 1px solid blue; margin-bottom: 10px; padding: 5px}
        </style>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/>>

    <div class="standardsNote">
        <fmt:message key="errors.browser.warning"/>
    </div>

    <div id="screen">
<table width="100%">
<tr>
    <td valign="top" width="167px">
        <c:import url="/WEB-INF/pages/menu.jsp"/>
    </td>
    <td>
        <div id="content">
            <h1><decorator:getProperty property="page.heading"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
    </td>
</tr>
</table>

    </div>

</body>
</html>
