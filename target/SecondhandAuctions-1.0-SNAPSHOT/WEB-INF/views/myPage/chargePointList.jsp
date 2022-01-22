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
<div class="container">
    <div class="row">
        <div class="col-lg-3">
            <%@include file="../includes/myPageCategory.jsp"%>
        </div>

        <div class="col-lg-9">
            <h4 class="my-4">포인트 충전</h4>
            <div class="row">
                <form action="/point/charge/form" method="get">
                    <input type="submit" class="btn btn-primary btn-sm" value="포인트 충전하기">&nbsp;
                </form>
                <c:if test="${account.token eq null}">
                    <form id="authorizeFrm" name="authorizeFrm" method="get" action="https://testapi.openbanking.or.kr/oauth/2.0/authorize">
<%--                        respnse_type = code (고정값)--%>
<%--                        cliend_id = [발급받은 값]--%>
<%--                        redirect_uri = [API Key 등록 시 입력한 callback_url]--%>
<%--                        scope = [login inquiry transfer] (' ' 스페이스바로 구분)--%>
<%--                        state = 32자리의 난수 (임의로 난수 32자리 입력하면 됨)--%>
<%--                        auth_type = 0 (최초인증 : 0, 재인증 : 2)--%>

                        <input type="hidden" name="response_type" value="code"/>
                        <input type="hidden" name="client_id" value="a2f49b05-ce98-4c0d-876c-5d1d9e4a96f9"/>
                        <input type="hidden" name="redirect_uri" value="http://localhost:8080/oauth/callback"/>
                        <input type="hidden" name="scope" value="login inquiry transfer"/>
                        <input type="hidden" name="state" value="efe5bbe603b0474ba00781c7b342e93b"/>
                        <input type="hidden" name="auth_type" value="0"/>
                        <input type="submit" class="btn btn-primary btn-sm" value="포인트 환전">
                    </form>
                    <%-- 토큰 발급--%>
                </c:if>
                <c:if test="${account.token ne null}">
                    <c:if test="${account.accountChk eq true}">
                        <form action="/point/exchange/form" method="get">
                            <input type="submit" class="btn btn-primary btn-sm" value="포인트 환전">

                        </form>
                    </c:if>
                    <%-- 환불 진행 --%>
                    <c:if test="${account.accountChk eq false}">
                        <form action="/real/name/form">
                            <input type="submit" class="btn btn-primary btn-sm" value="포인트 환전">
                            <%-- 계좌 등록 --%>
                        </form>
                    </c:if>
                </c:if>
            </div>
            <br>
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
