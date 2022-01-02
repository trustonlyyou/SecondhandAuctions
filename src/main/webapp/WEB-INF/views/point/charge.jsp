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
  <title>결제하기</title>
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

            <input class="btn btn-primary btn-md" id="card" type="button" value="카드">
            <input class="btn btn-primary btn-md" type="button" value="계좌 이체">
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<%--<h1>TEST</h1>--%>
<%--  <div>--%>
<%--      <input class="btn btn-outline-secondary btn-sm" type="button" id="card" value="카드">--%>
<%--  </div>--%>
<%--  <div>--%>
<%--    <input class="btn btn-outline-secondary btn-sm" type="button" id="account_transfer" value="계좌이체">--%>
<%--  </div>--%>
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

  // $("#card").on('click', function () {
  //   tossPayments.requestPayment('카드', {
  //     amount: 1,
  //     orderId: 'SH_20220102173105_1',
  //     orderName: '토스 티셔츠 외 2건',
  //     customerName: '박토스',
  //     successUrl: 'http://localhost:8080/card/success',
  //     failUrl: 'http://localhost:8080//point/fail'
  //   })
  // });
  //
  // $("#account_transfer").on("click", function () {
  //   tossPayments.requestPayment('계좌이체', {
  //     amount: 1,
  //     orderId: 'uW4ERJzrYnrKbzH9kSY3H',
  //     orderName: '포인트 결제권 1000원',
  //     customerName: '오정환',
  //     successUrl: window.location.origin + "/account/transfer",
  //     failUrl: window.location.origin + "/point/fail",
  //     bank: '기업'
  //   })
  // });



</script>
</html>
