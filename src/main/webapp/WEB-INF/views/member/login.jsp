<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/18
  Time: 6:11 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <title>로그인 | 중고 경매의 세계</title>

</head>
<body>
<header>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/">Hello Auctions</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive"
                    aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <nav class="navbar navbar-dark bg-dark">
                <div class="container-fluid">
                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search"> &nbsp;
                        <button class="btn btn-outline-light" type="submit">상품을 검색해 보세요.</button>
                    </form>
                </div>
            </nav>
            <div class="collapse navbar-collapse" id="navbarResponsive">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/">Home
                            <span class="sr-only">(current)</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/member/login/form">로그인</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/member/join/form">회원가입</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<br>
<br>
<br>
<br>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <h2 class="card-title text-center">로그인</h2>
                    <hr>
                    <br>

                    <form class="form-signin" action="/member/login/action" method="post">
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="memberId" name="memberId" placeholder="아이디를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                        </div>

                        <br>

                        <div class="input-group input-group-lg">
                            <input type="password" class="form-control" id="memberPassword" name="memberPassword" placeholder="비밀번호를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                        </div>

                        <br>
                        <br>

                        <div>
                            <input type="submit" id="login" class="btn btn-dark btn-lg btn-block" value="로그인" disabled>

                        </div>

                        <br>

                        <span>
                                <a href="/member/search/id">아이디 찾기</a>&nbsp;
                            </span>
                        <span>
                                <a href="/member/search/password">비밀번호 찾기</a>
                            </span>

                        <hr>

                        <br>
                        <div>
                            <input type="submit" id="kakaoLogin" class="btn btn-warning btn-lg btn-block" value="카카오톡 로그인" disabled>

                        </div>

                        <br>

                        <div>
                            <input type="submit" id="appleLogin" class="btn btn-outline-dark btn-lg btn-block" value="애플 로그인" disabled>

                        </div>

                        <br>
                        <br>

                        회원이 아니시라면 회원 가입을 해주세요. &nbsp; <a href="">회원가입</a>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    var idFlag = false;
    var passwordFlag = false;

    $("#memberId").on('keyup', function () {
        var memberId = $("#memberId").val();

        if (memberId.length < 4) {
            return false;
        } else {
            idFlag = true;

            $.fn.flagCheck();

            return true;
        }
    });

    $("#memberPassword").on('keyup', function () {
        var memberPassword = $("#memberPassword").val();

        if (memberPassword.length < 2) {
            return false;
        } else {
            passwordFlag = true;

            $.fn.flagCheck();

            return true;
        }
    });

    $.fn.flagCheck = function () {
        if ((idFlag && passwordFlag) == true) {
            $("#login").attr('disabled', false);
        }
    }
</script>
</html>
