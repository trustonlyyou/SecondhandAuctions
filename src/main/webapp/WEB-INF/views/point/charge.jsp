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
  <script src="https://js.tosspayments.com/v1"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>

<body>
<div><label><input type="radio" name="method" value="카드" checked/>신용카드</label></div>
<div><label><input type="radio" name="method" value="가상계좌"/>가상계좌</label></div>
<input class="btn btn-primary btn-sm" id="pay" type="button" value="결제">
</body>
<script>
  // * 테스트용 클라이언트 키로 시작하세요
  var clientKey = 'test_ck_JQbgMGZzorzzXdypGB7rl5E1em4d';
  // var clientKey = 'test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq';
  var tossPayments = TossPayments(clientKey);
  var orderMethod = document.querySelector('input[name=method]:checked').value;

  $("#pay").on('click', function () {
    // 결제 창 호출
    tossPayments.requestPayment('카드', {
      amount: 1,
      orderId: 'JnncXbv3PpihwBU6lU-Fkss',
      orderName: '10원 쿠폰',
      customerName: '오토스',
      successUrl: window.location.origin + "/point/success",
      failUrl: window.location.origin + "/point/fail",
    })

    if (orderMethod == '가상계좌') {
      paymentData.virtualAccountCallbackUrl = window.location.origin + '/virtual-account/callback'
    }

    tossPayments.requestPayment(orderMethod, paymentData);

  });



</script>
</html>
