<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>글 수정</title>
</head>
<body>
<h2>글 수정</h2>
<form action="/board/update/${post.id}" method="post">
    <input type="text" name="title" value="${post.title}" required><br>
    <textarea name="content" required>${post.content}</textarea><br>
    <button type="submit">저장</button>
</form>
<a href="/board/list">목록</a>
</body>
</html>
