<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/13
  Time: 10:53 오전
  To change this template use File | Settings | File Templates.
--%>
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
            <h4 class="my-4">나의 입찰 리스트</h4>
            <hr style="border: solid 1px;">
            <form>
                <c:if test="${empty list}">
                    <div class="form-group row">
                        <div class="col-sm-10">
                            <h5>판매 정보가 없습니다.</h5>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty list}">
                    <c:forEach items="${list}" var="productVo">
                        <div class="form-group row">
                            <div class="col-sm-10">
                                <h6>
                                    마감일 : <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${productVo.expireTime}"/><br>
                                </h6>
                                <h6>
                                    제목 : <a class="move" href="<c:out value="${productVo.productId}"/>"><c:out value="${productVo.productTitle}"/></a><br>
                                </h6>
                                <h6 style="color: red">
                                    현재 가격 : <c:out value="${productVo.nowPrice}"/><br>
                                </h6>
                                <h6 style="color: green">
                                    나의 입찰 가격 : <c:out value="${productVo.bidPrice}"/><br>
                                </h6>
                            </div>
                        </div>
                        <hr>
                    </c:forEach>
                </c:if>
            </form>

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
        </div>
    </div>
    <!-- .row -->
    <div>
        <form id="actionForm" action="/myBid/list" method="get">
            <input type="hidden" name="page" id="pageNum" value="${pageMaker.criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${pageMaker.criteria.perPageNum}">
        </form>
    </div>
</div>
</body>
<script type="text/javascript">
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
        });

        $(".move").on("click", function (e) {
            e.preventDefault();

            var targetProductId = $(this).attr('href');

            console.log(targetProductId);

            actionForm.append("<input type='hidden' name='productId' value='"+targetProductId+"'>");
            actionForm.attr("action", "/shop/get").submit();
        });
    });
</script>
</html>

