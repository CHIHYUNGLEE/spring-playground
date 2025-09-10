<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Bootstrap CSS 추가 (내부망이면 직접 다운로드 받아 static에 넣어야 해요) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<div class="container mt-5">
    <h2 class="mb-4 text-center fw-bold">회원 관리</h2>

    <!-- 상태 필터 -->
    <form method="get" action="/user/list" class="mb-3 d-flex justify-content-end">
        <select name="status" class="form-select w-auto" onchange="this.form.submit()">
            <option value="ALL" ${status == 'ALL' ? 'selected' : ''}>전체</option>
            <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>정상 회원</option>
            <option value="INACTIVE" ${status == 'INACTIVE' ? 'selected' : ''}>탈퇴 회원</option>
        </select>
    </form>

    <!-- 회원 목록 테이블 -->
    <div class="table-responsive">
        <table class="table table-bordered table-hover align-middle text-center">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>상태</th>
                    <th>액션</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.userId}</td>
                        <td>${user.userName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.status == 0}">
                                    <span class="badge bg-success">정상</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">탈퇴</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.status == 0}">
                                    <!-- 탈퇴 처리 -->
	                                <form method="post" action="/user/deactivate/${user.id}" style="display:inline;">
	                                    <button type="submit" class="btn btn-sm btn-outline-primary"
	                                        onclick="return confirm('정말 탈퇴 처리하시겠습니까?');">
	                                        탈퇴
	                                    </button>
	                                </form>
                                </c:when>
                                <c:otherwise>
                                    <!-- 복귀 처리 -->
	                                <form method="post" action="/user/activate/${user.id}" style="display:inline;">
	                                    <button type="submit" class="btn btn-sm btn-outline-primary"
	                                        onclick="return confirm('정말 복귀 처리하시겠습니까?');">
	                                        복귀
	                                    </button>
	                                </form>
	                                <!-- 물리적 삭제 -->
		                            <form method="post" action="/user/delete/${user.id}" style="display:inline;">
		                                <button type="submit" class="btn btn-sm btn-outline-danger"
		                                    onclick="return confirm('정말 삭제하시겠습니까?');">
		                                    삭제
		                                </button>
		                            </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
