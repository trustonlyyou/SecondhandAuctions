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
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>


    <title>로그인 | 중고 경매의 세계</title>

</head>
<body>
<%@include file="../includes/header.jsp"%>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <h2 class="card-title text-center">로그인</h2>
                    <hr>
                    <br>

                    <form class="form-signin">
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
                            <input type="button" id="loginAjax" class="btn btn-dark btn-lg btn-block" value="로그인" disabled>

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
    var loginCount = 5;

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
            $("#loginAjax").attr('disabled', false);
        }
    }

    $("#loginAjax").on("click", function (e) {

        var memberId = $("#memberId").val();
        var memberPassword = $("#memberPassword").val();

        var formData = {
            memberId : memberId,
            memberPassword : memberPassword
        }

       $.ajax({
           url: '/member/login/submit',
           type: 'post',
           data: formData,

           success: function (data) {
               if (data.result === 1) {
                   window.location.replace("/"); // replace 로 처리하면 뒤로가기가 막힌다.
               } else {
                   window.alert("아이디 또는 비밀번호가 틀립니다. 남은 로그인 횟수");
                   $("#memberPassword").val("");
                   $("#loginAjax").attr('disabled', true);
               }
           },
           error: function (request,status,error) {
               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
           }
       });
    });
</script>
</html>
