
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
        <div class="col-lg-8">
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">결제일자</th>
                    <th scope="col">결제금액</th>
                    <th scope="col">결제수단</th>
                    <th scope="col">구매자</th>
                    <th scope="col">아이디</th>
                    <th scope="col">상품명</th>
                    <th scope="col">결제취소</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${chargeList}" var="point" varStatus="status">
                        <tr>
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${point.approvedAt}"/></td>
                            <td>${point.totalAmount}</td>
                            <td>${point.method}</td>
                            <td>${point.memberName}</td>
                            <td>${point.memberId}</td>
                            <td>${point.orderName}</td>
                            <td>
                                <c:if test="${point.CancelReq eq false}">
                                <button type="button" class="btn btn btn-outline-danger btn-sm" data-toggle="modal" data-target="#modal${status.index}" data-whatever="취소사유">결제취소</button>

                                </c:if>
                                <c:if test="${point.CancelReq eq true}">
                                    <button type="button" class="btn btn btn-outline-danger btn-sm" data-toggle="modal" data-target="#modal${status.index}" data-whatever="취소사유" disabled>결제취소</button>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <div class="modal fade" id="modal${status.index}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="modalLabel">결제취소</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <form id="cancelForm${status.index}" name="cancelForm${status.index}">
                                                <div class="form-group">
                                                    <input type="text" class="form-control" id="recipient-name" placeholder="취소 사유" readonly>
                                                </div>
                                                <div class="form-group">
                                                    <textarea class="form-control" id="cancelReason_${status.index}" name="cancelReason_${status.index}"></textarea>
                                                </div>
                                                <input type="hidden" id="paymentKey_${status.index}" name="paymentKey_${status.index}" value="${point.paymentKey}">
                                                <input type="hidden" id="memberId_${status.index}" name="memberId_${status.index}" value="${point.memberId}">
                                                <input type="hidden" id="orderId_${status.index}" name="orderId_${status.index}" value="${point.orderId}">
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                                            <button type="button" id="cancel_btn" class="btn btn-primary" onclick="targetNo(${status.index})">결제 취소</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="col-lg-4">
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
<script>

    function targetNo(index) {
        var paymentKeyId = "#paymentKey_" + index;
        var cancelReasonId = "#cancelReason_" + index;
        var memberIdId = "#memberId_" + index;
        var orderIdId = "#orderId_" + index;

        var paymentKey = $(paymentKeyId).val();
        var cancelReason = $(cancelReasonId).val();
        var memberId = $(memberIdId).val();
        var orderId = $(orderIdId).val();

        var formData = {
            paymentKey : paymentKey,
            cancelReason : cancelReason,
            memberId : memberId,
            orderId : orderId
        }

        $.ajax({
            url: '/admin/cancel/point/date/chk',
            type: 'post',
            data: {
                memberId : memberId
            },

            success: function (data) {
                var result = data.result;

                if (result === true) {
                    $.ajax({
                        url: '/cancel/point/pay',
                        type: 'post',
                        data: formData,
                        success: function (data) {
                            var result = data.result;

                            if (result == true) {
                                alert("결제 취소가 완료 되었습니다.");
                                window.location.replace("/admin/pay/info/list")
                            } else {
                                alert("결제 취소를 실패 했습니다.");
                                window.location.replace("/admin/pay/info/list")
                            }
                        },
                        error: function (request,status,error) {

                        }
                    });
                } else {
                    var chk = data.msg;

                    if ("exchangePointChk" === chk) {
                        alert("포인트 결제 후 포인트 환전 내역이 있으므로 환불이 불가합니다.");
                    } else if ("pointPayChk" === chk) {
                        alert("포인트 결제 후 포인트 결제 내역이 있으므로 환불이 불가합니다.");
                    } else if ("cancelPointChk" === chk) {
                        alert("포인트 결제 후 포인트 취소 내역이 있으므로 환불이 불가합니다.");
                    } else {
                        alert(chk);
                    }
                }
            }
        });
    }
</script>
</html>
