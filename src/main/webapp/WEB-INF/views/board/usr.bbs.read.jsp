<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>${post.title}</title>
</head>
<body>
    <h2>${post.title}</h2>
    <p>${post.content}</p>
    <p>작성자: ${post.author.username}</p>
    <p>작성일: ${post.createdAt}</p>

    <sec:authorize access="hasRole('ADMIN') or principal.username == post.author.userId">
        <a href="/board/edit/${post.id}">수정</a>
        <a href="/board/delete/${post.id}">삭제</a>
    </sec:authorize>

    <a href="/board/list">목록</a>
</body>
</html>
