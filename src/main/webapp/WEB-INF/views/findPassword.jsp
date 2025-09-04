<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기 - Spring Playground</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #89f7fe, #66a6ff);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .find-box {
            background: #fff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            width: 350px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #1e3c72;
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
            background: #1e3c72;
            color: #fff;
            font-size: 1em;
            font-weight: bold;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn:hover {
            background: #162a50;
        }
        .extra {
            margin-top: 15px;
            text-align: center;
            font-size: 0.9em;
        }
        .extra a {
            color: #1e3c72;
            text-decoration: none;
            font-weight: bold;
        }
        .extra a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="find-box">
        <h2>비밀번호 찾기</h2>
        <form action="findPassword" method="post">
            <div class="input-group">
                <label for="username">아이디</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="input-group">
                <label for="email">가입 이메일</label>
                <input type="email" id="email" name="email" required>
            </div>
            <button type="submit" class="btn">비밀번호 재설정 링크 보내기</button>
        </form>
        <div class="extra">
            <a href="login">로그인으로 돌아가기</a>
        </div>
    </div>
</body>
</html>