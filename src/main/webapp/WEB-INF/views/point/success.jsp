<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/22
  Time: 11:52 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>결제 성공</title>
    <meta http-equiv="x-ua-compatible" content="ie=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/toss/style.css" />
</head>
<body>
<section>
    <h1>결제 성공</h1>
    <h3>상품명: 토스 티셔츠</h3>
    <h3>주문번호: {{orderId}}</h3>
</section>
</body>
</html>
