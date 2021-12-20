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
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="/resources/js/validation.js"></script>

  <title>중고 경매의 세계 | 마이페이지</title>

</head>
<body>
<%@include file="../includes/header.jsp"%>

<br>
<br>
<br>

<div class="container">
  <div class="row">

    <div class="col-lg-3">
      <h2 class="my-4">마이 페이지</h2>
      <div class="list-group list-group-flush">
        <a href="/myPage/form" class="list-group-item">나의 정보</a>
        <a href="/myShop/list" class="list-group-item">나의 판매 정보</a>
        <a href="/myBid/list" class="list-group-item">입찰 물품</a>
        <a href="/myBid/success/sell" class="list-group-item">낙찰 정보</a>
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
            <input type="button" id="modifyPwd" class="btn btn-secondary btn-sm float-right" value="비밀번호 변경">
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
  var pwdFlag = false;

  $(document).ready(function () {


    $("#checkPassword").on("keyup", function () {
      var pwd = $("#checkPassword").val();

      if (pwd == "") {
        $("#pwdCheckMsg").text("비밀번호는 필수 입력입니다.");
        $("#pwdCheckMsg").css('color', 'red');

        pwdFlag = false;

        return false;
      }

      if (isValidationPwd(pwd) === false) {
        $("#pwdCheckMsg").text("8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
        $("#pwdCheckMsg").css('color', 'red');

        pwdFlag = false;

        return false;
      } else {
        $("#pwdCheckMsg").text("확인 버튼을 눌러주세요");
        $("#pwdCheckMsg").css('color', 'green');

        pwdFlag = true;

        return true;
      }
    });

    $("#checkBtn").on("click", function () {
      if (pwdFlag === false) {
        window.alert("비밀번호를 확인해주세요.");
      }
    });


    $("#modifyPwd").on('click', function () {
      window.location.replace("/myPage/certification/password");
    });

    // $.ajax({
    //   url: '/myPage/check',
    //   type: 'get',
    //   data: ''
    // });
  });
</script>

</html>
