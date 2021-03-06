<%--
  Created by IntelliJ IDEA.
  User: junghwan화
  Date: 2021/08/31
  Time: 1:41 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="UTF-8">
    <title>비밀번호 변경 본인 인증 | 중고 경매의 세계</title>
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
                    <h2 class="card-title text-center">본인 인증</h2>
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

                    <!-- ================== 핸드폰 인증 ================== -->
                    <form class="form-signin" id="phoneForm">
                        <div class="input-group input-group-lg">
                            <input type="text" id="memberPhone" name="memberPhone" class="form-control"
                                   placeholder="핸드폰 번호를 입력해주세요." maxlength="11" aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="checkPhone" name="checkPhone" value="인증요청" disabled>
                        </div>
                        <div name="phoneCheckMsg" id="phoneCheckMsg" class="check_font"></div>

                        <br>

                        <div id="phoneCheckForm" class="input-group input-group-lg">
                            <input type="text" id="phoneInputNum" name="inputNum" placeholder="인증번호를 입력해주세요."
                                   class="form-control" aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="phoneInputNumCheck" name="phoneInputNumCheck" value="확인"
                                   disabled>
                        </div>

                    </form>


                    <!-- ====================이메일 인증==================  -->
                    <form id="emailForm">
                        <div class="input-group input-group-lg">
                            <input type="text" id="memberEmail" name="memberEmail" class="form-control"
                                   placeholder="이메일 번호를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="checkEmail" name="checkEmail" value="인증요청" disabled>
                        </div>
                        <div name="emailCheckMsg" id="emailCheckMsg" class="check_font"></div>
                        <br>

                        <div id="emailCheckForm" class="input-group input-group-lg">
                            <input type="text" id="emailInputNum" name="emailInputNum" placeholder="인증번호를 입력해주세요."
                                   class="form-control" aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                            <input type="button" id="emailInputNumCheck" name="emailInputNumCheck" value="확인"
                                   disabled>
                        </div>


                        <br>
                        <br>

<%--                        <div>--%>
<%--                            <input type="button" id="searchPwdSubmitEmail" name="searchPwdSubmitEmail"--%>
<%--                                   class="btn btn-dark btn-lg btn-block" value="비밀번호 찾기" disabled>--%>
<%--                        </div>--%>

                    </form>


                    <br>


<%--                    <span>--%>
<%--                            <a href="/member/login/form">로그인</a>&nbsp;--%>
<%--                        </span>--%>

<%--                    <span>--%>
<%--                            <a href="/member/search/id">비밀번호 찾기</a>--%>
<%--                        </span>--%>

<%--                    <hr>--%>

<%--                    <br>--%>


<%--                    회원이 아니시라면 회원 가입을 해주세요. &nbsp; <a href="/member/join/form">회원가입</a>--%>

                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script>

    var code = ""; // 이메일 코드 저장

    $(document).ready(function () {

        var phoneFlag = false;
        var emailFlag = false;

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

            } else {

                $("#phoneCheckMsg").text("");
                $("#checkPhone").attr('disabled', false);

                phoneFlag = true;

                return true;
            }
        });

        // 핸드폰 인증
        $("#checkPhone").click(function () {
            $("#phoneCheckForm").show();

            var memberPhone = $("#memberPhone").val();

            $.ajax({
                url: '/certification/phone',
                type: 'POST',
                data: memberPhone,

                success: function (data) {
                    var checkKey = data.key;

                    $("#phoneInputNumCheck").attr('disabled', false);

                    $("#phoneInputNumCheck").click(function () {
                        var inputNum = $("#phoneInputNum").val();

                        if (inputNum === checkKey) {
                            window.alert('인증번호가 일치합니다.');
                            window.location.replace("/member/password/modify/form")
                        } else {
                            window.alert('인증번호가 틀립니다.');
                            $("#phoneInputNum").val("");
                        }
                    });
                }
            });

        });



        // ====================================== 이메일로 인증 ======================================

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
                $("#checkEmail").attr('disabled', false);

                return true
            }

        });

        $("#checkEmail").click(function () {

            var email = $("#memberEmail").val();

            $("#emailCheckMsg").text("메일로 인증번호 전송 중 입니다. 잠시만 기다려주세요.");
            $("#checkEmail").attr('disabled', true);


            $.ajax({
                url: '/certification/email',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: email,

                success: function (data) {
                    code = data.num;

                    if (code === 0) {
                        window.location.replace("다시 시도해 주세요.");
                    }

                    $("#input_mail").attr("disabled", true);

                    $("#emailCheckMsg").text("메일로 인증번호가 전송되었습니다. 메일은 확인해주세요.")
                    $("#emailCheckMsg").css('color','green');
                },

                error: function (error) {
                    $("#emailCheckMsg").text("다시요청해주세요.");
                    $("#checkEmail").attr('disabled', false);
                }
            });
        });

        $("#emailInputNum").on('keyup', function () {
            $("#emailInputNumCheck").attr('disabled', false);

        });


        $("#emailInputNumCheck").click(function () {
            var inputCode = $("#emailInputNum").val();

            if (inputCode != code) {
                window.alert("인증번호가 일치하지 않습니다.");
                return false;
            } else {
                window.alert("인증번호가 일치합니다.");
                window.location.replace("/member/password/modify/form");
            }
        });

    });
</script>

</html>