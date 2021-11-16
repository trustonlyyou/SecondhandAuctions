<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/11/08
  Time: 12:17 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

    <title>상품 조회 | 중고 경매의 세계</title>

</head>
<style>
    .card {
        width: 800px;
    }
    .carousel {
        /* width: 750px; */
        width: 100%;
        height: 500px;
    }

    .carousel-inner > .carousel-item > img{
        /* width: 700px;  */
        width: 80%;
        height: 500px;
    }

    .carousel-control-prev-icon, .carousel-control-next-icon {
        background-color: rgba(0, 0, 0, 1);
    }
    /* .card {
      position: absolute;
    } */
</style>
<body>


<div class="container">

    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card text-center">
                <div class="card-body">
                    <div class="carousel slide" data-ride="carousel" id="banner" >
                        <!-- 이미지 부분 -->
                        <div class="carousel-inner" id="images">
                            <c:forEach var="fileName" items="${fileName}" varStatus="stauts">
                                <%--                             todo :: 여기서부터   --%>
                                <div class="carousel-item ${stauts.index == 0 ? "active" : ""}">
                                    <img src="/detail/show?fileName=${fileName}">
                                </div>
                            </c:forEach>
                        </div>
                        <!-- 인디케이션 부분 -->
                        <ul class="carousel-indicators">
                            <li data-target="#banner" data-slide-to="0" class="active"></li>
                            <li data-target="#banner" data-slide-to="1"></li>
                            <li data-target="#banner" data-slide-to="2"></li>
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
                    <%--                    <h2 class="card-title text-center">상품 조회</h2>--%>
                    <!-- Form 시작 -->
                    <form class="form-signin" method="post" action="/register/product/submit" id="registerProduct" enctype="multipart/form-data">
                        <br>


                        <div class="form-label-group">
                            제목<br>
                            <input type="text" id="productTitle" name="productTitle" class="form-control"
                                   readonly value="${product.productTitle}">
                        </div>
                        <br>

                        <div class="form-label-group">
                            내용<br>

                            <textarea name="productContent" id="productContent" cols="30" rows="10" class="form-control" readonly>
                                <c:out value="${product.productContent}"/>
                            </textarea>
                        </div>
                        <div name="contentCheckMsg" id="contentCheckMsg"></div>
                        <br>

                        <br>

                        <div class="form-label-group">
                            현재 가격 <c:out value="${product.startPrice}"/>
                            <%--                            <input type="text" id="startPrice" name="startPrice" readonly value="">&nbsp;원--%>
                        </div>
                        <br>
                        <div>
                            <input type="button" class="listBtn btn btn-primary btn-sm float-right" value="조회 페이지">
                        </div>
                    </form>
                </div>
            </div>
            <div class="card text-left">
                <div class="card-body">
                    <h4>Q&A</h4><br>
                    <table class="table">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">번호</th>
                            <th scope="col">아이디</th>
                            <th scope="col">제목</th>
                            <th scope="col">날짜</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <th scope="row">1</th>
                            <td>Mark</td>
                            <td>Otto</td>
                            <td>@mdo</td>
                        </tr>
                        <tr>
                            <th scope="row">2</th>
                            <td>Jacob</td>
                            <td>Thornton</td>
                            <td>@fat</td>
                        </tr>
                        <tr>
                            <th scope="row">3</th>
                            <td>Larry the Bird</td>
                            <td>@twitter</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div>
        <form id="actionForm" action="/myShop/list" method="get">
            <input type="hidden" name="page" id="pageNum" value="${criteria.page}">
            <input type="hidden" name="perPageNum" id="amount" value="${criteria.perPageNum}">
            <input type="hidden" name="productId" id="productId" value="${product.productId}">
        </form>
    </div>
</div>
<%--<./container>--%>



<hr>
</body>
<script>
    $(document).ready(function () {
        var msg = "${msg}";
        var actionForm = $("#actionForm");

        if (msg === "productNull") {
            alert(msg);
            window.location.replace("/shop");
        }

        $(".listBtn").on("click", function (e) {
            e.preventDefault();
            actionForm.submit();
        });
    });


</script>
</html>

