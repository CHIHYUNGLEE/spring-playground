<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        
        /* 댓글 전체 컨테이너 */
		#comments {
		    margin-top: 30px;
		}
		
		/* 댓글 박스 */
		.comment {
		    border: 1px solid #ddd;
		    border-radius: 10px;
		    padding: 12px 15px;
		    margin-bottom: 15px;
		    background-color: #f9f9f9;
		    transition: box-shadow 0.2s;
		}
		.comment:hover {
		    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
		}
		
		/* 댓글 작성자 및 날짜 */
		.comment b {
		    color: #764ba2;
		    font-weight: 600;
		}
		.comment .meta {
		    font-size: 12px;
		    color: #555;
		    margin-bottom: 8px;
		}
		
		.createdDt{
			font-size: 14px;
		}
		
		/* 대댓글도 들여쓰기 없이 부모와 같은 위치 */
		.reply {
		    border-left: 3px solid #764ba2; /* 원하면 선만 남김 */
		    padding-left: 8px; /* 약간 여백만 */
		    margin-left: 0; /* 들여쓰기 제거 */
		    background-color: #f1f1f1;
		    border-radius: 8px;
		}
		
		/* 댓글/대댓글 버튼 스타일 */
		.comment form button,
		.reply form button {
		    margin-top: 5px;
		}
		
		textarea {
		    width: 100%;
		    border-radius: 8px;
		    border: 1px solid #ccc;
		    padding: 8px;
		    resize: vertical;
		    min-height: 50px;
		}
		
		/* 댓글 작성폼 */
		form.mt-4 {
		    margin-top: 60px;
		}
		
		/* 답글 폼 */
		form.mt-2.ms-3 {
		    margin-top: 10px;
		    margin-left: 30px;
		}
		
		/* 버튼 공통 */
		.btn-sm {
			color: white;
		    font-size: 12px;
		    padding: 4px 8px;
		}
		.btn-primary {
		    background-color: #764ba2;
		    border: none;
		}
		.btn-secondary {
		    background-color: #667eea;
		    border: none;
		}
		.btn-danger {
		    background-color: #e74c3c;
		    border: none;
		}
		.btn-primary:hover,
		.btn-secondary:hover,
		.btn-danger:hover {
		    opacity: 0.85;
		}
		
		/* 반응형 */
		@media screen and (max-width: 600px) {
		    .reply {
		        margin-left: 15px;
		        padding-left: 10px;
		    }
		}
    </style>
    <script>
		// 댓글창 컨텐츠 없을시 alert 띄움
    	function checkComment(form) {
		    const content = form.querySelector('textarea[name="content"]').value.trim();
		    if (!content) {
		        alert('댓글 내용을 입력해주세요!');
		        return false; // submit 막기
		    }
		    return true;
		}
		
		// 대댓글 작성버튼 토글용
		function toggleReplyForm(commentId) {
		    const form = document.getElementById("reply-form-" + commentId);
		    form.style.display = (form.style.display === "none") ? "block" : "none";
		}
		
		//댓글 수정용 토글함수
		function showEditForm(commentId, content) {
		    // 보기모드 숨기고 수정모드 보여주기
		    document.getElementById("view-mode-" + commentId).style.display = "none";
		    document.getElementById("edit-mode-" + commentId).style.display = "block";

		    // textarea에 기존 댓글 내용 세팅
		    document.getElementById("edit-textarea-" + commentId).value = content;
		}

		//수정모드 취소후 다시 댓글로 돌아오는 함수
		function cancelEdit(commentId) {
		    // 수정모드 숨기고 다시 보기모드 보여주기
		    document.getElementById("edit-mode-" + commentId).style.display = "none";
		    document.getElementById("view-mode-" + commentId).style.display = "block";
		}
	</script>
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
		
	    <!-- 댓글 작성 -->
		<form action="/comments/add" method="post" class="mt-4">
		    <input type="hidden" name="postId" value="${post.id}">
		    <textarea name="content" class="form-control mb-2" placeholder="댓글을 입력하세요"></textarea>
		    <button type="submit" class="btn btn-primary btn-sm">댓글 등록</button>
   			<c:if test="${not empty errorReplyMessage}">
			    <div style="color:red; margin-bottom:10px;">
			        ${errorReplyMessage}
			    </div>
			</c:if>
		</form>
			
		<!-- 댓글 목록 -->
		<%@ include file="/WEB-INF/views/board/usr.comments.jspf" %>
	</div>
	<c:if test="${not empty param.focusId}">
		<script>
			document.addEventListener("DOMContentLoaded", function() {
			    setTimeout(function() {
			        const target = document.getElementById("view-mode-${param.focusId}");
			        if (target) {
			            target.scrollIntoView({ behavior: "smooth", block: "center" });
			        }
			    }, 100); // 0.1초 딜레이
			});
		</script>
	</c:if>
</body>
</html>
