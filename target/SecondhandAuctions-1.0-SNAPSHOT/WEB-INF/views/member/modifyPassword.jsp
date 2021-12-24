<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/09/03
  Time: 3:46 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>비밀번호 변경 | 중고 경매의 세계</title>
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
          <h2 class="card-title text-center">비밀번호 변경</h2>
          <hr>
          <br>
          <br>
          <br>

          <form class="form-signin" id="phoneForm">

            <div class="input-group input-group-lg">
              <input type="password" id="memberPassword" name="memberPassword" class="form-control"
                     placeholder="비밀번호를 입력해주세요." aria-label="Large"
                     aria-describedby="inputGroup-sizing-sm" maxlength="20">
            </div>
            <div id="pwd1CheckMsg" name="pwd1CheckMsg" class="check_font"></div>

            <br>

            <div class="input-group input-group-lg">
              <input type="password" id="memberPasswordCheck" name="memberPasswordCheck"
                     class="form-control" placeholder="비밀번호를 한번 더 입력해주세요." aria-label="Large"
                     aria-describedby="inputGroup-sizing-sm" maxlength="20">
            </div>
            <div name="pwd2CheckMsg" id="pwd2CheckMsg" class="check_font"></div>

            <br>


            <br>
            <br>

            <div>
              <input type="button" id="modifyPassword" name="modifyPassword"
                     class="btn btn-dark btn-lg btn-block" value="비밀번호 변경" disabled>
            </div>
          </form>
          
          <br>

          <hr>
        </div>
      </div>
    </div>
  </div>
</div>
</body>

<script>
  $(document).ready(function () {
    var passwordFlag = false;

    function checkSpace(str) {
      if (str.search(/\s/) != -1) {
        return true;
      } else {
        return false;
      }
    }

    function isValidationPwd(str) {
      var cnt = 0; // 중복된 숫자 걸러내기
      var isPW = /^[A-Za-z0-9`\-=\\\[\];',\./~!@#\$%\^&\*\(\)_\+|\{\}:"<>\?]{8,16}$/;

      if (str == "") {
        return false;
      }

      var retVal = checkSpace(str);

      if (retVal) {
        return false;
      }

      if (str.length < 8) {
        return false;
      }

      // 중복된 숫자 걸러 내기
      for (var i = 0; i < str.length; ++i) {
        if (str.charAt(0) == str.substring(i, i + 1))
          ++cnt;
      }
      if (cnt == str.length) {
        return false;
      }

      if (!isPW.test(str)) {
        return false;
      }

      return true;
    }

    $("#memberPassword").on('keyup', function () {
      var pwd1 = $("#memberPassword").val();

      if (pwd1 == "") {
        $("#pwd1CheckMsg").text('비밀번호는 필수 입력입니다.');
        $("#pwd1CheckMsg").css('color', 'red');

        passwordFlag = false;

        return false;
      }

      if (isValidationPwd(pwd1) == false) {
        $("#pwd1CheckMsg").text("8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
        $("#pwd1CheckMsg").css('color', 'red');

        passwordFlag = false;

        return false;

      } else {
        $("#pwd1CheckMsg").text("안전한 비밀번호입니다.");
        $("#pwd1CheckMsg").css('color', 'green');

        return true;
      }
    });

    // pwd2CheckMsg
    $("#memberPasswordCheck").on('keyup', function () {
      var pwd1 = $("#memberPassword").val();
      var pwd2 = $("#memberPasswordCheck").val();

      if (pwd2 == "") {
        $("#pwd2CheckMsg").text('비밀번호는 필수 입력입니다.');
        $("#pwd2CheckMsg").css('color', 'red');

        passwordFlag = false;

        return false;
      }

      if (pwd1 !== pwd2) {
        $("#pwd2CheckMsg").text("비밀번호가 일치하지 않습니다.");
        $("#pwd2CheckMsg").css('color', 'red');

        passwordFlag = false;

        return false;
      } else {
        $("#pwd2CheckMsg").text("비밀번호가 일치합니다.");
        $("#pwd2CheckMsg").css('color', 'green');

        passwordFlag = true;

        if (passwordFlag === true) {
          $("#modifyPassword").attr('disabled', false);
        }

        return true;
      }
    });
  });

  $("#modifyPassword").on("click", function () {
    $("#modifyPassword").attr('disabled', false);

    $.ajax({
      url: '/member/modify/password',
      type: 'post',
      data: $("form").serialize(),

      success: function (data) {
        var check = data.check;

        switch (check) {
          case 0 :
            window.alert("다시 시도해 주세요.");
            window.location.replace("/member/search/password");
            break;
          case 1 :
            window.location.replace("/member/modify/password/result");
            break;
        }
      }
    });
  });

</script>

</html>
