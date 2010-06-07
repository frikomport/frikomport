<%@ include file="/common/taglibs.jsp"%>
<c:if test="${empty eZSessionid || showMenu}">
<div id="menuDiv">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="FriKomMenu"/>
</menu:useMenuDisplayer>
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
    <c:if test="${alternativeUserForm != null && !(isAdmin || isEducationResponsible || isEventResponsible || isReader)}">
    personalizeMenu('<c:out value="${alternativeUserForm.fullName}" />','<c:url value='/profileUser.html'/>');
    </c:if>
   
</script>