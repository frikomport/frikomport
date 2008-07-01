<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
    <title><fmt:message key="errorPage.title"/></title>
    <link rel="stylesheet" type="text/css" media="all" 
        href="<c:url value="/styles/default.css"/>" /> 
</head>

<body>
<div id="screen">
    <div id="content">
    <h1><fmt:message key="errorPage.heading"/></h1>
    <%@ include file="/common/messages.jsp" %>
 <%="<!--"%>   
 <% if (exception != null) { %>
    <% exception.printStackTrace(new java.io.PrintWriter(out)); %>
 <% } else if ((Exception)request.getAttribute("javax.servlet.error.exception") != null) { %>
    <% ((Exception)request.getAttribute("javax.servlet.error.exception")).printStackTrace(new java.io.PrintWriter(out)); %>
 <% } %>
 <%="-->"%>   
    </div>
    
<p style="text-align: center; margin-top: 20px">
    <a href="http://community.webshots.com/photo/56793801/56801692jkyHaR"
        title="Hawaii, click to Zoom In">
    <img style="border: 0" 
        src="<c:url value="/images/403.jpg"/>" 
        alt="Hawaii" /></a>
</p>
</div>
</body>
</html>
