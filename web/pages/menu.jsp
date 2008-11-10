<%@ include file="/common/taglibs.jsp"%>
<c:if test="${empty eZSessionid || showMenu}">
<div id="menuDiv">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
<%--    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="FileUpload"/>
    <menu:displayMenu name="FlushCache"/>
    <menu:displayMenu name="Clickstream"/>--%>
    <menu:displayMenu name="FriKomMenu"/>
</menu:useMenuDisplayer>
</div>
</c:if>

<script type="text/javascript">
    initializeMenus();
</script>