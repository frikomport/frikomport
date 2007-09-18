<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty eZSessionid}">
<div id="menu">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="FileUpload"/>
    <menu:displayMenu name="FlushCache"/>
    <menu:displayMenu name="Clickstream"/>
    <!--Municipalities-START-->
    <menu:displayMenu name="MunicipalitiesMenu"/>
    <!--Municipalities-END-->
    <!--ServiceArea-START-->
    <menu:displayMenu name="ServiceAreaMenu"/>
    <!--ServiceArea-END-->
    <!--Location-START-->
    <menu:displayMenu name="LocationMenu"/>
    <!--Location-END-->
    <!--Person-START-->
    <menu:displayMenu name="PersonMenu"/>
    <!--Person-END-->
    <!--Course-START-->
    <menu:displayMenu name="CourseMenu"/>
    <!--Course-END-->
    <!--Registration-START-->
    <menu:displayMenu name="RegistrationMenu"/>
    <!--Registration-END-->
</menu:useMenuDisplayer>
</div>
</c:if>

<script type="text/javascript">
    initializeMenus();
</script>