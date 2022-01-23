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
    </div>

    <div class="col-lg-9">
      <h4 class="my-4">회원 인증</h4>
      <hr style="border: solid 1px;">
      <br>
      <br>
      <br>
      <form>
        <c:if test="${chk eq false}">
          <div class="form-group row">
            <div class="col-sm-10 text-center">
              <h6>마이페이지 이용시 인증이 필요합니다.</h6>
              <label for="memberPassword">비밀번호 &nbsp;</label>
              <input type="password" id="memberPassword" placeholder="비밀번호를 입렵해주세요.">
              <input id="checkSubmit" type="button" value="확인" class="btn btn-primary btn-sm">
            </div>
          </div>
        </c:if>
        <c:if test="${chk eq true}">
          <h3>카카오 인증</h3>
          <div class="form-group row">
            <div class="col-sm-10 text-center">
              <h6>마이페이지 이용시 인증이 필요합니다.</h6>
              <label for="memberPhone"></label>
              <input type="text" id="memberPhone" placeholder="핸드폰 번호를 입력해주세요.">
              <input id="phoneCheck" class="btn btn-primary btn-sm" type="button" value="전송">
            </div>

            <div class="col-sm-10 text-center" id="phoneMsg"></div>

            <div class="phoneInputCheckDiv col-sm-10 text-center">
              <input type="text" id="inputPhoneCheck" placeholder="인증번호를 입력해주세요.">
              <input type="button" class="btn btn-primary btn-sm" id="inputPhoneBtn" value="확인">
            </div>
          </div>
        </c:if>
      </form>
    </div>
  </div>
  <!-- .row -->
</div>
</body>
<script type="text/javascript">

  $(document).ready(function () {
    $(".phoneInputCheckDiv").hide();

    // controller 는 타지만 ajax success:function 부터 안탄다.
    $("#memberPassword").on('keydown', function (e) {
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
        url: '/authority',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: memberPassword,

        success: function (data) {
          var chk = data.chk;

          if (chk == true) {
            window.location.replace("/myPage/form");
          } else {
            window.alert("비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
            $("#memberPassword").val("");
          }
        },
        error: function (request,status,error) {
          console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          alert("다시 시도해주세요.");
          location.reload();
        }
      });
    });

    $("#memberPhone").on('keyup', function () {
        var phone = $("#memberPhone").val();
        var isPhone = /^((01[1|6|7|8|9])[1-9][0-9]{6,7})$|(010[1-9][0-9]{7})$/;

        if (phone == "") {
          $("#phoneMsg").text("핸드폰은 필수 입력입니다.");
          $("#phoneMsg").css('color','red');
          $("#phoneCheck").attr('disabled', true);

          return false;
        }

        if (!isPhone.test(phone)) {
          $("#phoneMsg").text("핸드폰 입력형식이 옳바르지 않습니다.");
          $("#phoneMsg").css('color','red');
          $("#phoneCheck").attr('disabled', true);

          phoneFlag = false;

          return false;
        } else {
          $("#phoneMsg").text("핸드폰 인증을 해주세요.");
          $("#phoneMsg").css('color','green');
          $("#phoneCheck").attr('disabled', false);

          return true;
        }
    });

    $("#phoneCheck").on('click', function () {
        var memberPhone = $("#memberPhone").val();
        $(".phoneInputCheckDiv").show();

        $.ajax({
          url: '/authority/sendSms',
          type: 'post',
          data: memberPhone,
          dataType: 'json',
          contentType: 'application/json; charset=UTF-8',

          success: function (data) {
            var chk = data.chk;
            var key = data.key;

            if (chk === true) {
              $("#inputPhoneBtn").on('click', function () {
                var inputKey = $("#inputPhoneCheck").val();

                if (inputKey === key) {
                  $("#phoneMsg").text("인증번호가 일치합니다.");
                  $("#phoneMsg").css('color','green');
                  window.location.replace("/authority/kakao");
                } else {
                  $("#phoneMsg").text("인증번호가 일치하지 않습니다.");
                  $("#phoneMsg").css('color','red');
                }
              });

            } else {
              alert("핸드폰 번호를 다시 인증해주세요.");
              location.reload();
            }

          },
          error: function (request,status,error) {
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("다시 시도해주세요.");
            location.reload();

          }
        });
    });

  });
</script>
</html>
