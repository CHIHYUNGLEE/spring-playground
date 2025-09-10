<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container">
    <h2>회원 관리</h2>

    <!-- 상태 필터 -->
    <form method="get" action="/user/list">
        <select name="status" onchange="this.form.submit()">
            <option value="ALL" ${status == 'ALL' ? 'selected' : ''}>전체</option>
            <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>정상 회원</option>
            <option value="INACTIVE" ${status == 'INACTIVE' ? 'selected' : ''}>탈퇴 회원</option>
        </select>
    </form>

    <table border="1" style="width:100%; margin-top:20px;">
        <thead>
            <tr>
                <th>ID</th>
                <th>아이디</th>
                <th>이름</th>
                <th>상태</th>
                <th>액션</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.userId}</td>
                    <td>${user.userName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.status == 1}">정상</c:when>
                            <c:otherwise>탈퇴</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${user.status == 1}">
                            <!-- 탈퇴 처리 -->
                            <form method="post" action="/user/deactivate/${user.id}" style="display:inline;">
                                <button type="submit" onclick="return confirm('정말 탈퇴 처리하시겠습니까?');">탈퇴</button>
                            </form>
                        </c:if>
                        <!-- 물리적 삭제 -->
                        <form method="post" action="/user/delete/${user.id}" style="display:inline;">
                            <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
