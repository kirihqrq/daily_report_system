<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commFollow" value="${ForwardConst.CMD_SHOW_FOLLOW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>フォロー　一覧</h2>
        <table id="follow_list">
            <tbody>
                <tr>
                    <th class="follow_name">フォロー中</th>
                    <th class="follow_date">フォロー日時</th>
                </tr>
                <c:forEach var="follow" items="${follows}" varStatus="status">
                    <fmt:parseDate value="${follow.createdAt}" pattern="yyyy-MM-dd" var="createDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="follow_name"><a href="<c:url value='?action=${actFol}&command=${commFollow}&id=${follow.opponent.id}' />">
                                <c:out value="${follow.opponent.name}" /></a></td>
                        <td class="follow_date"><fmt:formatDate value='${createDay}' pattern='yyyy-MM-dd' /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${follows_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((follows_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actFol}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:param>
</c:import>