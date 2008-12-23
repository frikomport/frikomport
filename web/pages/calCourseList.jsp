<%@ page language="java" pageEncoding="UTF-8" contentType="text/calendar; charset=UTF-8" %>
<%@ taglib prefix="ical" uri="http://franchu.net/ical-taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<c:if test="${courseList[0] != null}">
<ical:calendar prodId="-//Know IT Objectnet AS//FriKomPort//NO">
<c:forEach var="course" items="${courseList}">
<ical:event>
<ical:uid><c:out value="${course.id}" escapeXml="false"/></ical:uid>
<ical:summary><c:out value="${course.name}" escapeXml="true"/></ical:summary>
<ical:datestart format="yyyy-MM-dd HH:mm:ss.S"><c:out value="${course.startTime}" escapeXml="false"/></ical:datestart>
<ical:dateend format="yyyy-MM-dd HH:mm:ss.S" ><c:out value="${course.stopTime}" escapeXml="false"/></ical:dateend>
<ical:description><c:out value="${course.description}" escapeXml="true"/></ical:description>
<ical:location><c:out value="${course.location.name}" escapeXml="true"/></ical:location>
</ical:event>
</c:forEach>
</ical:calendar>
</c:if>