<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/01
  Time: 1:10 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>회원가입 | 중고 경매의 세계</title>
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>

<body>

<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body">
<%--                    <button type="button" class="btn btn-link">이전 페이지</button>--%>
                    <a href="/">메인 페이지</a>
                    <h2 class="card-title text-center">회원가입</h2>

                    <!-- Form 시작 -->
                    <form class="form-signin" action="/member/join/action" id="join" name="join" method="post"
                          name="joinform">
                        <div class="form-label-group">
                            아이디<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="memberId" name="memberId" class="form-control"
                                   placeholder="아이디 입력(5~11자)" minlength="5" maxlength="20">
                        </div>
                        <div>
                            <input type="button" id="idCheck" name="idCheck" value="중복확인"><br>
                        </div>
                        <div name="IdCheckMsg" id="IdCheckMsg"></div>
                        <br>
                        <!-- 아이디 중복 체크 ajax -->


                        <div class="form-label-group">
                            비밀번호<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="password" id="memberPassword" name="memberPassword" class="form-control"
                                   placeholder="Password" required>
                        </div>
                        <div name="pwd1CheckMsg" id="pwd1CheckMsg" class="check_font"></div>

                        <br>

                        <div class="form-label-group">
                            비밀번호 재확인<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="password" id="memberPasswordCheck" name="memberPasswordCheck"
                                   class="form-control" placeholder="비밀번호 재확인" required>
                        </div>
                        <div name="pwd2CheckMsg" id="pwd2CheckMsg" class="check_font"></div>

                        <br>

                        <div class="form-label-group">
                            이름<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="memberName" name="memberName" class="form-control"
                                   placeholder="name" required>
                        </div>
                        <div name="nameCheckMsg" id="nameCheckMsg" class="check_font"></div>

                        <br>

                        <div class="form-label-group">
                            이메일<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="email" name="memberEmail" id="memberEmail" class="form-control"
                                   placeholder="email">
                        </div>
                        <div name="emailCheckMsg" id="emailCheckMsg" class="check_font"></div>
                        <br>
                        <div>
                            인증번호 입력 <input type="text" id="input_mail" name="input_mail" disabled="disabled">
                            <input type="button" id="mailCheck" name="mailCheck" value="인증번호 전송" disabled><br>
                        </div>
                        <div id="mail_check" name="mail_check" class="check_font"></div>

                        <br>

                        <div class="form-label-group">
                            핸드폰<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="memberPhone" name="memberPhone" class="form-control"
                                   placeholder="특수기호 제외하고 숫자면 입력해주세요. 예) 01068300772" required>
                        </div>
                        <div name="phoneCheckMsg" id="phoneCheckMsg" class="check_font"></div>
                        <br>
                        <div id="phoneCheckForm" class="form-label-group">
                            인증번호 입력 <input type="text" id="phoneInputNum" name="phoneInputNum" disabled="disabled">
                            <input type="button" id="phoneInputNumCheck" name="phoneInputNumCheck" value="인증번호 전송" disabled><br>
                        </div>
                        <div id="phone_check" name="phone_check" class="check_font"></div>


                        <br>
                        <input type="submit" class="btn btn-lg btn-primary btn-block text-uppercase"
                               value="동의하고 회원가입" id="join_submit" name="join_submit" disabled>

                        <a class="d-block text-center mt-2 small" href="login_form"></a>
                        <hr class="my-4">

                        <div>
                            <input type="button" id="test" name="test" value="test">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<hr>


