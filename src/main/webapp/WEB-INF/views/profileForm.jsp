<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원정보 변경</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f7f9fc; display: flex; justify-content: center; align-items: center; height: 100vh; }
        .form-container { background: #fff; padding: 30px; border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 350px; }
        h2 { text-align: center; margin-bottom: 20px; color: #444; }
        .input-group { margin-bottom: 15px; }
        label { display: block; font-size: 14px; margin-bottom: 6px; color: #555; }
        input[type="text"], input[type="email"] { width: 100%; padding: 10px; border-radius: 8px; border: 1px solid #ccc; }
        .btn { width: 100%; padding: 12px; background: #4a90e2; color: white; border: none; border-radius: 8px; cursor: pointer; font-size: 15px; transition: background 0.3s; }
        .btn:hover { background: #357abd; }
        .message { margin-top: 15px; text-align: center; font-size: 13px; color: #27ae60; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>회원정보 변경</h2>
        <form action="${pageContext.request.contextPath}/profile" method="post">
            <div class="input-group">
                <label for="name">이름</label>
                <input type="text" id="userName" name="userName" value="${user.userName}" required>
            </div>
            <div class="input-group">
                <label for="email">이메일</label>
                <input type="email" id="email" name="email" value="${user.email}" required>
            </div>
            <button type="submit" class="btn">변경하기</button>
        </form>

        <div class="message">
            <c:if test="${not empty message}">
                <p>${message}</p>
            </c:if>
        </div>
    </div>
</body>
</html>
