<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; display:flex; justify-content:center; align-items:center; height:100vh; background:linear-gradient(to right, #6a11cb, #2575fc);}
        .login-box { background:white; padding:40px; border-radius:15px; box-shadow:0 10px 25px rgba(0,0,0,0.2); width:350px; text-align:center;}
        .login-box input { width:100%; padding:12px; margin:10px 0; border-radius:8px; border:1px solid #ccc;}
        .login-box button { width:100%; padding:12px; margin-top:20px; border:none; border-radius:8px; background:linear-gradient(to right, #6a11cb, #2575fc); color:white; cursor:pointer;}
        .login-box .links { margin-top:15px; font-size:12px;}
        .login-box .links a { color:#2575fc; text-decoration:none;}
        .error-msg { color:red; margin-top:15px; font-size:15px; }
    </style>
</head>
<body>
<div class="login-box">
    <h2>로그인</h2>
    <form action="doLogin" method="post">
        <input type="text" name="userId" placeholder="아이디" required>
        <input type="password" name="password" placeholder="비밀번호" required>
        <button type="submit">로그인</button>
    </form>

	<c:if test="${not empty param.error}">
	    <p style="color:red;">아이디 또는 비밀번호가 틀렸습니다.</p>
	</c:if>

	<!-- 회원가입후 로그인 요청 -->
	<c:if test="${not empty registerSuccessMsg}">
	    <script>alert('${registerSuccessMsg}');</script>
	</c:if>
	
	<!-- 회원틸퇴후 로그인창 이동 -->
	<c:if test="${not empty withdrawMsg}">
	    <script>alert('${withdrawMsg}');</script>
	</c:if>
	
    <div class="links">
        <a href="home">홈으로</a> | <a href="register">회원가입</a> | <a href="findPassword">비밀번호 찾기</a>
    </div>
</div>
</body>
</html>
