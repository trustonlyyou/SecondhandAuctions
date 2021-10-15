<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/10/13
  Time: 12:53 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

  <title>중고 경매의 세계 | 마이페이지</title>

  <!-- Bootstrap core CSS -->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="css/shop-homepage.css" rel="stylesheet">
</head>
<body>
<header>

  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="/">중고 경매의 세계</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
          <form class="d-flex">
            <input class="form-control me-2" type="search" placeholder="상품을 검색해 보세요." aria-label="Search">
            <button class="btn btn-outline-light" type="submit">검색</button>
          </form>
        </div>
      </nav>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
            <a class="nav-link" href="/">Home
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <c:if test="${sessionScope.member == null}">
            <li class="nav-item">
              <a class="nav-link" href="member/login/form">로그인</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="member/join/form">회원가입</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">카테고리</a>
            </li>
          </c:if>
          <c:if test="${sessionScope.member != null}">
            <li class="nav-item">
              <a class="nav-link" href="member/login/form">${sessionScope.member}</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/member/logout/action">로그아웃</a>
            </li>
            <%--                            <li class="nav-item">--%>
            <%--                                <a class="nav-link" href="#">카테고리</a>--%>
            <%--                            </li>--%>
          </c:if>
        </ul>
      </div>
    </div>
  </nav>
</header>
<br>
<br>
<br>

<div class="container">
  <div class="row">
    <div class="col-lg-3">

      <h2 class="my-4">마이 페이지</h2>
      <div class="list-group list-group-flush">
        <a href="#" class="list-group-item">나의 정보</a>
        <a href="#" class="list-group-item">입찰 물품</a>
        <a href="#" class="list-group-item">배송</a>
      </div>
    </div>

    <div class="col-lg-9">
      <h4 class="my-4">회원 기본정보</h4>
      <hr style="border: solid 1px;">
      <form>
        <div class="form-group row">
          <label for="memberId" class="col-sm-2 col-form-label">아이디</label>
          <div class="col-sm-10">
            <input type="text" readonly class="form-control-plaintext" id="memberId" value="${memberInfo.memberId}">
          </div>
        </div>
        <hr>
        <div class="form-group row">
          <label for="memberPassword" class="col-sm-2 col-form-label">비밀번호</label>
          <div class="col-sm-10">
            <input type="text" readonly class="form-control-plaintext" id="memberPassword" value="${memberInfo.memberPassword}">
          </div>
        </div>
        <hr>
        <div class="form-group row">
          <label for="memberName" class="col-sm-2 col-form-label">이름</label>
          <div class="col-sm-10">
            <input type="text" readonly class="form-control-plaintext" id="memberName" value="${memberInfo.memberName}">
          </div>
        </div>
        <hr>
        <div class="form-group row">
          <label for="memberEmail" class="col-sm-2 col-form-label">이메일</label>
          <div class="col-sm-10">
            <input type="text" readonly class="form-control-plaintext" id="memberEmail" value="${memberInfo.memberEmail}">
          </div>
        </div>
        <hr>
        <div class="form-group row">
          <label for="memberPhone" class="col-sm-2 col-form-label">핸드폰</label>
          <div class="col-sm-10">
            <input type="text" readonly class="form-control-plaintext" id="memberPhone" value="${memberInfo.memberPhone}">
          </div>
        </div>
        <hr>
      </form>
    </div>

  </div>
  <!-- .row -->
</div>
</body>

<script type="text/javascript">






</script>

</html>
