    <fmt:message key="google.analytics.trackingid" var="trackingid"/>
    <c:if test="${!empty trackingid}">
    <!-- Google Analytics code -->
    <script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
    try {
    var pageTracker = _gat._getTracker("<c:out value="${trackingid}"/>");
    pageTracker._trackPageview();
    } catch(err) {}
    </script>
    </c:if>