<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>利用者情報　詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <td><c:out value="${employee.code}" /></td>
                </tr>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${employee.name}" /></td>
                </tr>
                <tr>
                    <th>フォロー日時</th>
                    <fmt:parseDate value="${follow.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <form method="POST" action="<c:url value='?action=${actFol}&command=${commCrt}' />">
            <c:import url="_form.jsp" />
        </form>

        <p>
            <a href="<c:url value='?action=${actFol}&command=${commIdx}' />">フォロー 一覧</a>
        </p>
         <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">日報 一覧</a>
        </p>
    </c:param>
</c:import>