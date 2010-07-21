        <!-- HTTP 1.1 -->
        <meta http-equiv="Cache-Control" content="no-store"/>
        <!-- HTTP 1.0 -->
        <meta http-equiv="Pragma" content="no-cache"/>
        <!-- Prevents caching at the Proxy Server -->
        <meta http-equiv="Expires" content="0"/>
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
        <c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
        <c:if test="${!isSVV}">
	        <meta name="author" content="FriKomPort (frikomport@gmail.com)"/>
	        <link rel="icon" href="<c:url context="${urlContext}" value='/images/favicon.ico'/>"/>
        </c:if>
        <script type="text/javascript" src="<c:url context="${urlContext}" value='/scripts/calendar.js'/>"></script>
        