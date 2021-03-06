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
    <script src="/resources/js/price.js"></script>
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
            <h4 class="my-4">낙찰 리스트</h4>
            <hr style="border: solid 1px;">
            <a href="/myBid/success/sell" class="btn btn-primary btn-sm float-right">판매 성공 물품</a>
            <br>
            <br>
            <br>
                <c:if test="${empty list}">
                    <div class="form-group row">
                        <div class="col-sm-10 text-center">
                            <h5>경매 성공 정보가 없습니다.</h5>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty list}">
                    <c:forEach items="${list}" var="bid" varStatus="var">
                        <div class="form-group row">
                            <div class="col-sm-10">
                                <div class="row">
                                    <h6>
                                        제목 : <a href="/myBid/success/bid/detail?successBidNo=${bid.successBidNo}&productId=${bid.productId}"><c:out value="${bid.productTitle}"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    </h6>
                                    <input type="button" class="btn btn-outline-primary btn-sm" value="포인트로 결제하기" onclick="pointPay(${var.index})">
                                </div>
                            </div>
                            <div>
                                <form id="point_pay_${var.index}">
                                    <input type="hidden" id="successBidNo" name="successBidNo"  value="${bid.successBidNo}">
                                    <input type="hidden" id="seller" name="seller" value="${bid.memberId}">
                                    <input type="hidden" id="bidder" name="bidder" value="${bid.bidMemberId}">
                                    <input type="hidden" id="bidPrice" name="bidPrice" value="${bid.bidPrice}">
                                </form>
                            </div>
                        </div>
                        <hr>
                    </c:forEach>
                </c:if>
        </div>
    </div>
    <!-- .row -->
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
        <form id="actionForm" action="/myShop/list" method="get">
            <input type="hidden" name="page" id="pageNum" value="${pageMaker.criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${pageMaker.criteria.perPageNum}">
        </form>
    </div>
</div>
</body>
<script type="text/javascript">


    // 포인트로 상품 결제하기
    function pointPay(index) {
        console.log("index :: " + index);
        var targetFormId = "#point_pay_" + index;

        alert($(targetFormId).serialize());

        $.ajax({
            url: '/point/pay/success/bid',
            type: 'post',
            data: $(targetFormId).serialize(),

            success: function (data) {
                var msg = data.msg;
                var result = data.result;

                if (result == true) {
                    alert("포인트로 결제가 완료 되었습니다.");
                    window.location.replace("/myBid/success/bid");
                } else {
                    if (msg === "MATCH") {
                        alert("정보가 일치하지 않습니다. 고객센터에 문의해 주세요.");
                    } else if (msg === "PAY") {
                        alert("결제할 포인트가 부족합니다.");
                    }
                }
            }
        });
    }


    var status = "${msg}";

    if (status) {
        alert(status);
    }

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
