<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/11/08
  Time: 12:17 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/detail.css">
    <link rel="stylesheet" href="/resources/css/spinner.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
    <script src="/resources/js/product.js"></script>

    <title>상품 조회 | 중고 경매의 세계</title>

</head>
<style>
    /* .card {
      position: absolute;
    } */
</style>
<body>

<%@include file="../includes/header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card text-center">
                <div class="card-body">
                    <div class="carousel slide" data-ride="carousel" id="banner">
                        <!-- 이미지 부분 -->
                        <div class="carousel-inner" id="images">
                            <c:forEach var="fileName" items="${fileName}" varStatus="stauts">
                                <div class="carousel-item ${stauts.index == 0 ? "active" : ""}">
                                    <img src="/detail/show?fileName=${fileName}">
                                </div>
                            </c:forEach>
                        </div>
                        <!-- 인디케이션 부분 -->
                        <ul class="carousel-indicators">
                            <%--                            <li data-target="#banner" data-slide-to="0" class="active"></li>--%>
                            <%--                            <li data-target="#banner" data-slide-to="1"></li>--%>
                            <%--                            <li data-target="#banner" data-slide-to="2"></li>--%>
                            <c:forEach var="count" items="${fileName}" varStatus="status">
                                <li data-target="#banner" data-slide-to="${status.index}"
                                    class="${status.index == 0 ? "active" : ""}"></li>
                            </c:forEach>
                        </ul>
                        <!-- 이동 버튼 부분 -->
                        <a class="carousel-control-prev" href="#banner" data-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                        </a>
                        <a class="carousel-control-next" href="#banner" data-slide="next">
                            <span class="carousel-control-next-icon"></span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="card card-signin flex-row my-5">
                <div class="card-body">
                    <br>

                    <div class="form-label-group">
                        <label for="productTitle">제목</label>
                        <input type="text" id="productTitle" name="productTitle" class="form-control"
                               readonly value="${product.productTitle}">
                    </div>
                    <br>

                    <div class="form-label-group">
                        <label for="productContent">내용</label>
                        <textarea name="productContent" id="productContent" cols="30" rows="10"
                                  class="productText form-control" readonly>${product.productContent}</textarea>
                    </div>
                    <br>

                    <br>

                    <div class="form-label-group">
                        <h5>
                        현재 가격
                        <c:if test="${empty product.bidPrice}">
                            <c:out value="${product.startPrice}"/>원
                        </c:if>
                        <c:if test="${not empty product.bidPrice}">
                            <c:out value="${product.bidPrice}"/>원
                        </c:if>
                        </h5>
                    </div>
                    <br>
                    <div class="float-right">
                        <input type="button" class="listBtn btn btn-outline-secondary btn-sm" value="조회 페이지">&nbsp;&nbsp;
                        <input type="button" class="bidModalBtn btn btn-outline-secondary btn-sm" data-toggle="modal"
                               data-target="#bidModal" value="입찰 하기">
                        <c:if test="${empty sessionScope.member}">
                            <div class="modal fade" id="bidModal" tabindex="-1" role="dialog"
                                 aria-labelledby="bidModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="nullModalLabel">입찰</h5>
                                        </div>
                                        <div class="modal-body">
                                            로그인이 필요한 서비스 입니다. &nbsp;&nbsp;<a href="/member/login/form">로그인 하러가기</a>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty sessionScope.member}">
                            <c:if test="${sessionScope.member eq product.memberId}">
                                <div class="modal fade" id="bidModal" tabindex="-1" role="dialog"
                                     aria-labelledby="bidModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="sameModalLabel">입찰</h5>
                                            </div>
                                            <div class="modal-body">
                                                <h5>본인의 물품에는 입찰을 할 수 없습니다.</h5>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${topBidMember eq sessionScope.member}">
                                <div class="modal fade" id="bidModal" tabindex="-1" role="dialog"
                                     aria-labelledby="bidModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">입찰</h5>
                                            </div>
                                            <div class="modal-body">
                                                <h5>최고 입찰자는 추가 입찰을 할 수 없습니다.</h5>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="modal fade" id="bidModal" tabindex="-1" role="dialog"
                                 aria-labelledby="bidModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="notNullModalLabel">입찰</h5>
                                        </div>
                                        <div class="modal-body">
                                            <form>
                                                <div class="form-group">
                                                    <label for="bidMemberId" class="col-form-label">입찰자</label>
                                                    <input type="text" class="form-control" id="bidMemberId" value="${sessionScope.member}">
                                                </div>
                                                <div class="form-group">
                                                    <label for="startPrice" class="col-form-label">입찰 금액</label>
                                                    <input type="text" class="form-control" id="startPrice" onkeyup="numberWithCommas(this.value)"> 원
                                                </div>
                                                <div class="spinner-border text-primary" role="status">
                                                    <span class="sr-only">Loading...</span>
                                                </div>
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기
                                            </button>
                                            <input type="button" class="bidBtn btn btn-primary" value="입찰하기">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                    </div>

                </div>
            </div>
            <div class="card text-left">
                <div class="card-body">
                    <h4>Q&A</h4><br>
                    <div>
                        <a id="questionBtn" href="/shop/question/form?productId=${product.productId}"
                           class="btn btn-primary btn-sm float-right">문의사항 남기기</a>
                    </div>
                    <br>
                    <br>
                    <br>
                    <table class="table">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">아이디</th>
                            <th scope="col">제목</th>
                            <th scope="col">날짜</th>
                            <th scope="col">답변 여부</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%-- foreach --%>
                        <c:forEach items="${qna}" var="qna">
                            <tr>
                                <td>${qna.get("memberId")}</td>
                                <td>${qna.get("questionTitle")}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value='${qna.get("regdate")}'/></td>
                                <td>
                                    <c:if test='${qna.get("isAnswer") eq true}'>
                                        <a style="color: green;" data-toggle="collapse" href='#${qna.get("questionId")}'
                                           role="button" aria-expanded="false" aria-controls="collapseExample">
                                            답변완료
                                        </a>
                                    </c:if>
                                    <c:if test='${qna.get("isAnswer") eq false}'>
                                        <a style="color: red;" data-toggle="collapse" href='#${qna.get("questionId")}'
                                           role="button" aria-expanded="false" aria-controls="collapseExample">
                                            미답변
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <c:if test="${empty sessionScope.member}">
                                    <td colspan="4">
                                        <div class="collapse" id='${qna.get("questionId")}'>
                                            <span style="color: red">읽을 수 없는 계정입니다.</span>
                                        </div>
                                    </td>
                                </c:if>
                                <c:if test="${not empty sessionScope.member}">
                                    <c:if test='${sessionScope.get("member") eq product.memberId || sessionScope.get("member") eq qna.get("memberId")}'>
                                        <c:if test='${qna.get("isAnswer") eq true}'>
                                            <td colspan="4">
                                                <div class="collapse" id='${qna.get("questionId")}'>
                                                    <c:out value='${qna.get("answer")}'/>
                                                </div>
                                            </td>
                                        </c:if>

                                        <c:if test='${qna.get("isAnswer") eq false}'>
                                            <td colspan="4">
                                                <div class="collapse" id='${qna.get("questionId")}'>
                                                    아직 답변이 달리지 않았습니다.
                                                </div>
                                            </td>
                                        </c:if>
                                    </c:if>
                                    <c:if test='${sessionScope.get("member") ne product.memberId || sessionScope.get("member") ne qna.get("memberId")}'>
                                        <td colspan="4">
                                            <div class="collapse" id='${qna.get("questionId")}'>
                                                <span style="color: red">답변을 보실 수 없는 계정 입니다.</span>
                                            </div>
                                        </td>
                                    </c:if>
                                </c:if>
                            </tr>
                        </c:forEach>
                        <%-- end foreach --%>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div>
        <form id="actionForm" action="/shop" method="get">
            <input type="hidden" name="page" id="pageNum" value="${criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${criteria.perPageNum}">
        </form>
    </div>
    <div>
        <form id="questionForm" action="/shop/question/form" method="get">
            <input id="productId" type="hidden" value="${product.productId}">
        </form>
    </div>
