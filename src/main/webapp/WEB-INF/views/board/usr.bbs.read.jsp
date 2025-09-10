<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${post.title} - 게시글</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #333;
            padding: 40px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: #fff;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }
        h2 {
            color: #764ba2;
            margin-bottom: 20px;
        }
        p {
            line-height: 1.6;
        }
        .meta {
            color: #555;
            font-size: 14px;
            margin-bottom: 20px;
        }
        .btn-group a { 
            text-decoration: none; 
            background: #764ba2; 
            color: white; 
            padding: 8px 15px; 
            border-radius: 8px; 
            font-size: 13px; 
            margin-top: 20px; 
        }
        .btn:hover {
            opacity: 0.85;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>${post.title}</h2>
    <p class="meta">
        작성자: ${post.author.userName} | 작성일: ${post.createdAt}
    </p>
    <p>${post.content}</p>

    <div class="btn-group">
		<%-- 
		  SpEL을 사용하여 두 조건을 'or'로 연결합니다.
		  1. hasRole('ADMIN'): 현재 사용자가 ADMIN 권한을 가졌는지 확인
		  2. authentication.principal.user.userId == post.author.userId: 현재 로그인된 사용자(principal)의 ID와 게시글 작성자의 ID가 같은지 확인
		--%>
		<sec:authorize access="hasRole('ADMIN') or authentication.principal.user.userId == '${post.author.userId}'">
		    <a href="/board/edit/${post.id}">수정</a>
		    <a href="/board/delete/${post.id}" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
		</sec:authorize>
        <a class="btn" href="/board/list">목록으로</a>
    </div>
</div>
</body>
</html>
