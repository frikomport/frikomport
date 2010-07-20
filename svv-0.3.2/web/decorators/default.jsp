<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<%@page import="org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder"%>
<%@page import="no.unified.soak.Constants"%>
<%@page import="no.unified.soak.model.User"%>

<c:out value="${pageDecorationBeforeHeadPleaceholder}" escapeXml="false"/>

        <%-- Include common set of meta tags for each layout --%>
        <%@ include file="/common/meta.jsp" %>
        <title><fmt:message key="webapp.prefix"/><decorator:title/></title>
        
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/default.css'/>" /> 
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/helptip.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/print.css'/>" />    
<c:if test="${isSVV}">
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/svv.css'/>" />    
</c:if>
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

<c:out value="${pageDecorationBetweenHeadAndBodyPleaceholders}" escapeXml="false"/>

<%
User user = (User)session.getAttribute(Constants.USER_KEY);
User hashUser = (User)session.getAttribute(Constants.ALT_USER_KEY);
boolean divstart = false;
if (user != null) {
	out.write("<div id='loggedin'>Innlogget: " + user.getUsername() + " ["+ (request.getAttribute("userRolesString")==null?"":request.getAttribute("userRolesString")) + "]");
	divstart=true;
}
if (hashUser != null) {
	if (!divstart) {
		out.write("<div id='loggedin'>");
		divstart=true;
	} else {
		out.write("&nbsp;&nbsp;&nbsp;");
	}
	out.write("Publikumsbruker: " + hashUser.getUsername());
}
if (divstart) {
	out.write("</div>");
}
%>

    <!--[if lte IE 6]>
    <div class="standardsNote">
        <fmt:message key="errors.browser.warning"/>
    </div>
    <![endif]-->  

    <div id="screen">
        <c:import url="/WEB-INF/pages/menu.jsp" charEncoding="UTF-8"/>
        <div id="content">
            <h1><decorator:getProperty property="page.heading"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
    </div>
    <%@ include file="/common/tracker.jsp" %>

<c:out value="${pageDecorationAfterBodyPleaceholder}" escapeXml="false"/>
