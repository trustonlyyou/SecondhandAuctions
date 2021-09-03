<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/21
  Time: 2:35 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>아이디 찾기 결과 | 중고 경매의 세계</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-lg-10 col-xl-7 mx-auto">
      <div class="card card-signin flex-row my-5">
        <div class="card-body text-center">
          <div class="form-row float-left">
            <a href="/">메인 페이지</a>
          </div>
          <br>
          <br>
          <h2 class="card-title text-center">아이디 찾기 결과</h2>
          <hr>
          <br>
          <br>
          <br>
          <br>
          <br>

          <c:if test="${(''.equals(memberId))}">
            <div>
              <h3>가입된 회원의 정보가 없습니다.<br><br> <a href="/member/join/form">회원가입</a> </h3>
            </div>

          </c:if>

          <c:if test="${!''.equals(memberId)}">
            <div>
              <h3>회원님의 가입된 아이디 입니다.<br><br> ${memberId}</h3>
            </div>
          </c:if>

          <br>
          <br>
          <br>
          <br>
          <br>

          <div>
            <a href="#">비밀번호가 기억나지 않으세요?</a>
          </div>

          <br>

          <div>
            <button class="btn btn-primary btn-lg" value="로그인">로그인</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>

