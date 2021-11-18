<!-- <%--
  Created by IntelliJ IDEA.
  User: 렁환이
  Date: 2021-06-07
  Time: 오후 7:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
-->
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

    <title>중고 경매의 세계</title>
</head>

<body>
<%@include file="../includes/header.jsp"%>


<!-- Page Content -->
<div class="container">
    <div class="row">

        <div class="row">
            <h3 class="my-4">실시간 상품</h3> &nbsp;&nbsp;
            <span class="my-4">
          <button type="button" class="btn btn-outline-secondary btn-sm">신규등록순</button>
        </span>
            &nbsp;
            <span class="my-4">
          <button type="button" class="btn btn-outline-secondary btn-sm">마감임박순</button>
        </span>
        </div>

        <div class="col-lg-12">

            <div class="row">
                <c:forEach items="${list}" var="shopVo">
                    <div class="col-lg-4 col-md-6 mb-4">
                        <div class="card h-100">
                            <img class="card-img-top" src="/file/show?uploadPath=${shopVo.uploadPath}&fileName=${shopVo.uploadFileName}" alt="test" width="700" height="200">
                            <div class="card-body">
                                <h4 class="card-title">
                                    <a href="/shop/get?productId=<c:out value="${shopVo.productId}" />">${shopVo.productTitle}</a>
                                    <a class="move" href="<c:out value="${shopVo.productId}"/>">${shopVo.productTitle}</a>
                                </h4>
                                <h5>경매 시작 가격 : ${shopVo.startPrice}</h5>
                                <h5>경매 시작 시간 : <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${shopVo.startTime}" /></h5>
                                <h5>경매 마감 시간 : <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${shopVo.expireTime}" /></h5>
                                <p class="card-text">현재 입찰가격 : ${shopVo.bidPrice}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <!-- /.row -->

        </div>

    </div>
    <!-- /.row -->

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
        <form id="actionForm" action="/shop" method="get">
            <input type="hidden" name="page" id="pageNum" value="${pageMaker.criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${pageMaker.criteria.perPageNum}">
        </form>
    </div>

</div>

<!-- /.container -->

<!-- Footer -->
<footer class="py-5 bg-dark">
    <div class="container">
        <p class="m-0 text-center text-white">Copyright &copy; Your Website 2021</p>
    </div>
    <!-- /.container -->
</footer>



</body>
<script type="text/javascript">
    // todo :: pagination jquery active, prev, next

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

        $(".move").on("click", function (e) {
           e.preventDefault();

           var targetProductId = $(this).attr('href');

           console.log(targetProductId);

           actionForm.append("<input type='hidden' name='productId' value='"+targetProductId+"'>");
           actionForm.attr("action", "/shop/get").submit();
           // actionForm.submit();
        });

    });
</script>
</html>