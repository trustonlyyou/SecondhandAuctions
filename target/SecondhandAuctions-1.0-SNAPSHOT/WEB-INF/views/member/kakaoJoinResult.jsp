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
  <title>카카오 회원가입 결과 | 중고 경매의 세계</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
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
          <h2 class="card-title text-center">카카오 회원가입 결과</h2>
          <hr>
          <br>
          <br>
          <br>
          <br>
          <br>
          <div>
            <h3>${msg}</h3>
          </div>
          <br>
          <br>
          <br>
          <br>
          <br>
          <div>
            <a href="/member/login/list" class="btn btn-primary btn-lg" role="button" aria-pressed="true">로그인</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>


