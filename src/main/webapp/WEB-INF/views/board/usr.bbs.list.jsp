<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시판 목록</title>
</head>
<body>
<h2>게시판 목록</h2>

<table border="1">
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
        <th>액션</th>
    </tr>
    <c:forEach var="post" items="${posts}">
        <tr>
            <td>${post.id}</td>
            <td><a href="board/${post.id}">${post.title}</a></td>
            <td>${post.author.username}</td>
            <td>${post.createdAt}</td>
            <td>
                <!-- 본인 글이면 수정/삭제 가능 -->
                <sec:authorize access="hasRole('ADMIN') or principal.username == post.author.userId">
                    <a href="board/edit/${post.id}">수정</a>
                    <a href="board/delete/${post.id}">삭제</a>
                </sec:authorize>
            </td>
        </tr>
    </c:forEach>
</table>

<sec:authorize access="isAuthenticated()">
    <a href="board/new">새 글 쓰기</a>
</sec:authorize>

</body>
</html>
