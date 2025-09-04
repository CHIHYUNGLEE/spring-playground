<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error - Spring Playground</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #f8d7da;
            color: #721c24;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .box {
            background: #fff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            text-align: center;
        }
        h1 {
            margin-bottom: 20px;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background: #721c24;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background: #501217;
        }
    </style>
</head>
<body>
    <div class="box">
        <h1>⚠️ 오류가 발생했습니다</h1>
        <p>요청하신 페이지를 찾을 수 없습니다.<br>잠시 후 다시 시도해주세요.</p>
        <a href="/home">홈으로 돌아가기</a>
    </div>
</body>
</html>
