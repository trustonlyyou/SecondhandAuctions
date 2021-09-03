<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/20
  Time: 1:01 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기 | 중고 경매의 세계</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <div class="form-row float-left">
                        <a href="/">이전 페이지</a>
                    </div>
                    <br>
                    <h2 class="card-title text-center">아이디 찾기</h2>
                    <hr>
                    <br>

                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="type" id="phoneCheck" value="phone"
                               checked>
                        <label class="form-check-label" for="phoneCheck">휴대폰 인증</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="type" id="emailCheck" value="email">
                        <label class="form-check-label" for="emailCheck">이메일 인증</label>
                    </div>

                    <br>
                    <br>

                    <!-- 핸드폰 인증 -->
                    <form class="form-signin" id="phoneForm" action="/member/search/id/phone/action" method="post">
                        <div class="input-group input-group-lg">
                            <input type="text" id="memberName" name="memberName" class="form-control"
                                   placeholder="이름를 입력해주세요." aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                        </div>
                        <div name="nameCheckMsg" id="nameCheckMsg" class="check_font"></div>

                        <br>

                        <div class="input-group input-group-lg">
                            <input type="text" id="memberPhone" name="memberPhone" class="form-control" placeholder="핸드폰 번호를 입력해주세요." maxlength="11" aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="checkPhone" name="checkPhone" value="인증요청" disabled>
                        </div>
                        <div name="phoneCheckMsg" id="phoneCheckMsg" class="check_font"></div>

                        <br>

                        <div id="phoneCheckForm" class="input-group input-group-lg">
                            <input type="text" id="inputNum" name="inputNum" placeholder="인증번호를 입력해주세요." class="form-control" aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="checkResult" name="checkPhone" value="확인" disabled>
                        </div>

                        <br>
                        <br>

                        <div>
                            <input type="submit" id="searchIdSubmitPhone" name="searchIdSubmitPhone"
                                   class="btn btn-dark btn-lg btn-block" value="아이디 찾기" disabled>
                        </div>
                    </form>


                    <!-- 이메일 인증  -->
                    <form action="/member/search/id/email/action" method="post" id="emailForm">
                        <div class="input-group input-group-lg">
                            <input type="text" id="memberEmail" name="memberEmail" class="form-control"
                                   placeholder="이메일 번호를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                        </div>
                        <div name="emailCheckMsg" id="emailCheckMsg" class="check_font"></div>
                        <br>


                        <br>
                        <br>

                        <div>
                            <input type="submit" id="searchIdSubmitEmail" name="searchIdSubmitEmail"
                                   class="btn btn-dark btn-lg btn-block" value="아이디 찾기" disabled>
                        </div>

                    </form>


                    <br>

                    <span>
                                <a href="/member/login/form">로그인</a>&nbsp;
                            </span>
                    <span>
                                <a href="">비밀번호 찾기</a>
                            </span>

                    <hr>

                    <br>


                    회원이 아니시라면 회원 가입을 해주세요. &nbsp; <a href="/member/join/form">회원가입</a>

                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script>
    $(document).ready(function () {
        $("#emailForm").hide();
        $("#phoneCheckForm").hide();

        $("#phoneCheck").click(function () {
            $("#emailForm").hide();
            $("#phoneForm").show();
            $("input:radio[name='type']:radio[value='email']").prop("checked", false);
        });

        $("#emailCheck").click(function () {
            $("#phoneForm").hide();
            $("#emailForm").show();
            $("input:radio[name='type']:radio[value='phone']").prop("checked", false);
        });



        var nameFlag = false;
        var emailFlag = false;
        var phoneFlag = false;

        $("#memberName").on('keyup', function () {
            var memberName = $("#memberName").val();
            var isName = /^[가-힣]+$/;

            if (memberName == "") {
                $("#nameCheckMsg").text("이름은 필수입력 입니다.");
                $("#nameCheckMsg").css('color', 'red');

                nameFlag = false;

                return false;
            }

            if (!isName.test(memberName)) {
                $("#nameCheckMsg").text("한글만 입력 가능합니다.");
                $("#nameCheckMsg").css('color', 'red');

                nameFlag = false;

                return false;

            } else {
                $("#nameCheckMsg").text("");

                nameFlag = true;

                if ((nameFlag && phoneFlag) == true) {
                    $("#checkPhone").attr('disabled', false);
                }

                return true;
            }
        });

        $("#memberPhone").on('keyup', function () {
            var phone = $("#memberPhone").val();
            var isPhone = /^((01[1|6|7|8|9])[1-9][0-9]{6,7})$|(010[1-9][0-9]{7})$/;

            if (phone == "") {
                $("#phoneCheckMsg").text("핸드폰은 필수 입력입니다.");
                $("#phoneCheckMsg").css('color', 'red');

                phoneFlag = false;

                return false;
            }

            if (!isPhone.test(phone)) {
                $("#phoneCheckMsg").text("핸드폰 입력형식이 옳바르지 않습니다.");
                $("#phoneCheckMsg").css('color', 'red');

                phoneFlag = false;

                return false;

            } else { // Ajax Phone Check (중복확인)

                // phone = phone.replace(/ /gi, "").replace(/-/gi, "");
                // $("#phone").val(phone);

                $("#phoneCheckMsg").text("");

                phoneFlag = true;

                if ((nameFlag && phoneFlag) == true) {
                    $("#checkPhone").attr('disabled', false);
                }

                return true;

            }
        });

        $("#memberEmail").on('keyup', function () {
            var email = $("#memberEmail").val();
            var isEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

            if (email == "") {
                $("#emailCheckMsg").text("이메일은 필수입력 입니다.");
                $("#emailCheckMsg").css('color', 'red');

                emailFlag = false;

                return false;
            }

            if (!isEmail.test(email)) {
                $("#emailCheckMsg").text("옳바르지 않은 이메일 형식 입니다.");
                $("#emailCheckMsg").css('color', 'red');

                emailFlag = false;

                return false;

            } else {
                $("#emailCheckMsg").text("");

                emailFlag = true;

                if (emailFlag == true) {
                    $("#searchIdSubmitEmail").attr('disabled', false);
                }


                return true
            }

        });



        $("#checkPhone").click(function () {
            $("#phoneCheckForm").show();
        });

        $("#inputNum").on('keyup', function() {

            $("#checkResult").attr('disabled', false);

        });

    })

    $("#checkPhone").click(function () {

        $.ajax({
            url: '/member/check/phone/sendSms',
            type: 'POST',
            data: $("form").serialize(),

            success: function (data) {
                var checkKey = data.key;

                $("#checkResult").click(function () {
                    var inputNum = $("#inputNum").val();

                    if (inputNum === checkKey) {
                        alert('인증번호가 일치합니다.');
                        $("#searchIdSubmitPhone").attr('disabled', false);
                    } else {
                        alert('인증번호가 틀립니다.');
                        $("#searchIdSubmitPhone").attr('disabled', true);
                    }
                });
            }
        });
    });


    <%--$("#checkPhone").click(function () {--%>

    <%--    var inputNum = $("#inputNum").val();--%>

    <%--    $.ajax({--%>
    <%--        url: '/member/search/id/phone/sendSms',--%>
    <%--        type: 'POST',--%>
    <%--        dataType: 'json',--%>
    <%--        contentType: 'application/json; charset=UTF-8',--%>
    <%--        data: {key : ${key}},--%>

    <%--        success: function (data) {--%>
    <%--            var strNum = data;--%>

    <%--            if (inputNum == strNum) {--%>
    <%--                window.alert('인증번호가 일치합니다.');--%>
    <%--                $("#searchIdSubmit").attr('disabled', false);--%>
    <%--            } else {--%>
    <%--                window.alert('인증번호가 일치하지 않습니다. 인증번호를 확인해주세요.');--%>
    <%--                $("#searchIdSubmit").attr('disabled', true);--%>
    <%--            }--%>
    <%--        },--%>

    <%--        error: function (error) {--%>
    <%--            console.log("error :: " + error);--%>
    <%--            alert(error);--%>
    <%--        }--%>
    <%--    });--%>
    <%--});--%>


</script>
</html>
