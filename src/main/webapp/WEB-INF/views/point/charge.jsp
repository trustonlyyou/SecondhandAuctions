<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/21
  Time: 11:44 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>포인트 결제 | 중고 경매의 세계</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
  <script src="https://js.tosspayments.com/v1"></script>
</head>

<body>
<%@include file="../includes/header.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-lg-10 col-xl-7 mx-auto">
      <div class="card card-signin flex-row my-5">
        <div class="card-body text-center">
          <h2 class="card-title text-center">포인트 충전</h2>
          <hr>
          <br>

          <form class="form-signin">
            <select class="custom-select my-1 mr-sm-2" id="select_amount" name="select_amount">
              <option value="" selected>결제 금액 선택</option>
              <option value="10000">10,000</option>
              <option value="20000">20,000</option>
              <option value="50000">50,000</option>
              <option value="100000">100,000</option>
              <option value="200000">200,000</option>
              <option value="300000">300,000</option>
              <option value="500000">500,000</option>
              <option value="1000000">1,000,000</option>
            </select>
            <hr>
            <div class="text-right">
              <h5>결재 금액 / 충전 포인트</h5><br>
              <div>
                <span id="amount">0</span>&nbsp; / <span id="point">0</span>
              </div>
            </div>
            <hr>

            <input class="btn btn-primary btn-md" id="card" type="button" value="카드결제">
            <input class="btn btn-primary btn-md" id="account_transfer" type="button" value="계좌이체">
            <input class="btn btn-primary btn-md" id="virtual_account" type="button" value="가상계좌(로컬 환경 제공X)" disabled>
            <input class="btn btn-primary btn-md" id="phone" type="button" value="휴대폰(테스트환경 제공X)" disabled>

            <%--     todo :: alert 으로 환불 해주기.     --%>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

</body>

<%@include file="../includes/footer.jsp"%>
<script>

  $("#select_amount").on('change', function () {
    var amount = $('select[name=select_amount]').val();
    var amount_addTxt = Math.floor(amount * 1.1);
    var chargePoint = Math.floor(amount * 1.08);

    $("#amount").text(amount_addTxt.toLocaleString());
    $("#amount").val(amount_addTxt.toLocaleString());
    $("#point").text(chargePoint.toLocaleString());
    $("#point").val(chargePoint.toLocaleString());
  });

  var clientKey = 'test_ck_lpP2YxJ4K87XzoP5AwX3RGZwXLOb';
  var tossPayments = TossPayments(clientKey);

  $("#card").on('click', function () {

    var chk = $('select[name=select_amount]').val();

    if (chk === "") {
      alert("결제 금액을 선택해 주세요.");
      return false;
    }

    $.ajax({
      url: '/get/orderId',
      type: 'post',
      success: function (data) {
        var selectAmount = $('select[name=select_amount]').val();
        var orderId = data.orderId;
        var orderName = selectAmount + " 포인트";
        var customerName = "오정환"
        var amount = Math.floor(selectAmount * 1.1);

        tossPayments.requestPayment('카드', {
          amount: amount,
          orderId: orderId,
          orderName: orderName,
          customerName: customerName,
          successUrl: window.location.origin + "/success",
          failUrl: window.location.origin + "/fail",
        })
      }
    });
  });

  $("#phone").on('click', function () {

    var chk = $('select[name=select_amount]').val();

    if (chk === "") {
      alert("결제 금액을 선택해 주세요.");
      return false;
    }

    $.ajax({
      url: '/get/orderId',
      type: 'post',
      success: function (data) {
        var selectAmount = $('select[name=select_amount]').val();
        var orderId = data.orderId;
        var orderName = selectAmount + " 포인트";
        var customerName = "오정환"
        var amount = Math.floor(selectAmount * 1.1);

        tossPayments.requestPayment('휴대폰', {
          amount: amount,
          orderId: orderId,
          orderName: orderName,
          customerName: customerName,
          successUrl: window.location.origin + "/success",
          failUrl: window.location.origin + "/fail",
        })
      }
    });
  });

  $("#account_transfer").on('click', function () {
    var chk = $('select[name=select_amount]').val();

    if (chk === "") {
      alert("결제 금액을 선택해 주세요.");
      return false;
    }

    $.ajax({
      url: '/get/orderId',
      type: 'post',
      success: function (data) {
        var selectAmount = $('select[name=select_amount]').val();
        var orderId = data.orderId;
        var orderName = selectAmount + " 포인트";
        var customerName = "오정환"
        var amount = Math.floor(selectAmount * 1.1);

        tossPayments.requestPayment('계좌이체', {
          amount: amount,
          orderId: orderId,
          orderName: orderName,
          customerName: customerName,
          successUrl: window.location.origin + "/success",
          failUrl: window.location.origin + "/fail",
        })
      }
    });
  });

  $("#virtual_account").on('click', function () {
    var chk = $('select[name=select_amount]').val();

    if (chk === "") {
      alert("결제 금액을 선택해 주세요.");
      return false;
    }

    $.ajax({
      url: '/get/orderId',
      type: 'post',
      success: function (data) {
        var selectAmount = $('select[name=select_amount]').val();
        var orderId = data.orderId;
        var orderName = selectAmount + " 포인트";
        var customerName = "오정환"
        var amount = Math.floor(selectAmount * 1.1);

        tossPayments.requestPayment('가상계좌', {
          amount: amount,
          orderId: orderId,
          orderName: orderName,
          customerName: customerName,
          successUrl: window.location.origin + "/success",
          failUrl: window.location.origin + "/fail",
          validHours: 24,
          cashReceipt: {
            type: '소득공제',
          },
          virtualAccountCallbackUrl: window.location.origin + "/virtual/account/callback",

        })
      }
    });
  });
</script>
</html>
