<%@ include file="/common/taglibs.jsp" %>

<title><fmt:message key="dataErrorPage.title"/></title>
<content tag="heading"><fmt:message key="dataErrorPage.heading"/></content>

<c:out value="${requestScope.exception.message}"/>

<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
