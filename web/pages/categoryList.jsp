<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="categoryList.title" />
</title>
<content tag="heading">
<fmt:message key="categoryList.heading" />
</content>

<c:set var="buttons">
<c:if test="${isAdmin || isEducationResponsible}">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editCategory.html"/>?method=Add&from=list'">
        <fmt:message key="button.add"/>
    </button>
</c:if>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<display:table name="${categoryList}" cellspacing="0" cellpadding="0"
    requestURI="" defaultsort="1" id="category" pagesize="25"
    class="list" export="true">

    <%-- Table columns --%>
    <display:column property="name" sortable="true"
        headerClass="sortable" url="/detailsCategory.html?from=list"
        paramId="id" paramProperty="id" titleKey="category.name" />

<c:if test="${isAdmin || isEducationResponsible}">
    <display:column media="html" sortable="false" headerClass="sortable" titleKey="button.heading">
<c:if test="${isAdmin || isEducationResponsible}">
        <button type="button" onclick="location.href='<c:url value="/editCategory.html"><c:param name="id" value="${category.id}"/></c:url>'">
            <fmt:message key="button.edit"/>
        </button>
</c:if>
    </display:column>
</c:if>

    <fmt:message var="category" key="categoryList.category" />
    <fmt:message var="categories" key="categoryList.categories" />

    <display:setProperty name="paging.banner.item_name" value="${category}" />
    <display:setProperty name="paging.banner.items_name" value="${categories}" />

    <display:setProperty name="export.excel.filename" value="Category List.xls" />
    <display:setProperty name="export.csv.filename" value="Category List.csv" />
    <display:setProperty name="export.pdf.filename" value="Category List.pdf" />
</display:table>

<%-- 
<c:out value="${buttons}" escapeXml="false" />
%-->

<%--
<script type="text/javascript">
highlightTableRows("users");
</script>
--%>
