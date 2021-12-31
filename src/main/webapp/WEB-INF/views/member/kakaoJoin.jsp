<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/12/27
  Time: 1:47 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기 결과 | 중고 경매의 세계</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="/resources/js/join.js"></script>
</head>
<body>
<%@include file="../includes/header.jsp"%>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <h2 class="card-title text-center">카카오 회원가입</h2>
                    <hr>
                    <br>

                    <form class="form-signin">
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="memberId" name="memberId" placeholder="사이트에서 사용할 아이디 입력해주세요.(5~11자)" minlength="5" maxlength="20"
                                   aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="idCheck" value="중복확인">
                        </div>
                        <div class="input-group" id="idMsg"></div>

                        <br>
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="memberPhone" name="memberPhone" placeholder="핸드폰 번호를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="phoneCheck" value="전송" disabled>
                        </div>
                        <div class="phoneInputCheckDiv input-group">
                            <input type="text" class="form-control" id="inputPhoneCheck" placeholder="인증번호를 입력해주세요.">
                            <input type="button" id="inputPhoneBtn" value="확인">
                        </div>
                        <div class="input-group" id="phoneMsg"></div>

                        <br>
                        <br>

                        <div>
                            <input type="button" id="joinRegister" class="btn btn-dark btn-block" value="회원가입" disabled>
                        </div>

                        <br>


                        <hr>

                        <br>


                        <br>
                        <br>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var idFlag = false;
    var phoneFlag = false;
    var phoneNum;

    $(document).ready(function () {
        $(".phoneInputCheckDiv").hide();
    });

    $("#memberId").on('keyup', function () {
        var memberId = $("#memberId").val();
        var isID = /^[a-z0-9]{4,19}$/;

        if (memberId === "") {
            $("#idMsg").css('color', 'red');
            $("#idMsg").text("아이디 입력은 필수 입니다.");

            idFlag = false;

            return false;
        }

        if (!isID.test(memberId)) {
            $("#idMsg").text('5~20자의 영문 소문자, 숫자만 사용 가능합니다.');
            $("#idMsg").css('color','red');

            idFlag = false;

            return false;
        } else {
            $("#idMsg").text('중복검사를 실시해 주세요.');
            $("#idMsg").css('color', 'red');

            return false;
        }
    });

    $("#idCheck").on("click", function () {
        var id = $("#memberId").val();

        $.ajax({
            url: '/member/join/idCheck',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: id,

            success: function (data) {
                var chk = data.result;

                if (chk === false) {
                    $("#idMsg").text('이미 사용중인 아이디 입니다.');
                    $("#idMsg").css('color', 'red');

                    idFlag = false;

                } else {
                    $("#idMsg").text('사용 가능한 아이디 입니다.');
                    $("#idMeg").css('color', 'green');

                    idFlag = true;

                    if (idFlag == true && phoneFlag == true) {
                        $("#joinRegister").attr('disabled', false);
                        return false;
                    }

                }
            },
            error: function (request,status,error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                location.reload();
            }
        });
    });

    $("#memberPhone").on('keyup', function () {
        var phone = $("#memberPhone").val();
        var isPhone = /^((01[1|6|7|8|9])[1-9][0-9]{6,7})$|(010[1-9][0-9]{7})$/;

        if (phone == "") {
            $("#phoneMsg").text("핸드폰은 필수 입력입니다.");
            $("#phoneCheckMsg").css('color','red');
            $("#joinRegister").attr('disabled', true);
            $("#phoneCheck").attr('disabled', true);

            phoneFlag = false;

            return false;
        }

        if (!isPhone.test(phone)) {
            $("#phoneMsg").text("핸드폰 입력형식이 옳바르지 않습니다.");
            $("#phoneCheckMsg").css('color','red');
            $("#joinRegister").attr('disabled', true);
            $("#phoneCheck").attr('disabled', true);

            phoneFlag = false;

            return false;
        } else {
            $("#phoneMsg").text("핸드폰 인증을 해주세요.");
            $("#phoneCheckMsg").css('color','red');
            $("#phoneCheck").attr('disabled', false);
            $("#joinRegister").attr('disabled', true);

            return true;
        }
    });

    $("#phoneCheck").on('click', function () {
        var memberPhone = $("#memberPhone").val();
        var memberName = "${memberName}";


        // 중복 확인
        $.ajax({
            url: '/member/phoneCheck',
            type: 'post',
            data: {
                memberPhone : memberPhone,
                memberName : memberName
            },

            success: function (data) {
                var chk = data.result;

                if (chk == false) {
                    $("#phoneMsg").text("이미 가입된 회원의 정보입니다.");
                    $("#phoneMsg").css('color', 'red');
                } else {
                    $("#phoneMsg").text("인증번호가 전송 중 입니다. 잠시만 기다려주세요.");
                    $("#phoneMsg").css('color', 'red');
                    $(".phoneInputCheckDiv").show();

                    // Send sms
                    $.ajax({
                        url: '/member/check/phone/sendSms',
                        type: 'post',
                        data: memberPhone,

                        success: function (data) {
                            var phoneCheckKey = data.key;

                            $("#phoneMsg").text("휴대폰에 인증번호가 전송되었습니다. 인증번호를 입력해주세요.");
                            $("#inputPhoneCheck").attr('disabled', false);

                            console.log("key :: " + phoneCheckKey);
                            $("#inputPhoneBtn").on('click', function () {
                                var inputNum = $("#inputPhoneCheck").val();

                                if (phoneCheckKey === inputNum) {
                                    $("#inputPhoneCheck").attr('disabled', true);
                                    $("#phoneMsg").text("인증번호가 일치합니다.");
                                    $("#phoneMsg").css('color','green');

                                    phoneFlag = true;

                                    if (idFlag == true && phoneFlag == true) {
                                        $("#joinRegister").attr('disabled', false);
                                    }

                                    return true;
                                } else {
                                    $("#phoneMsg").text("인증번호가 일치하지 않습니다.");
                                    $("#phoneMsg").css('color','red');
                                    $("#joinRegister").attr('disabled', true);

                                    phoneFlag = false;

                                    return false;
                                }
                            });
                        },
                        error: function (request,status,error) {
                            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                            location.reload();
                        }
                    });
                }
            }
        });
    });

    $("#joinRegister").on('click', function () {
        var memberId = $("#memberId").val();
        var memberPhone = $("#memberPhone").val();
        var memberName = "${memberName}";
        var memberEmail = "${memberEmail}";

        if ((idFlag == false) || (phoneFlag == false)) {
            alert("아이디 또는 핸드폰을 확인 하세요.");
        }

        $.ajax({
            url: '/member/kakao/join/submit',
            type: 'post',
            data: {
                memberId : memberId,
                memberPhone : memberPhone,
                memberName : memberName,
                memberEmail : memberEmail
            },

            success: function (data) {
                var result = data.result

                if (result == true) {
                    window.location.replace("/member/join/result")
                } else {
                    alert("다시 시도해 주세요.");
                    location.reload();
                }
            },
            error: function (request,status,error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                location.reload();
            }
        });
    });
</script>
</html>
