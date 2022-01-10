<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/08
  Time: 3:22 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>중고 경매의 세계 | 포인트 충전 정보</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
<%@include file="../includes/header.jsp"%>
인
<div class="container">
    <div class="row">
        <div class="col-lg-3">
            <h2 class="my-4">포인트 충전 정보</h2>
            <%@include file="../includes/myPageCategory.jsp"%>
        </div>

        <div class="col-lg-9">
            <h4 class="my-4">포인트 충전 리스트 <a href="/point/charge/form" class="btn btn-primary btn-md">포인트 충전 하기</a></h4>
            <h6>나의 포인트 : ${myPoint}&nbsp;포인트</h6>
            <hr style="border: solid 1px;">

            <c:if test="${empty list}">
                <div class="form-group row">
                    <div class="col-sm-10">
                        <h5>포인트 충전 정보가 없습니다.</h5>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list}">
                <table class="table">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col">No</th>
                        <th scope="col">결제번호</th>
                        <th scope="col">결제일자</th>
                        <th scope="col">결제금액</th>
                        <th scope="col">결제수단</th>
                        <th scope="col">구매자</th>
                        <th scope="col">상품명</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="point" varStatus="status">
                        <tr>
                            <td scope="row">${status.count}</td>
                            <td>${point.orderId}</td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${point.approvedAt}"/></td>
                            <td>${point.totalAmount}</td>
                            <td>${point.method}</td>
                            <td>${point.memberName}</td>
                            <td>${point.orderName}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
    <div>
        <ul class="pagination justify-content-end">
            <c:if test="${pageMaker.prev}">
                <li class="page-item">
                    <a class="page-link" href="${pageMaker.startPage - 1}" tabindex="-1">이전</a>
                </li>
            </c:if>
            <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="num">
                <li class="page-item ${pageMaker.criteria.page == num ? "active": ""}">
                    <a class="page-link" href="${num}">${num}</a></li>
            </c:forEach>
            <c:if test="${pageMaker.next}">
                <li class="page-item">
                    <a class="page-link" href="${pageMaker.endPage + 1}" tabindex="-1">다음</a>
                </li>
            </c:if>
        </ul>
    </div>

    <div>
        <form id="actionForm" action="/myPage/myPoint" method="get">
            <input type="hidden" name="page" id="pageNum" value="${pageMaker.criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${pageMaker.criteria.perPageNum}">
        </form>
    </div>
</div>
</body>
<script>
    $(document).ready(function () {

        history.replaceState({}, null, null); // 히스토리를 지운다.

        var actionForm = $("#actionForm");

        $(".page-link").on("click", function (e) {
            e.preventDefault(); // a 태그의 기본 동작을 막는다. 즉, 기본 동작을 막아준다.

            var targetPage = $(this).attr("href");

            console.log(targetPage);

            actionForm.find("[name='page']").val(targetPage);
            actionForm.submit();
        });
    });
</script>
</html>
