<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="login.title"/></title>
<content tag="heading"><fmt:message key="login.heading"/></content>
<body id="login"/>

<p><fmt:message key="welcome.message"/></p>

<%-- Include the login form --%>
<c:import url="/WEB-INF/pages/loginForm.jsp"/>

<p><fmt:message key="login.passwordHint"/></p>
