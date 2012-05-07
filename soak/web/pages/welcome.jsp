<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="welcome.title"/></title>

<fmt:message key="userRegistrationList.item" var="item"/>
<fmt:message key="userRegistrationList.items" var="items"/>

<content tag="heading"><fmt:message key="welcome.title"/></content>
<fmt:message key="welcome.body1"></fmt:message>
<div style="padding-top:15px; padding-bottom:5px;" class="detail">
<form action="listCourses.html" name="postalcode" method="post">
	<label><fmt:message key="welcome.postalcode"/></label>
	<input type="text" name="postalcode" size="5" maxlength="4" style="vertical-align:middle;"/> &nbsp;
    <input type="submit" value="<fmt:message key="welcome.searchButtonlabel"/>"/>    	
</form>
</div>
<fmt:message key="welcome.body2"/>
<fmt:message key="welcome.body3"/>
<fmt:message key="welcome.body4"/>
<fmt:message key="welcome.body5"/>
