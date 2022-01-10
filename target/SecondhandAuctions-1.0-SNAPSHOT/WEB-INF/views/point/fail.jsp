<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/22
  Time: 11:52 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>결제 실패 | 중고 경매의 세계</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <div class="form-row float-left">
                        <a href="/">메인 페이지</a>
                    </div>
                    <br>
                    <br>
                    <h2 class="card-title text-center">결제 실패</h2>
                    <hr>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>

                    <div>
                        <h3>포인트 결제를 실패 했습니다.</h3>
                    </div>
                    <h6>${tossError.code}</h6>
                    <h6>${tossError.message}</h6>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>

                    <br>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
