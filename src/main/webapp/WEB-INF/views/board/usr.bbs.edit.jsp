<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>글 수정 - Spring Playground</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #333;
            padding: 40px;
        }
        .container {
            max-width: 700px;
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
        input[type="text"], textarea {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 16px;
        }
        textarea {
            height: 200px;
            resize: vertical;
        }
        .btn-group {
            margin-top: 20px;
        }
        .btn {
            display: inline-block;
            padding: 10px 25px;
            border-radius: 25px;
            text-decoration: none;
            font-weight: bold;
            margin-right: 10px;
            color: #fff;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            border: none;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn:hover {
            opacity: 0.85;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>글 수정</h2>
    <form action="/board/update/${post.id}" method="post">
        <input type="text" name="title" value="${post.title}" placeholder="제목을 입력하세요" required>
        <textarea name="content" placeholder="내용을 입력하세요" required>${post.content}</textarea>
        <div class="btn-group">
            <button type="submit" class="btn">저장</button>
            <a href="/board/list" class="btn">목록으로</a>
        </div>
    </form>
</div>
</body>
</html>
