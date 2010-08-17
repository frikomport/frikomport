<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<title><fmt:message key="404.title"/></title>
<content tag="heading"><fmt:message key="404.title"/></content>

<p>
    <fmt:message key="404.message">
        <fmt:param><c:url context="${urlContext}" value="/listCourses.html"/></fmt:param>
    </fmt:message>
</p>
</page:applyDecorator>