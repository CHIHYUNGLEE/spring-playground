<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        /* 배경과 전체 레이아웃 */
        body {
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /* 로그인 박스 */
        .login-box {
            background: white;
            border-radius: 15px;
            padding: 40px 50px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            width: 350px;
            text-align: center;
        }

        .login-box h2 {
            margin-bottom: 30px;
            color: #333;
            font-weight: 600;
        }

        /* 입력 필드 */
        .login-box input[type="text"],
        .login-box input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 14px;
        }

        /* 로그인 버튼 */
        .login-box button {
            width: 100%;
            padding: 12px;
            margin-top: 20px;
            border: none;
            border-radius: 8px;
            background: #6a11cb;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
            font-size: 16px;
            cursor: pointer;
            transition: 0.3s ease;
        }

        .login-box button:hover {
            background: linear-gradient(to right, #2575fc, #6a11cb);
        }

        /* 작은 글씨 링크 */
        .login-box .links {
            margin-top: 15px;
            font-size: 12px;
        }

        .login-box .links a {
            color: #2575fc;
            text-decoration: none;
        }

        .login-box .links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-box">
        <h2>로그인</h2>
        <form action="login" method="post">
            <input type="text" name="username" placeholder="아이디" required>
            <input type="password" name="password" placeholder="비밀번호" required>
            <button type="submit">로그인</button>
        </form>
        <div class="links">
            <a href="#">회원가입</a> | <a href="#">비밀번호 찾기</a>
        </div>
    </div>
</body>
</html>