<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/24
  Time: 10:45 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>중고 경매의 세계 | 로그인 선택</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
    <%@include file="../includes/header.jsp"%>

    <div class="container">
        <div class="row">
            <div class="col-lg-10 col-xl-7 mx-auto">
                <div class="card card-signin flex-row my-5">
                    <div class="card-body text-center">
                        <h2 class="card-title text-center">로그인 선택 &nbsp;</h2>
                        <br>
                        <br>
                        <div>
                            <a href="/member/login/form" class="btn btn-primary mb-4">중고경매의 세계 로그인</a>
                        </div>
                        <div>
                            <a href="/member/kakao/login/init"><img width="175px" height="38px" src="/resources/image/kakao_login_btn.png"></a>
                        </div>

                        <br>
                        <br>
                        <br>
                        <br>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../includes/footer.jsp"%>
</body>
</html>
