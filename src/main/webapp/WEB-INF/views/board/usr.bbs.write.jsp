<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #f5f6fa;
            margin: 0; padding: 0;
        }
        .container {
            width: 600px;
            margin: 50px auto;
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h2 { margin-bottom: 20px; color: #764ba2; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-weight: bold; margin-bottom: 6px; }
        input[type="text"], textarea {
            width: 100%; padding: 10px;
            border: 1px solid #ccc; border-radius: 6px;
        }
        textarea { resize: vertical; height: 200px; }
        
		/* 버튼 공통 스타일 */
		.btn {
		    display: inline-flex;             /* inline-flex로 맞춤 */
		    align-items: center;
		    justify-content: center;
		    width: 120px;                     /* 너비 고정 */
		    height: 45px;                     /* 높이 고정 */
		    padding: 0;                        /* padding 제거, width/height에 맞춤 */
		    border-radius: 6px;
		    font-weight: bold;
		    font-size: 1em;
		    text-decoration: none;            /* a태그도 버튼처럼 */
		    box-sizing: border-box;           /* 테두리 포함 크기 계산 */
		    cursor: pointer;
		    margin-right: 10px;
		}

        /* 등록하기 버튼 스타일 */
        .btn-submit {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff;
            border: none;
        }
        .btn-submit:hover { opacity: 0.9; }

        /* 취소 버튼 스타일 */
        .btn-cancel {
            background: #fff;
            color: #000;
            border: 3px solid #764ba2;
            text-decoration: none;
        }
        .btn-cancel:hover {
            background: #f0f0f0;
        }

        /* 버튼 그룹 */
        .button-group {
            display: flex;
            justify-content: flex-start;
            align-items: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>게시글 작성 ✍️</h2>
    <form action="save" method="post">
        <div class="form-group">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" required>
        </div>
        <div class="form-group">
            <label for="content">내용</label>
            <textarea id="content" name="content" required></textarea>
        </div>
        <div class="button-group">
            <button type="submit" class="btn btn-submit">등록하기</button>
            <a href="list" class="btn btn-cancel">취소</a>
        </div>
    </form>
</div>
</body>
</html>
