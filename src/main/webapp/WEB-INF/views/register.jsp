<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - Spring Playground</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #ff9a9e, #fad0c4);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .register-box {
            background: #fff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            width: 350px;
        }
        h2 {
            margin-bottom: 20px;
            text-align: center;
            color: #ff6f91;
        }
        .input-group {
            margin-bottom: 15px;
        }
        .input-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }
        .input-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        .btn {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 8px;
            background: #ff6f91;
            color: #fff;
            font-size: 1em;
            font-weight: bold;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn:hover {
            background: #ff4e73;
        }
        .extra {
            margin-top: 15px;
            text-align: center;
            font-size: 0.9em;
        }
        .extra a {
            color: #ff6f91;
            text-decoration: none;
            font-weight: bold;
        }
        .extra a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="register-box">
        <h2>회원가입</h2>
        <form action="register" method="post">
           	<div class="input-group">
                <label for="username">이름</label>
                <input type="text" id="userName" name="userName" required>
            </div>
            <div class="input-group">
                <label for="username">아이디</label>
                <input type="text" id="userId" name="userId" required>
			    <c:if test="${not empty userIdExistsError}">
			        <div style="color:red; font-size:12px;">${userIdExistsError}</div>
			    </c:if>
            </div>
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="input-group">
                <label for="email">이메일</label>
                <input type="email" id="email" name="email" required>
                <c:if test="${not empty emailRegularError}">
        			<div style="color:red; font-size:12px;">${emailRegularError}</div>
    			</c:if>
    			<c:if test="${not empty emailExistsError}">
			        <div style="color:red; font-size:12px;">${emailExistsError}</div>
			    </c:if>
            </div>
            <button type="submit" class="btn">가입하기</button>
        </form>
        <div class="extra">
            <a href="home">홈으로</a> | 이미 계정이 있으신가요? <a href="login">로그인</a>
            
        </div>
    </div>
</body>
</html>