</body>
<script>

    var code = ""; // 이메일 코드 저장

    var idFlag = false;
    var passwordFlag = false;
    var nameFlag = false;
    var emailFlag = false;
    var phoneFlag = false;


    // 카운트 다운
    var num = 60 * 3; // 몇분을 설정할지의 대한 변수 선언
    var myVar;
    var check = false;


     // 아이디 중복 확인
     $("#idCheck").click(function () {
         var id = $("#memberId").val();

         $.ajax({
             url: '/member/join/idCheck',
             type: 'POST',
             dataType: 'json',
             contentType: 'application/json; charset=UTF-8',
             data: id,

             success: function (data) {
                 if (data == 0) {
                     $("#IdCheckMsg").text("사용 가능한 아이디 입니다.");
                     $("#IdCheckMsg").css('color', 'green');

                     idFlag = true;

                     if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                         $("#join_submit").attr('disabled', false);
                     }


                 } else {
                     $("#IdCheckMsg").text("이미 존재하는 아이디 입니다.");
                     $("#IdCheckMsg").css('color','red');

                     nameFlag = false;
                 }
             },

             error: function (error) {
                 console.log("error :: " + error);
                 alert(error);
             }
         });
     });

    // 이메일 체크
    $("#mailCheck").click(function () {

        var email = $("#memberEmail").val();

        $.ajax({
            url: '/member/emailCheck',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: email,

            success: function (data) {

                if (data === 0) {
                    $("#mailCheck").attr("disabled", true);
                    $("#emailCheckMsg").text("메일 전송 중입니다. 잠시만 기다려주세요.");


                    $.ajax({
                        // url: "/join/emailCheck?email" + email,
                        url: '/member/sendEmail',
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8',
                        data: email,

                        success: function (data) {
                            $("#input_mail").attr("disabled", false);
                            code = data;

                            $("#emailCheckMsg").text("메일로 인증번호가 전송되었습니다. 메일은 확인해주세요.")
                        },

                        error: function (request,status,error) {
                            alert("error!!!");
                            $("#mailCheck").attr("disabled", false);
                            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);

                        }
                    });
                } else {
                    alert("이미 사용 중인 이메일 입니다.");
                }
            },
            error: function (request,status,error) {
                alert("ajax error!!!");
                $("#mailCheck").attr("disabled", false);
                console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);

            }
        });

        // $("#mailCheck").attr("disabled", true);
        // $("#emailCheckMsg").text("메일 전송 중입니다. 잠시만 기다려주세요.");
        //
        //
        // $.ajax({
        //     // url: "/join/emailCheck?email" + email,
        //     url: '/member/sendEmail',
        //     type: 'POST',
        //     dataType: 'json',
        //     contentType: 'application/json; charset=UTF-8',
        //     data: email,
        //
        //     success: function (data) {
        //         $("#input_mail").attr("disabled", false);
        //         code = data;
        //
        //         $("#emailCheckMsg").text("메일로 인증번호가 전송되었습니다. 메일은 확인해주세요.")
        //     },
        //
        //     error: function (request,status,error) {
        //         alert("이메일 인증을 다시 시도해 주세요.");
        //         $("#mailCheck").attr("disabled", false);
        //         console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        //
        //     }
        // });

    });

    var phoneCheckKey;

    $("#test").click(function () {
        var memberName = $("#memberName").val();
        var memberPhone = $("#memberPhone").val();

        var formData = {
            memberName : memberName,
            memberPhone : memberPhone
        }

        $.ajax({
            url: '/member/phoneCheck',
            type: 'post',
            // data: $("form").serialize(),
            data: formData,

            success: function (data) {
                alert(data);
            },
            error: function (request,status,error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error)
            }
        });
    });

    // 핸드폰 체크
    $("#phoneInputNumCheck").click(function () {
        var memberName = $("#memberName").val();
        var memberPhone = $("#memberPhone").val();

        var isName = /^[가-힣]+$/;

        if ((memberName == "") || (memberPhone == "")) {
            alert("이름과 핸드폰 번호를 입력해주세요.");
            return false;
        }

        if (!isName.test(memberName)) {
            alert("이름의 입력형식이 옳바르지 않습니다.");
            return false
        }

        var formData = {
            memberName : memberName,
            memberPhone : memberPhone
        }

        $.ajax({
            url: '/member/phoneCheck',
            type: 'post',
            data: formData,

            success: function (data) {
                if (data === 0) {
                    $("#phoneCheckMsg").text("인증번호가 전송 중 입니다. 잠시만 기다려주세요.");
                    $("#phoneInputNumCheck").attr("disabled", true);

                    $.ajax({
                        url: '/member/check/phone/sendSms',
                        type: 'POST',
                        data: $("form").serialize(),

                        success: function (data) {
                            phoneCheckKey = data.key;

                            $("#phoneCheckMsg").text("휴대폰에 인증번호가 전송되었습니다. 인증번호를 입력해주세요.");

                            console.log("key :: " + phoneCheckKey);

                            $("#phoneInputNum").attr('disabled', false);


                            // $("#phoneInputNumCheck").click(function () {
                            //     var inputNum = $("#phoneInputNum").val();
                            //
                            //     if (inputNum === checkKey) {
                            //         alert('인증번호가 일치합니다.');
                            //         $("#searchPwdSubmitPhone").attr('disabled', false);
                            //     } else {
                            //         alert('인증번호가 틀립니다.');
                            //         $("#searchPwdSubmitPhone").attr('disabled', true);
                            //     }
                            // });
                        },
                        error: function (request,status,error) {
                            alert("핸드폰 인증을 다시 시도해주세요.");
                            $("#phoneInputNumCheck").attr("disabled", true);
                            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                        }
                    });
                } else {
                    alert("이미 회원가입이 되어있는 핸드폰 번호입니다.");
                }
            },
            error: function (request,status,error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error)
            }
        });
    });

    $("#phoneInputNum").on('keyup', function () {
        var inputNum = $("#phoneInputNum").val();

        if (inputNum === phoneCheckKey) {
            $("#phone_check").text('인증번호가 일치합니다.');
            $("#phone_check").css('color', 'green');

            phoneFlag = true;

            if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                $("#join_submit").attr('disabled', false);
            }

            return true;

        } else {
            $("#phone_check").text('인증번호가 틀립니다.');
            $("#phone_check").css('color', 'red');

            phoneFlag = false;

            if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                $("#join_submit").attr('disabled', true);
            }

            return false;

        }
    });





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
            $("#IdCheckMsg").text('중복검사를 실시 해주세요.');
            $("#IdCheckMsg").css('color', 'red');

            return true;
        }
    });

    function checkSpace(str) {
        if (str.search(/\s/) != -1) {
            return true;
        } else {
            return false;
        }
    }

    function isValidationPwd(str) {
        var cnt = 0; // 중복된 숫자 걸러내기
        var isPW = /^[A-Za-z0-9`\-=\\\[\];',\./~!@#\$%\^&\*\(\)_\+|\{\}:"<>\?]{8,16}$/;

        if (str == "") {
            return false;
        }

        var retVal = checkSpace(str);

        if (retVal) {
            return false;
        }

        if (str.length < 8) {
            return false;
        }

        // 중복된 숫자 걸러 내기
        for (var i = 0; i < str.length; ++i) {
            if (str.charAt(0) == str.substring(i, i + 1))
                ++cnt;
        }
        if (cnt == str.length) {
            return false;
        }

        if (!isPW.test(str)) {
            return false;
        }

        return true;
    }

    $("#memberPassword").on('keyup', function () {
        var pwd1 = $("#memberPassword").val();

        if (pwd1 == "") {
            $("#pwd1CheckMsg").text('비밀번호는 필수 입력입니다.');
            $("#pwd1CheckMsg").css('color', 'red');

            passwordFlag = false;

            return false;
        }

        if (isValidationPwd(pwd1) == false) {
            $("#pwd1CheckMsg").text("8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
            $("#pwd1CheckMsg").css('color', 'red');

            passwordFlag = false;

            return false;

        } else {
            $("#pwd1CheckMsg").text("안전한 비밀번호입니다.");
            $("#pwd1CheckMsg").css('color', 'green');

            return true;
        }
    });

    // pwd2CheckMsg
    $("#memberPasswordCheck").on('keyup', function () {
        var pwd1 = $("#memberPassword").val();
        var pwd2 = $("#memberPasswordCheck").val();

        if (pwd2 == "") {
            $("#pwd2CheckMsg").text('비밀번호는 필수 입력입니다.');
            $("#pwd2CheckMsg").css('color', 'red');

            passwordFlag = false;

            return false;
        }

        if (pwd1 !== pwd2) {
            $("#pwd2CheckMsg").text("비밀번호가 일치하지 않습니다.");
            $("#pwd2CheckMsg").css('color', 'red');

            passwordFlag = false;

            return false;
        } else {
            $("#pwd2CheckMsg").text("비밀번호가 일치합니다.");
            $("#pwd2CheckMsg").css('color', 'green');

            passwordFlag = true;

            if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                $("#join_submit").attr('disabled', false);
            }

            return true;
        }
    });

    // name!!
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

            if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                $("#join_submit").attr('disabled', false);
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
            $("#mailCheck").attr('disabled', true);

            emailFlag = false;

            return false;
        }

        if (!isEmail.test(email)) {
            $("#emailCheckMsg").text("옳바르지 않은 이메일 형식 입니다.");
            $("#emailCheckMsg").css('color', 'red');
            $("#mailCheck").attr('disabled', true);

            emailFlag = false;

            return false;

        } else {
            $("#emailCheckMsg").text("이메일 인증을 해주세요.");
            $("#emailCheckMsg").css('color', 'red');
            $("#mailCheck").attr('disabled', false);

            return true
        }

    });


    $("#input_mail").on('keyup', function () {
        var inputCode = $("#input_mail").val();

        console.log("input_mailCheck :: " + code);

            if (inputCode != code) {
                $("#mail_check").text("불일치 합니다.");
                $("#mail_check").css('color', 'red');
                $("#emailCheckMsg").text("이메일 인증을 해주세요.");

                emailFlag = false;

                if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                    $("#join_submit").attr('disabled', true);
                }

                return false;
            } else {
                $("#mail_check").text("일치합니다.");
                $("#mail_check").css('color', 'green');
                $("#emailCheckMsg").text("");

                emailFlag = true;

                if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
                    $("#join_submit").attr('disabled', false);
                }

                return false;
            }
    });

    $("#memberPhone").on('keyup', function () {
        var phone = $("#memberPhone").val();
        var isPhone = /^((01[1|6|7|8|9])[1-9][0-9]{6,7})$|(010[1-9][0-9]{7})$/;

        if (phone == "") {
            $("#phoneCheckMsg").text("핸드폰은 필수 입력입니다.");
            $("#phoneCheckMsg").css('color', 'red');
            $("#phoneInputNumCheck").attr('disabled', true);

            phoneFlag = false;

            return false;
        }

        if (!isPhone.test(phone)) {
            $("#phoneCheckMsg").text("핸드폰 입력형식이 옳바르지 않습니다.");
            $("#phoneCheckMsg").css('color', 'red');
            $("#phoneInputNumCheck").attr('disabled', true);

            phoneFlag = false;

            return false;

        } else { // Ajax Phone Check (중복확인)

            // phone = phone.replace(/ /gi, "").replace(/-/gi, "");
            // $("#phone").val(phone);

            $("#phoneCheckMsg").text("핸드폰 인증을 해주세요.");
            $("#phoneCheckMsg").css('color', 'red');
            $("#phoneInputNumCheck").attr('disabled', false);

            // phoneFlag = true;
            //
            // if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
            //     $("#join_submit").attr('disabled', false);
            // }

            return true;

        }
    });

    // $.fn.flagCheck = function () {
    //     if ((idFlag && passwordFlag && nameFlag && emailFlag && phoneFlag) == true) {
    //         $("#join_submit").attr('disabled', false);
    //     }
    // };
    //
    // $(document).ready(function () {
    //    $.fn.flagCheck();
    // });

</script>

</html>
