<%@ include file="/common/taglibs.jsp" %>

<%
      out.write("<H1>"+String.valueOf(request.getAttribute("courseErrorPage.heading.localized"))+"</H1>");
%> 

<c:out value="${requestScope.exception.localizedMessage}"/>
