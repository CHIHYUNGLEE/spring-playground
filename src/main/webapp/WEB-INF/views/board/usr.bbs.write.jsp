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
        .btn {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff; padding: 10px 25px;
            border: none; border-radius: 6px;
            cursor: pointer; font-weight: bold;
        }
        .btn:hover { opacity: 0.9; }
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
        <button type="submit" class="btn">등록하기</button>
    </form>
</div>
</body>
</html>
