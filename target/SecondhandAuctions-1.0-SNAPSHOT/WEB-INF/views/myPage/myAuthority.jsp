<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/10/13
  Time: 12:53 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

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
      <%@include file="../includes/myPageCategory.jsp"%>
<%--      <div class="list-group list-group-flush">--%>
<%--        <a href="/myPage" class="list-group-item">나의 정보</a>--%>
<%--        <a href="/myShop/list" class="list-group-item">나의 판매 정보</a>--%>
<%--        <a href="/myBid/list" class="list-group-item">입찰 물품</a>--%>
<%--        <a href="/myBid/success/sell" class="list-group-item">낙찰 정보</a>--%>
<%--      </div>--%>
    </div>

    <div class="col-lg-9">
      <h4 class="my-4">회원 인증</h4>
      <hr style="border: solid 1px;">
      <br>
      <br>
      <br>
      <form>
        <div class="form-group row">
          <div class="col-sm-10 text-center">
            <h6>마이페이지 이용시 인증이 필요합니다.</h6>
            <label for="memberPassword">비밀번호 &nbsp;</label>
            <input type="password" id="memberPassword" placeholder="비밀번호를 입렵해주세요.">
            <input id="checkSubmit" type="button" value="확인" class="btn btn-primary btn-sm">
          </div>
        </div>
      </form>
    </div>
  </div>
  <!-- .row -->
</div>
</body>
<script type="text/javascript">

  $(document).ready(function () {

    // controller 는 타지만 ajax success:function 부터 안탄다.
    $("#memberPassword").on('keypress', function (e) {
      if (e.keyCode === 13) {
        $("#checkSubmit").click();
      }
    });

    $("#checkSubmit").on('click', function () {
      var memberPassword = $("#memberPassword").val();

      if (memberPassword === "") {
        alert("비밀번호를 입력해주세요.");

        return false;
      }

      $.ajax({
        url: '/myPage/authority',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: memberPassword,

        success: function (data) {
          // todo :: keycode 사용시 ajax 밑에 내용이 인식이 안된다.
          var check = data.check;

          if (check === 1) {
            window.location.replace("/myPage/form");
          } else {
            window.alert("비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
            $("#memberPassword").val("");
          }
        },
        error: function (request,status,error) {
          console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          alert("다시 시도해주세요.");
          window.location.href("/myPage/authority/form");
        }
      });
    });

  });
</script>
</html>
