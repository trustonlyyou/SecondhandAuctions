<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/08
  Time: 3:22 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="../includes/header.jsp"%>
<table class="table">
    <thead class="thead-light">
    <tr>
        <th scope="col">No</th>
        <th scope="col">주문번호</th>
        <th scope="col">주문일자</th>
        <th scope="col">금액</th>
        <th scope="col">할부</th>
        <th scope="col">결제수단</th>
        <th scope="col">결제정보</th>
        <th scope="col">구매자</th>
        <th scope="col">상품명</th>
        <th scope="col">취소</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <th scope="row">1</th>
        <td>Mark</td>
        <td>Otto</td>
        <td>@mdo</td>
    </tr>
    </tbody>
</table>
<%@include file="../includes/footer.jsp"%>
</body>
</html>
