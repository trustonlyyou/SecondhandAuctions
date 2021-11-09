<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/11/08
  Time: 12:17 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
        width: 750px;
        height: 500px;
    }

    .carousel-inner > .carousel-item > img{
        width: 700px; height: 500px;
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
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <img src="896FCA67-78F4-40DC-9E1A-48D090D2BDB5.jpeg" alt="beg">
                            </div>
                            <div class="carousel-item">
                                <img src="78639320-0D45-4451-AA41-5B778837D6BA.jpeg" alt="beg" >
                            </div>
                            <div class="carousel-item">
                                <img src="A2605FF7-A557-4247-865D-7CE707B275FD.jpeg" alt="beg" >
                            </div>
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
                    <br>
                    <h2 class="card-title text-center">상품 조회</h2>

                    <!-- Form 시작 -->
                    <form class="form-signin" method="post" action="/register/product/submit" id="registerProduct" enctype="multipart/form-data">
                        <br>


                        <div class="form-label-group">
                            제목<br>
                            <input type="text" id="productTitle" name="productTitle" class="form-control"
                                   placeholder="상품의 제목을 입력해주세요." minlength="5" maxlength="20">
                        </div>
                        <br>

                        <div class="form-label-group">
                            내용<br>

                            <textarea name="productContent" id="productContent" cols="30" rows="10" class="form-control"></textarea>
                        </div>
                        <div name="contentCheckMsg" id="contentCheckMsg"></div>
                        <br>

                        <br>

                        <div class="form-label-group">
                            현재 가격
                            <input type="text" id="startPrice" name="startPrice" onkeyup="numberWithCommas(this.value)">&nbsp;원
                        </div>



                        <br>
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
</div>


<hr>
</body>
<script>

</script>
</html>

