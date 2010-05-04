<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<%@page import="org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder"%>

<%@page import="org.apache.commons.lang.StringUtils"%><fmt:message key="global.pageDecorator.url" var="decorURL"/>
<fmt:message key="global.pageDecorator.headPlaceholder" var="headPlaceholder"/>
<fmt:message key="global.pageDecorator.bodyPlaceholder" var="bodyPlaceholder"/>
<%
    Boolean useCmsUrl = true;
%>
<c:choose>
<c:when test="${decorURL != null && !empty decorURL}">
<c:catch var="exception">
	<c:import url="${decorURL}" var="ctmpl" charEncoding="utf-8"/>
</c:catch>
<c:if test="${exception != null}">
<%
    Exception exception = (Exception) pageContext.getAttribute("exception");
	Exception causeException = (Exception) exception.getCause();
	if (causeException instanceof java.io.IOException) {
        useCmsUrl = false;
	} else {
	    throw exception;
	}
%>
</c:if>
</c:when>
<c:otherwise>
<%
useCmsUrl = false;
%>
</c:otherwise>
</c:choose>

<%
    String ctmpl = (String) pageContext.getAttribute("ctmpl");
    String headPlaceholder = (String) pageContext.getAttribute("headPlaceholder");
    String bodyPlaceholder = (String) pageContext.getAttribute("bodyPlaceholder");

    if (StringUtils.isEmpty(ctmpl) || StringUtils.isEmpty(headPlaceholder) || StringUtils.isEmpty(bodyPlaceholder)) {
        ctmpl = "";
        useCmsUrl = false;
    }
    // Get the position of the placeholders
    int baseStartPos = ctmpl.indexOf("<base ");
    int baseEndPos = ctmpl.indexOf(">", baseStartPos+1);
    int headStartPos = ctmpl.indexOf(headPlaceholder);
    int bodyStartPos = ctmpl.indexOf(bodyPlaceholder, headStartPos+1);

    if (headStartPos == -1 || bodyStartPos == -1) {
        useCmsUrl = false;
    }

    if (useCmsUrl) {
        if (baseStartPos > -1 && baseStartPos < headStartPos) {
            out.write(ctmpl.substring(0, baseStartPos));
            out.write(ctmpl.substring(baseEndPos + 1, headStartPos));
        } else {
            out.write(ctmpl.substring(0, headStartPos));  // Write out from start of file to the head placeholder.
        }
    } else {
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
<%
    }
%>
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
<%
    if (useCmsUrl) {
        if (baseStartPos > headStartPos) {
            out.write(ctmpl.substring(headStartPos + headPlaceholder.length(), baseStartPos));
            out.write(ctmpl.substring(baseEndPos + 1, bodyStartPos));
        } else {
            out.write(ctmpl.substring(headStartPos + headPlaceholder.length(), bodyStartPos));
        }
    } else {
%>
        </head>
        <body<decorator:getProperty property="body.id" writeEntireProperty="true"/>>
<%        
    }
%>

    <!--[if lte IE 6]>
    <div class="standardsNote">
        <fmt:message key="errors.browser.warning"/>
    </div>
    <![endif]-->  

    <div id="screen">
        <c:import url="/WEB-INF/pages/menu.jsp"/>
        <div id="content">
            <h1><decorator:getProperty property="page.heading"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
    </div>
    <%@ include file="/common/tracker.jsp" %>
<%
    if (useCmsUrl) {
        out.write(ctmpl.substring(bodyStartPos + bodyPlaceholder.length()));
    } else {
%>
        </body>
        </html>
<%      
    }
%>