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

        .textarea {
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
                    <h2 class="card-title text-center">문의사항 답변 등록</h2>
                    <!-- Form 시작 -->
                    <form class="form-signin">
                        <h4>고객 질문</h4>
                        <br>
                        <div class="form-label-group">
                            질문 작성자 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" class="form-control" value='${qna.get("memberId")}' readonly>
                            <input type="hidden" id="questionId" value='${qna.get("questionId")}'>
                        </div>
                        <br>

                        <div class="form-label-group">
                            질문 제목 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="questionTitle" name="questionTitle" class="form-control"
                                   value='${qna.get("questionTitle")}' readonly>
                        </div>
                        <br>

                        <div class="form-label-group">
                            질문 내용 <img src="/resources/image/check.gif" alt="필수 입력사항"><br>

                            <textarea name="questionContent" id="questionContent" cols="10" rows="10" class="textarea form-control" readonly>
                                <c:out value='${qna.get("questionContent")}'/>
                            </textarea>
                        </div>


                        <br>

                        <br>

                        <hr class="my-4">
                    </form>
                    <h4>답변 작성</h4>
                    <br>
                    <textarea name="answer" id="answer" cols="10" rows="10" class="textarea form-control"></textarea>
                    <br>
                    <div id="answerMsg">

                    </div>
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
        var answer = $("#answer").val();
        var targetQuestionId = $("#questionId").val();
        var prevPageUrl = "${prevPageUrl}";

        var formData = {
            answer : answer,
            questionId : targetQuestionId
        }

        console.log("answer :: " + answer);
        console.log("questionId :: " + targetQuestionId);
        console.log("Url :: " + prevPageUrl);

        $.ajax({
            url: '/myShop/product/answer/register',
            type: 'post',
            data: formData,

            success: function (result) {
                var check = result.check;

                alert(check);

                if (check === 1) {
                    alert("답변이 등록 되었습니다.");
                    window.location.replace(prevPageUrl);
                }
            }

        });
    });


    // ================================================================================

    $(document).ready(function (e) {

        $("#answer").on('keyup', function () {
            var answer = $("#answer").val();

            if (answer == "") {
                $("#answerMsg").text('내용은 필수 입력 사항 입니다.');
                $("#answerMsg").css('color', 'red');

                $("#registerSubmit").attr('disabled', true);


                return false;
            }

            $("#answerMsg").text('');
            $("#answerMsg").attr('disabled', false);

            $("#registerSubmit").attr('disabled', false);

            return true;

        });
    });

</script>
</html>

