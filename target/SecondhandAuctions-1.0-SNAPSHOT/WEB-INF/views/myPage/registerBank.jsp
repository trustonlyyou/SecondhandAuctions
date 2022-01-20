<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/11
  Time: 1:01 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>계좌 등록 | 중고 경매의 세계</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
<%@include file="../includes/header.jsp"%>


<div class="container">
  <div class="row">
    <div class="col-lg-10 col-xl-7 mx-auto">
      <div class="card card-signin flex-row my-5">
        <div class="card-body text-center">
          <h2 class="card-title text-center">계좌등록</h2>
          <hr>
          <br>

          <form class="form-signin" id="account_info">

            <div class="input-group input-group-md">
              <input type="text" class="form-control" id="memberName" name="memberName" placeholder="이름을 입력해주세요." aria-label="Large"
                     aria-describedby="inputGroup-sizing-sm">
            </div>
            <div name="nameCheckMsg" id="nameCheckMsg" class="check_font"></div>
            <br>
            <div class="input-group input-group-md">
              <input type="text" class="form-control" id="memberBirth" name="memberBirth" maxlength="6" placeholder="생년월일6자리를 입력해주세요." aria-label="Large"
                     aria-describedby="inputGroup-sizing-sm">
            </div>
            <div name="birthCheckMsg" id="birthCheckMsg" class="check_font"></div>
            <br>
<%--            <div class="input-group input-group-md">--%>
<%--              <select class="form-select" id="bank_code" name="bank_code" required>--%>
<%--                <option selected>은행 선택</option>--%>
<%--                <option value="088">신한</option>--%>
<%--                <option value="097">오픈</option>--%>
<%--              </select>--%>
<%--            </div>--%>
            <div class="input-group input-group-md">
              <div class="input-group-prepend">
                <label class="input-group-text" for="bank_code">은행선택</label>
              </div>
              <select class="custom-select" id="bank_code" name="bank_code">
                <option value="">선택되지 않음</option>
                <option value="088">신한</option>
                <option value="097">오픈</option>
              </select>
            </div>

            <br>
            <div class="input-group input-group-md">
              <input type="text" class="form-control" id="account_num" name="account_num" placeholder="계좌번호를 입력해주세요." aria-label="Large"
                     aria-describedby="inputGroup-sizing-sm">
            </div>
            <div name="accountCheckMsg" id="accountCheckMsg" class="check_font"></div>
            <br>
            <br>

            <div>
              <input type="button" id="accountSubmit" class="btn btn-dark btn-block" value="계좌등록" disabled>
            </div>

            <br>
            <hr>

            <br>

          </form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
<script>
  var nameFlag;
  var birthFlag;
  var accountFlag;

  $("#memberName").on('keyup', function () {
    var memberName = $("#memberName").val();
    var isName = /^[가-힣]+$/;

    if (memberName == "") {
      $("#nameCheckMsg").text("이름은 필수입력 입니다.");
      $("#nameCheckMsg").css('color', 'red');

      nameFlag = false;

      return false;
    }

    if (!isName.test(memberName)) {
      $("#nameCheckMsg").text("한글만 입력 가능합니다.");
      $("#nameCheckMsg").css('color', 'red');

      nameFlag = false;

      return false;
    } else {
      $("#nameCheckMsg").text("");

      nameFlag = true;

      if ((nameFlag && birthFlag && accountFlag) == true) {
        $("#accountSubmit").attr('disabled', false);

        return true;
      }
    }
  });

  $("#memberBirth").on('keyup', function () {
    var memberBirth = $("#memberBirth").val();
    var isBirth = /[0-9]{6}$/;

    if (memberBirth == "") {
      $("#birthCheckMsg").text("생년월일은 필수 입력입니다.");
      $("#birthCheckMsg").css('color','red');

      birthFlag = false;
    }

    if (!isBirth.test(memberBirth)) {
      $("#birthCheckMsg").text("숫자만 입력 가능하고 6자리를 입력하셔야 합니다.");
      $("#birthCheckMsg").css('color','red');

      birthFlag = false;

      return false;
    } else {
      $("#birthCheckMsg").text("");

      birthFlag = true;

      if ((nameFlag && birthFlag && accountFlag) == true) {
        $("#accountSubmit").attr('disabled', false);

        return true;
      }
    }

  });

  $("#account_num").on('keyup', function () {
    var account_num = $("#account_num").val();
    var isAccount = /[0-9]/;

    if (account_num == "") {
      $("#accountCheckMsg").text("계좌번호는 필수 입력입니다.");
      $("#accountCheckMsg").css('color','red');

      accountFlag = false;

      return false;
    }

    if (!isAccount.test(account_num)) {
      $("#accountCheckMsg").text("숫자만 입력가능합니다.");
      $("#accountCheckMsg").css('color','red');
    } else {
      $("#accountCheckMsg").text("");

      accountFlag = true;

      if ((nameFlag && birthFlag && accountFlag) == true) {
        $("#accountSubmit").attr('disabled', false);

        return true;
      }
    }
  });

  $("#accountSubmit").on('click', function () {
    var info = $("#account_info").serialize();
    alert(info);
    console.log(info)

    $.ajax({
      url: '/real/name/callback',
      type: 'post',
      data: info,

      success: function (data) {
          var result = data.result;

          if (result == true) {
              alert("계좌등록이 완료 되었습니다. 환불을 진행해 주세요.");
          } else {
              alert("계좌등록에 실패 하셨습니다. code :: " + data.code + " message :: " + data.message);
          }

          window.location.replace('/myPage/myPoint');
      }
    });
  });

</script>
</html>
