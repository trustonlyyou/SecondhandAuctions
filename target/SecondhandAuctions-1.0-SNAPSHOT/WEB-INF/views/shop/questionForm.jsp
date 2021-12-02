<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/09/13
  Time: 10:50 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

    <title>상품 판매 | 중고 경매의 세계</title>

    <style>
        .filebox input[type="file"] {
            position: absolute;
            width: 0;
            height: 0;
            padding: 0;
            overflow: hidden;
            border: 0;
        }

        .filebox label {
            display: inline-block;
            padding: 10px 20px;
            color: black;
            vertical-align: middle;
            background-color: #a6b3cd;
            cursor: pointer;
            border: 1px solid #ebebeb;
            border-radius: 5px;
        }


        #att_zone{
            width: 660px;
            min-height:150px;
            padding:10px;
            border:1px dotted #00f;
        }
        #att_zone:empty:before{
            content : attr(data-placeholder);
            color : #999;
            font-size:.9em;
        }

        .product_textarea {
            resize: none;
        }
    </style>
</head>
<body>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body">
                    <br>
                    <a href="/">메인 페이지</a>
                    <h2 class="card-title text-center">문의 사항 등록</h2>
                    <!-- Form 시작 -->
                    <form class="form-signin">
                        <br>
                        <div class="form-label-group">
                            작성자 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" class="form-control" value="${sessionScope.member}" readonly>
                        </div>
                        <br>

                        <div class="form-label-group">
                            제목 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="questionTitle" name="questionTitle" class="form-control"
                                   placeholder="상품의 제목을 입력해주세요." minlength="5" maxlength="20">
                        </div>
                        <div name="titleCheckMsg" id="titleCheckMsg"></div>
                        <br>

                        <div class="form-label-group">
                            내용 <img src="/resources/image/check.gif" alt="필수 입력사항"><br>

                            <textarea name="questionContent" id="questionContent" cols="30" rows="10" class="product_textarea form-control"></textarea>
                        </div>
                        <div name="contentCheckMsg" id="contentCheckMsg"></div>
                        <br>


                        <br>

                        <br>

                        <hr class="my-4">
                    </form>
                    <br>
                    <input type="button" class="btn btn-lg btn-primary btn-block text-uppercase"
                           value="문의 사항 등록" id="registerSubmit" name="registerSubmit" disabled>
                </div>
            </div>
        </div>

    </div>
</div>


<hr>
</body>

<script>


    $("#registerSubmit").on('click', function () {
        var questionTitle = $("#questionTitle").val();
        var questionContent = $("#questionContent").val();
        var productId = ${targetProductId};
        var prevPageUrl = "${prevPageUrl}";

        var formData = {
            questionTitle : questionTitle,
            questionContent : questionContent,
            productId : productId
        }

        $.ajax({
            url: '/shop/question/register',
            type: 'post',
            data: formData,

            success: function (result) {
                var check = result.check;

                switch (check) {
                    case 0 :
                        window.alert("질문은 다시 등록해 주세요.");
                        window.location.replace("/shop/question/form?productId=${targetProductId}");
                        break;
                    case 1 :
                        window.alert("작성이 완료 되었습니다.");
                        window.location.replace(prevPageUrl);
                        break;
                }

                window.alert("product register result :: " + check);

            }
        });
    });


    // ================================================================================
    var questionTitleFlag = false;
    var questionContentFlag = false;


    $(document).ready(function (e) {

        $("#questionContent").on('keyup', function () {
            var questionContent = $("#questionContent").val();

            if (questionContent == "") {
                $("#contentCheckMsg").text('내용은 필수 입력 사항 입니다.');
                $("#contentCheckMsg").css('color', 'red');

                questionContentFlag = false;

                return false;
            }

            questionContentFlag = true;

            $("#contentCheckMsg").text('');

            if ((questionTitleFlag && questionContentFlag) == true) {
                $("#registerSubmit").attr('disabled', false);
            }

            return true;

        });
    });

    $("#questionTitle").on('keyup', function () {
        var questionTitle = $("#questionTitle").val();

        if (questionTitle == "") {
            $("#titleCheckMsg").text('제목은 필수 입력 사항 입니다.');
            $("#titleCheckMsg").css('color', 'red');

            questionTitleFlag = false;

            return false;
        }

        questionTitleFlag = true;

        $("#titleCheckMsg").text('');

        if ((questionTitleFlag && questionContentFlag) == true) {
            $("#registerSubmit").attr('disabled', false);
        }

        return true;
    });

</script>
</html>

