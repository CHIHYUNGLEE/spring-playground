<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Home - Spring Playground</title>
    <style>
        body { font-family:'Poppins', sans-serif; display:flex; align-items:center; justify-content:center; height:100vh; background:linear-gradient(135deg, #667eea, #764ba2); color:#fff;}
        .container { text-align:center;}
        .btn-group {margin:30px;}
        .btn { background:white; color:#764ba2; padding:12px 30px; border-radius:30px; text-decoration:none; font-weight:bold; margin:0 10px;}
        .welcome { font-size: 25px; }
    </style>
    <script>
	    var msg = '${updateMsg}';
	    if (msg) {
	        alert(msg);
	    }
	</script>
</head>
<body>
<div class="container">
	
    <h1>Welcome to Spring Playground 🌸</h1>
    <p>포트폴리오 프로젝트에 오신 걸 환영합니다.
	    <sec:authorize access="!isAuthenticated()">
	    <br>로그인 후 다양한 기능을 이용해 보세요!
	    </sec:authorize>
    </p>
    <div class="btn-group">
		
	    <sec:authorize access="isAuthenticated()">
	        <span class="welcome">안녕하세요, <sec:authentication property="name" />님</span>
	        <a class="btn" href="board/list">게시판</a>
	        <a class="btn" href="${pageContext.request.contextPath}/profile">회원정보 변경</a>
	        <a class="btn" href="logout">로그아웃</a>
	    </sec:authorize>
	
	    <sec:authorize access="!isAuthenticated()">
	        <a class="btn" href="login">로그인</a>
	        <a class="btn" href="register">회원가입</a>
	    </sec:authorize>
   		<sec:authorize access="hasRole('ADMIN')">
		    <a href="/user/list">회원관리</a>
		</sec:authorize>
    </div>
</div>
</body>
</html>
