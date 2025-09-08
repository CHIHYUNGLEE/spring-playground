<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 재설정</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f7f9fc;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .reset-container {
            background: #fff;
            padding: 30px;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            width: 350px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #444;
        }
        .input-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            font-size: 14px;
            margin-bottom: 6px;
            color: #555;
        }
        input[type="password"] {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ccc;
        }
        .btn {
            width: 100%;
            padding: 12px;
            background: #4a90e2;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            transition: background 0.3s;
        }
        .btn:hover {
            background: #357abd;
        }
        .message {
            margin-top: 15px;
            text-align: center;
            font-size: 13px;
            color: #e74c3c;
        }
    </style>
    <script>
        function validateForm() {
            const newPassword = document.getElementById("newPassword").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            if (newPassword !== confirmPassword) {
                alert("비밀번호가 일치하지 않습니다!");
                return false; // 제출 막기
            }
            return true; // 정상 제출
        }
    </script>
</head>
<body>
    <div class="reset-container">
        <h2>비밀번호 재설정</h2>
        <form action="${pageContext.request.contextPath}/resetPassword" method="post" onsubmit="return validateForm()">
            <!-- 토큰을 숨겨서 같이 전송 -->
            <input type="hidden" name="token" value="${token}">

            <div class="input-group">
                <label for="newPassword">새 비밀번호</label>
                <input type="password" id="newPassword" name="newPassword" required>
            </div>

            <div class="input-group">
                <label for="confirmPassword">새 비밀번호 확인</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>

            <button type="submit" class="btn">비밀번호 변경</button>

            <div class="message">
                <c:if test="${not empty error}">
                    <p>${error}</p>
                </c:if>
                <c:if test="${not empty message}">
                    <p>${message}</p>
                </c:if>
            </div>
        </form>
    </div>
</body>
</html>
