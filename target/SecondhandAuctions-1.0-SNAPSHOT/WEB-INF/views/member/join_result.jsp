<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/21
  Time: 2:35 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입 결과 | 중고 경매의 세계</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
<%@include file="../includes/header.jsp"%>
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
                    <h2 class="card-title text-center">중고 경매 세계에 오신것을 환영합니다.</h2>
                    <hr>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>

                    <div>
                        <h3>회원가입이 완료되었습니다.</h3>
                    </div>

                    <br>
                    <br>
                    <br>
                    <br>
                    <br>

                    <br>

                    <div>
                        <a href="/member/login/list" class="btn btn-primary btn-lg" role="button" aria-pressed="true">로그인</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../includes/footer.jsp"%>
</body>
</html>
<script>
    // todo :: 로그인 버트 활성화
</script>