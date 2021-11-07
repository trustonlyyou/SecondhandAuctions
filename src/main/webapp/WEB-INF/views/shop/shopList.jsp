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

    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/shop-homepage.css" rel="stylesheet">
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
                            <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                            <div class="card-body">
                                <h4 class="card-title">
                                    <a href="#">${shopVo.productTitle}</a>
                                </h4>
                                <h5>${shopVo.startPrice}&nbsp; <fmt:formatDate pattern="yyyy-MM-dd HH:mm"
                                                                               value="${shopVo.startTime}" /></h5>
                                <p class="card-text">${shopVo.productContent}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item One</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur!</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item Two</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur! Lorem ipsum dolor sit amet.</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item Three</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur!</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item Four</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur!</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item Five</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur! Lorem ipsum dolor sit amet.</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card h-100">
                        <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                        <div class="card-body">
                            <h4 class="card-title">
                                <a href="#">Item Six</a>
                            </h4>
                            <h5>$24.99</h5>
                            <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet numquam aspernatur!</p>
                        </div>
                    </div>
                </div>

            </div>
            <!-- /.row -->

        </div>

    </div>
    <!-- /.row -->

    <h3>${pageMaker}</h3>
    <div>
        <ul class="pagination justify-content-end">
            <c:if test="${pageMaker.prev}">
                <li class="page-item">
                    <a class="page-link" href="${pageMaker.startPage - 1}" tabindex="-1">이전</a>
                </li>
            </c:if>
            <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="num">
                <li class="page-item ${pageMaker.criteria.pageNum == num ? "active": ""}"><a class="page-link" href="${num}">${num}</a></li>
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
            <input type="hidden" name="pageNum" id="pageNum" value="${pageMaker.criteria.pageNum}">
            <input type="hidden" name="amount" id="amount" value="${pageMaker.criteria.amount}">
        </form>
    </div>

    <%--    <nav>--%>
    <%--        <ul class="pagination pagination-circle pg-blue justify-content-end">--%>
    <%--            <li class="prev disabled">--%>
    <%--                <a class="page-link">이전</a>--%>
    <%--            </li>--%>
    <%--            <li class="page-item active"><a class="page-link" href="#">1</a></li>--%>
    <%--            <li class="page-item"><a class="page-link" href="#">2</a></li>--%>
    <%--            <li class="page-item"><a class="page-link" href="#">3</a></li>--%>
    <%--            <li class="next">--%>
    <%--                <a class="page-link" href="#">다음</a>--%>
    <%--            </li>--%>
    <%--        </ul>--%>
    <%--    </nav>--%>
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

    var actionForm = $("#actionForm");

    $(".page-link").on("click", function (e) {
        e.preventDefault(); // a 태그의 기본 동작을 막는다. 즉, 기본 동작을 막아준다.

        var targetPage = $(this).attr("href");

        console.log(targetPage);

        actionForm.find("input[name='pageNum']").val(targetPage);
        actionForm.submit();
    });


</script>
</html>