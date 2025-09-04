<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Home - Spring Playground</title>
    <style>
        body { font-family:'Poppins', sans-serif; display:flex; align-items:center; justify-content:center; height:100vh; background:linear-gradient(135deg, #667eea, #764ba2); color:#fff;}
        .container { text-align:center;}
        .btn { background:white; color:#764ba2; padding:12px 30px; border-radius:30px; text-decoration:none; font-weight:bold; margin:0 10px;}
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to Spring Playground 🌸</h1>
    <p>포트폴리오 프로젝트에 오신 걸 환영합니다.<br>로그인 후 다양한 기능을 이용해 보세요!</p>
    <div class="btn-group">
        <% if (session.getAttribute("username") != null) { %>
            <span>안녕하세요, <%= session.getAttribute("username") %>님</span>
            <a class="btn" href="logout">로그아웃</a>
        <% } else { %>
            <a class="btn" href="login">로그인</a>
            <a class="btn" href="register">회원가입</a>
        <% } %>
    </div>
</div>
</body>
</html>
