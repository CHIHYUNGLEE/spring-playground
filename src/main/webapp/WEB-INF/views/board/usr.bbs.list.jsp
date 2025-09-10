<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <style>
        body { 
            font-family:'Poppins', sans-serif; 
            background: linear-gradient(135deg, #667eea, #764ba2); 
            color: #333; 
            margin:0; 
            padding:0; 
        }
        .container { 
            width: 90%; 
            max-width: 900px; 
            margin: 50px auto; 
            background: #fff; 
            padding: 20px 30px; 
            border-radius: 15px; 
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }
        h2 { 
            text-align: center; 
            margin-bottom: 30px; 
            color: #764ba2;
        }
        .post-card {
            border: 1px solid #eee; 
            border-radius: 10px; 
            padding: 15px 20px; 
            margin-bottom: 15px; 
            display: flex; 
            justify-content: space-between; 
            align-items: center;
            transition: box-shadow 0.3s;
        }
        .post-card:hover { 
            box-shadow: 0 5px 15px rgba(0,0,0,0.2); 
        }
        .post-info { 
            display: flex; 
            flex-direction: column; 
        }
        .post-title { 
            font-size: 18px; 
            font-weight: bold; 
            color: #333; 
            text-decoration: none;
        }
        .post-meta { 
            font-size: 12px; 
            color: #777; 
            margin-top: 5px;
        }
        .btn-group a { 
            text-decoration: none; 
            background: #764ba2; 
            color: white; 
            padding: 8px 15px; 
            border-radius: 8px; 
            font-size: 13px; 
            margin-left: 5px; 
        }
        .btn-group a:hover {
            background: #6a11cb;
        }
        .new-post, .btn_home { 
            display: block; 
            width: 150px; 
            margin: 20px auto; 
            text-align: center; 
            background: #2575fc; 
            color: #fff; 
            padding: 10px 0; 
            border-radius: 10px; 
            text-decoration: none; 
            font-weight: bold;
        }
        .new-post:hover .btn_home:hover { 
            background: #6a11cb; 
        }
    </style>
</head>
<body>
<div class="container">
    <h2>게시판 목록</h2>

    <c:forEach var="post" items="${posts}">
        <div class="post-card">
            <div class="post-info">
                <a class="post-title" href="/board/${post.id}">${post.title}</a>
                <span class="post-meta">작성자: ${post.author.userName} | 작성일: ${post.createdAt}</span>
            </div>
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
            </div>
        </div>
    </c:forEach>

    <sec:authorize access="isAuthenticated()">
        <a class="new-post" href="/board/new">새 글 쓰기</a>
    </sec:authorize>
    <a href="home" class="btn btn_home">홈으로</a>
</div>
</body>
</html>