</div>
<%--<./container>--%>


<hr>
</body>
<script>
    $(document).ready(function () {
        $(".spinner-border").hide();

        var msg = "${msg}";
        var actionForm = $("#actionForm");

        if (msg === "productNull") {
            window.alert(msg);
            window.location.replace("/shop");
        }

        // todo :: error!!
        $(".listBtn").on("click", function (e) {
            e.preventDefault();
            actionForm.submit();
        });


        $("#questionBtn").on("click", function () {

            var productWriter = "${product.memberId}";
            var questionWriter = "${sessionScope.member}";

            if ((questionWriter === null) || (questionWriter === "")) {
                window.alert("로그인 후 이용 가능합니다.");
                return false;
            }

            if (productWriter === questionWriter) {
                window.alert("자신의 글에는 문의사항을 남길 수 없습니다.");
                return false;
            }

            $("#questionForm").submit();

        });

        $(".bidModalBtn").on("click", function () {
            $("#startPrice").val("");
        })

        $(".bidBtn").on("click", function () {
            var chk = false;
            var bidMemberId = "${sessionScope.member}";
            var bidPrice = $("#startPrice").val();
            var startPrice = "${product.startPrice}";
            var productId = ${product.productId};
            var pageUrl = "";

            chk = numberComparison(bidPrice, startPrice);
            pageUrl = document.location.href;

            if (chk === false) {
                alert("입찰금액은 현재가격 보다 커야 합니다.");
                return;
            }

            $(".spinner-border").show();
            $("#bidModal").css({
                'pointer-events': 'none',
                'opacity': '0.5'
            });

            $.ajax({
               url: '/bid',
               type: 'post',
               data: {
                   bidMemberId : bidMemberId,
                   bidPrice : bidPrice,
                   productId : productId,
                   pageUrl : pageUrl
               },

               success: function (result) {
                   var check = result.check;

                   if (check === 1) {
                       window.alert("해당 상품에 " + bidPrice + " 원을 입찰하셨습니다.");
                       window.location.reload();
                   }
               }
            });

        });

    });


</script>
</html>

