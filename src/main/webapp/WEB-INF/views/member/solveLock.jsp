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
    <title>로그인 잠금 풀기 | 중고 경매의 세계</title>
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
                    <h2 class="card-title text-center">로그인 잠금 풀기</h2>
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
                            <input type="text" id="memberId" name="memberId" class="form-control"
                                   placeholder="아이디를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm" maxlength="20">
                        </div>
                        <div name="IdCheckMsg" id="IdCheckMsg"></div>

                        <br>

                        <div class="input-group input-group-lg">
                            <input type="text" id="memberName" name="memberName" class="form-control"
                                   placeholder="이름를 입력해주세요." aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                        </div>
                        <div name="nameCheckMsg" id="nameCheckMsg" class="check_font"></div>

                        <br>

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

                        <br>
                        <br>

                        <div>
                            <input type="button" id="solveLockFromPhone" name="solveLockFromPhone"
                                   class="btn btn-dark btn-lg btn-block" value="잠금풀기" disabled>
                        </div>
                    </form>


                    <!-- ====================이메일 인증==================  -->
                    <form id="emailForm">
                        <div class="input-group input-group-lg">
                            <input type="text" id="memberIdEmail" name="memberIdEmail" class="form-control"
                                   placeholder="아이디를 입력해주세요." aria-label="Large"
                                   aria-describedby="inputGroup-sizing-sm" maxlength="20">
                        </div>
                        <div name="IdCheckMsgEmail" id="IdCheckMsgEmail"></div>

                        <br>

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

                        <div>
                            <input type="button" id="solveLockFromEmail" name="solveLockFromEmail"
                                   class="btn btn-dark btn-lg btn-block" value="잠금풀기" disabled>
                        </div>

                    </form>


                    <br>
                    <hr>

                    <br>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script>

    var code = ""; // 이메일 코드 저장


    // 핸드폰 인증
    $("#checkPhone").click(function () {

        $.ajax({
            url: '/member/check/phone/sendSms',
            type: 'POST',
            data: $("form").serialize(),

            success: function (data) {
                var checkKey = data.key;

                $("#phoneInputNumCheck").attr('disabled', false);

                $("#phoneInputNumCheck").click(function () {
                    var inputNum = $("#phoneInputNum").val();

                    if (inputNum === checkKey) {
                        window.alert('인증번호가 일치합니다.');
                        $("#solveLockFromPhone").attr('disabled', false);
                    } else {
                        window.alert('인증번호가 틀립니다.');
                        $("#solveLockFromPhone").attr('disabled', true);
                    }
                });
            }
        });
    });

    // 핸드폰
    $("#solveLockFromPhone").on("click", function () {
        var memberId = $("#memberId").val();
        var memberName = $("#memberName").val();
        var memberPhone = $("#memberPhone").val();

        var formData = {
            memberId : memberId,
            memberName : memberName,
            memberPhone : memberPhone
        }

        $.ajax({
            url: '/member/lock/solve/phone',
            type: 'post',
            data: formData,

            success: function (data) {
                var chk = data.result;

                if (chk === false) {
                    window.alert("입력하신 정보의 회원의 정보가 없습니다.");
                    location.reload();
                } else {
                    window.alert("비밀번호를 바꿔주세요.");
                    window.location.replace("/member/password/modify/form");
                }
            }
        });

    });

    //========================= 이메일로 패드워드 찾기 =========================

    $("#checkEmail").click(function () {

        var email = $("#memberEmail").val();

        $("#emailCheckMsg").text("메일로 인증번호 전송 중 입니다. 잠시만 기다려주세요.");
        $("#checkEmail").attr('disabled', true);


        $.ajax({
            url: '/member/sendEmail',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: email,

            success: function (data) {
                $("#input_mail").attr("disabled", true);
                code = data.num;

                console.log(data.num);

                $("#emailCheckMsg").text("메일로 인증번호가 전송되었습니다. 메일은 확인해주세요.")
                $("#emailCheckMsg").css('color','green');
            },

            error: function (error) {
                $("#emailCheckMsg").text("다시요청해주세요.");
                $("#checkEmail").attr('disabled', false);
            }
        });
    });

    $("#solveLockFromEmail").on("click", function () {
        $.ajax({
            url: '/member/lock/solve/email',
            type: 'post',
            data: $("form").serialize(),

            success: function (data) {
                var chk = data.result;

                if (chk === false) {
                    window.alert("입력하신 정보의 회원의 정보가 없습니다.");
                    location.reload();
                } else {
                    window.alert("비밀번호를 바꿔주세요.");
                    window.location.replace("/member/password/modify/form");
                }
            },
            error: function (request,status,error) {
                window.alert("error");
            }
        });
    });

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


        var idFlag = false;
        var nameFlag = false;
        var phoneFlag = false;

        var idEmailFlag = false;
        var emailFlag = false;


        $("#memberId").on('keyup', function () {
            var id = $("#memberId").val();
            var isID = /^[a-z0-9]{4,19}$/;


            if (id == "") {
                $("#IdCheckMsg").text('아이디는 필수 입력입니다.');
                $("#IdCheckMsg").css('color', 'red');

                idFlag = false;

                return false;
            }

            if (!isID.test(id)) {
                $("#IdCheckMsg").text('5~20자의 영문 소문자, 숫자만 사용 가능합니다.');
                $("#IdCheckMsg").css('color', 'red');

                idFlag = false;

                return false;

            } else {

                $("#IdCheckMsg").text("");

                idFlag = true;

                if ((nameFlag && phoneFlag && idFlag) == true) {
                    $("#checkPhone").attr('disabled', false);
                }

                return true;
            }
        });

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

                if ((nameFlag && phoneFlag && idFlag) == true) {
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

            } else {

                $("#phoneCheckMsg").text("");

                phoneFlag = true;

                if ((nameFlag && phoneFlag && idFlag) == true) {
                    $("#checkPhone").attr('disabled', false);
                }

                return true;

            }
        });


        // ====================================== 이메일로 인증 ======================================
        $("#memberIdEmail").on('keyup', function () {
            var id = $("#memberIdEmail").val();
            var isID = /^[a-z0-9]{4,19}$/;

            if (id == "") {
                $("#IdCheckMsgEmail").text('아이디는 필수 입력입니다.');
                $("#IdCheckMsgEmail").css('color', 'red');

                idEmailFlag = false;

                return false;
            }

            if (!isID.test(id)) {
                $("#IdCheckMsgEmail").text('5~20자의 영문 소문자, 숫자만 사용 가능합니다.');
                $("#IdCheckMsgEmail").css('color', 'red');

                idEmailFlag = false;

                return false;

            } else {

                $("#IdCheckMsgEmail").text("");

                idEmailFlag = true;

                if ((idEmailFlag && emailFlag) == true) {
                    $("#searchIdSubmitEmail").attr('disabled', false);
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
                $("#checkEmail").attr('disabled', false);

                return true
            }

        });

        $("#emailInputNum").on('keyup', function () {
            $("#emailInputNumCheck").attr('disabled', false);

        });

        $("#emailInputNumCheck").click(function () {
            var inputCode = $("#emailInputNum").val();

            if (inputCode != code) {
                window.alert("인증번호가 일치하지 않습니다.");

                emailFlag = false;

                return false;
            } else {
                window.alert("인증번호가 일치합니다.");

                emailFlag = true;

                if ((idEmailFlag && emailFlag) == true) {
                    $("#solveLockFromEmail").attr('disabled', false);//
                }
            }
        });


        $("#checkPhone").click(function () {
            $("#phoneCheckForm").show();
        });

        $("#inputNum").on('keyup', function () {

            $("#checkResult").attr('disabled', false);

        });

    });
</script>

</html>