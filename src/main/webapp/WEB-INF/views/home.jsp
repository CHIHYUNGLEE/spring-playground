<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - Spring Playground</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .container {
            text-align: center;
        }
        h1 {
            font-size: 3em;
            margin-bottom: 0.5em;
        }
        p {
            font-size: 1.2em;
            margin-bottom: 2em;
        }
        .btn-group {
            display: flex;
            justify-content: center;
            gap: 20px;
        }
        .btn {
            background: #fff;
            color: #764ba2;
            padding: 12px 30px;
            border-radius: 30px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1em;
            transition: 0.3s;
        }
        .btn:hover {
            background: #f5f5f5;
            transform: translateY(-3px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.2);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Spring Playground 🌸</h1>
        <p>포트폴리오 프로젝트에 오신 걸 환영합니다.<br>
        로그인 후 다양한 기능을 이용해 보세요!</p>
        <div class="btn-group">
	        <% if (session.getAttribute("username") != null) { %>
	            <span>안녕하세요, <%= session.getAttribute("username") %>님</span>
	            <a href="logout">로그아웃</a>
	        <% } else { %>
	            <a href="login">로그인</a>
	            <a href="register">회원가입</a>
	        <% } %>
        </div>
    </div>
</body>
</html>