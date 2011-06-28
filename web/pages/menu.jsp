<%@ include file="/common/taglibs.jsp"%>
<c:if test="${empty eZSessionid || showMenu}">
<div id="menuDiv">
<authz:authorize ifAnyGranted="admin,eventresponsible,editor,reader">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="FriKomMenu"/>
    <!-- menu:displayMenu name="FriKomMenu_SVV"/ -->
</menu:useMenuDisplayer>
</authz:authorize>

<authz:authorize ifNotGranted="admin,eventresponsible,editor,reader">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="FriKomMenu"/>
    <!-- menu:displayMenu name="FriKomMenu_SVV_public"/ -->
</menu:useMenuDisplayer>
</authz:authorize>
</div>
</c:if>

<script type="text/javascript">
    function personalizeMenu(username,url){
        for(i=0;i<document.links.length;i++){
            if(document.links[i].className.match('base')){
                document.links[i].innerHTML=username;
                document.links[i].href=url;
                break;
            }
        }
    }

    initializeMenus();
    <c:if test="${alternativeUserForm != null && !(isAdmin || isEducationResponsible || isEventResponsible || isReader || isSVV)}">
    personalizeMenu('<c:out value="${alternativeUserForm.fullName}" />','<c:url context="${urlContext}" value='/profileUser.html'/>');
    </c:if>
   
</script>