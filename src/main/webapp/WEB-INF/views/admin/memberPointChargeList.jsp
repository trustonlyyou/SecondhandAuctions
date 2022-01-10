
<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/08
  Time: 3:22 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

</head>
<body>
<%@include file="../includes/header.jsp"%>

<div class="container">
    <form id="actionFrom" action="/admin/searchId/pay/info/list" method="post">
        <input type="text" id="keyword" name="keyword" placeholder="고객의 아이디를 입력">
        <input type="submit" class="btn btn-primary btn-sm" value="확인">
    </form>
    <hr>
    <div class="row">
        <div class="col-lg-7">
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">결제일자</th>
                    <th scope="col">결제금액</th>
                    <th scope="col">결제수단</th>
                    <th scope="col">구매자</th>
                    <th scope="col">상품명</th>
                    <th scope="col">결제취소</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${chargeList}" var="point">
                        <tr>
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${point.approvedAt}"/></td>
                            <td>${point.totalAmount}</td>
                            <td>${point.method}</td>
                            <td>${point.memberName}</td>
                            <td>${point.orderName}</td>
                            <td>
                                <form>
                                    <input type="button" class="btn btn-outline-danger" value="결제취소">
                                    <input type="hidden" value="${point.paymentKey}">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="col-lg-5">
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">아이디</th>
                    <th scope="col">이름</th>
                    <th scope="col">지불 포인트</th>
                    <th scope="col">결제 날짜</th>
                </tr>
                </thead>
                <tbody>
                    <c:if test="${payList eq null}">
                        <th>내역이 존재하지 않습니다.</th>
                    </c:if>
                    <c:if test="${payList ne null}">
                        <th>hello</th>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
