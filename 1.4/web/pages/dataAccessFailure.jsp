<%@ include file="/common/taglibs.jsp" %>

<title><fmt:message key="dataErrorPage.title"/></title>
<content tag="heading"><fmt:message key="dataErrorPage.heading"/></content>

<%="<!--"%>   
<c:out value="${requestScope.exception.message}"/>

<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
<%="-->"%>   

<p style="text-align: center; margin-top: 20px">
    <a href="http://community.webshots.com/photo/56793801/56801692jkyHaR"
        title="Hawaii, click to Zoom In">
    <img style="border: 0" 
        src="<c:url value="/images/403.jpg"/>" 
        alt="Hawaii" /></a>
</p>
