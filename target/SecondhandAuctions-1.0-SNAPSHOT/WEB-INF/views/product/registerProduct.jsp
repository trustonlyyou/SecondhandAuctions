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
    <link rel="stylesheet" href="/resources/css/product.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="/resources/js/product.js"></script>

좌
    <title>상품 등록 | 중고 경매의 세계</title>

</head>
<body>

<%@include file="../includes/header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body">
                    <br>
                    <a href="/">메인 페이지</a>
                    <h2 class="card-title text-center">상품 등록</h2>
                    <!-- Form 시작 -->
                    <form class="form-signin" id="registerProduct" enctype="multipart/form-data">
                        <br>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="categoryName">카테고리</label>
                            </div>
                            <select class="custom-select" id="categoryName" name="categoryName" required>
                                <option value="none">카테고리 선택</option>
                                <option value="digital">전자</option>
                                <option value="computer">컴퓨터</option>
                                <option value="cloths">의류</option>
                                <option value="shoes">신발</option>
                                <option value="book">도서</option>

                            </select>
                        </div>
                        <div id="categoryMsg" name="categoryMsg" class="check_font"></div>
                        <br>

                        <div class="form-label-group">
                            제목 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="productTitle" name="productTitle" class="form-control"
                                   placeholder="상품의 제목을 입력해주세요." minlength="5" maxlength="20">
                        </div>
                        <div name="titleCheckMsg" id="titleCheckMsg"></div>
                        <br>

                        <div class="form-label-group">
                            내용 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <span id="nowByte">0</span>/500bytes
                            <br>

                            <%--              <label for="productContent">내용</label>--%>
                            <textarea name="productContent" id="productContent" cols="30" rows="10" class="product_textarea form-control"></textarea>
                        </div>
                        <div name="contentCheckMsg" id="contentCheckMsg"></div>
                        <br>

                        <br>

                        <div class="form-label-group">
                            경매 시작 가격<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="startPrice" name="startPrice" onkeyup="numberWithCommas(this.value)">&nbsp;원
                        </div>
                        <div id="priceCheckMsg" name="priceCheckMsg" class="check_font"></div>

                        <br>

                        <hr class="my-4">
                    </form>
                    <div class="filebox card-body">
                        <label for="file">이미지 첨부를 원하시면 버튼을 누르세요.</label>
                        <input type="file" id="file" multiple>
                    </div>

                    <div class="col-lg-12">
                        <div class="row" id="imgLoad">
                        </div>
                    </div>
                    <br>
                    <input type="button" id="btn_registerModal" class="btn btn-lg btn-primary btn-block text-uppercase"
                           data-toggle="modal" data-target="#registerModal" value="게시물 등록 하기" disabled>
                    <div class="modal fade" id="registerModal" tabindex="-1" role="dialog"
                         aria-labelledby="registerModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="registerModalLabel">게시물을 정말 등록하시겠습니까??</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-footer">
                                    <input type="button" class="btn btn-secondary" data-dismiss="modal" value="취소">
                                    <input type="button" id="registerSubmit" class="btn btn-primary" value="확인">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>


<hr>
</body>

<script>
    var uploadFiles = [];
    var formData = new FormData();
    var imgIndex = 0;
    var fileIndex = 0;
    var fileCount = 0;
    var maxCount = 5;
    var regex = new RegExp("(.*?)\.(jpg|png|jpeg)$");
    var maxSize = 1048576; //1MB

    $("#file").on("change", function (e) {
        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);

        for (var i = 0; i < filesArr.length; i++, fileIndex++) {

            var file = filesArr[i];

            for (var value of formData.values()) {
                if (value.name === filesArr[i].name) {
                    window.alert("중복된 사진이 있습니다.");
                    return;
                }
            }

            if (!regex.test(filesArr[i].type)) {
                window.alert("확장자는 이미지 확장자만 가능합니다.");
                return;
            }

            if (fileCount === maxCount) {
                window.alert("이미지 파일은 최대 5장만 업로드가 가능합니다.");

                return;
            }

            fileCount++;

            formData.append(fileIndex, file);

            var fileReader = new FileReader();

            fileReader.onload = function (e) {
                var img_html =
                    "<div class='col-lg-4 col-md-4 mb-4' id=\"img_" + imgIndex + "\">"
                    + "<div class='card h-100'>"
                    + "<img id=\"img_id_" + imgIndex + "\" src=\"" + e.target.result + "\" width='100%' height='100px'  />"
                    + "<input type='button' id=\"deleteBtn_" + imgIndex + "\" value='X' onclick=\"deleteImg(" + imgIndex + ")\">"
                    + "</div>"
                    + "</div>";

                $("#imgLoad").append(img_html);
                imgIndex++;
            }
            fileReader.readAsDataURL(file);
        }

    });

    function deleteImg(index) {
        console.log("delete index :: " + index);

        formData.delete(index);

        var imageDivId = "#img_" + index;

        $(imageDivId).remove();

    }


    $("#registerSubmit").on('click', function () {
        $.ajax({
            url: '/product/submit',
            type: 'post',
            data: $("form").serialize(),

            success: function (result) {
                var check = result.check;
                var productId = result.productId;

                if (check !== true) {
                    window.alert("게시물 등록 실패, 다시 등록해주세요..");
                    window.location.replace("/register/product/form");
                    return false;
                } else {
                    formData.append("productId", productId);

                    $.ajax({
                        url: '/upload/image',
                        type: 'post',
                        data: formData,
                        processData: false,
                        contentType: false,

                        success: function (result) {
                            var check = result.check;

                            if (check === true) {
                                window.location.replace("/product/register/success");
                            } else {
                                
                            }
                        },
                        error: function (request, status, error) {
                            console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                        }
                    });
                }
            }
        });
    });


    // ================================================================================
    var categoryFlag = false;
    var productTitleFlag = false;
    var productContentFlag = false;
    var startPriceFlag = false;

    $(document).ready(function (e) {
        $("#startPrice").on('keyup', function () {
            var startPrice = $("#startPrice").val();
            var startPriceArr = startPrice.split(",");
            var chkPrice = startPriceArr[startPriceArr.length - 1];

            if (startPrice == "") {
                $("#priceCheckMsg").text('경매 시작가는 필수 입력입니다.');
                $("#priceCheckMsg").css('color', 'red');

                startPriceFlag = false;

                $("#btn_registerModal").attr('disabled', true);

                return false;
            }

            if (chkPrice !== "000") {
                $("#priceCheckMsg").text("백원, 십원, 일원 단위의 가격은 책정 할 수 없습니다.");
                $("#priceCheckMsg").css('color', 'red');

                startPriceFlag = false;

                $("#btn_registerModal").attr('disabled', true);

                return false;
            }


            startPriceFlag = true;

            $("#priceCheckMsg").text('');

            if ((productTitleFlag && productContentFlag && startPriceFlag && categoryFlag) == true) {
                $("#btn_registerModal").attr('disabled', false);
            }

            return true;

        });

        $("#productContent").on('keyup', function () {
            var productContent = $("#productContent").val();
            var maxByte = 500; //최대 500바이트
            var productContentLength = productContent.length;
            var totalByte = 0;

            if (productContent == "") {
                $("#contentCheckMsg").text('내용은 필수 입력 사항 입니다.');
                $("#contentCheckMsg").css('color', 'red');

                productContentFlag = false;

                $("#btn_registerModal").attr('disabled', true);

                return false;
            }

            for (var i = 0; i < productContentLength; i++){
                var eachChar = productContent.charAt(i);
                var uniChar = escape(eachChar) //유니코드 형식으로 변환

                if(uniChar.length>4){
                    // 한글 : 2Byte
                    totalByte += 2;
                }else{
                    // 영문,숫자,특수문자 : 1Byte
                    totalByte += 1;
                }
            }

            if (totalByte > maxByte){
                $("#nowByte").text(totalByte);
                $("#nowByte").css('color', 'red');

                $("#contentCheckMsg").text("최대 500Byte까지만 입력가능합니다.");
                $("#contentCheckMsg").css('color', 'red');

                productContentFlag = false;

                $("#btn_registerModal").attr('disabled', true);

                return false;
            }

            $("#nowByte").text(totalByte);
            $("#nowByte").css('color', 'green');

            productContentFlag = true;

            $("#contentCheckMsg").text('');

            console.log(productContentFlag);

            if ((productTitleFlag && productContentFlag && startPriceFlag && categoryFlag) == true) {
                $("#btn_registerModal").attr('disabled', false);
            }

            return true;

        });
    });

    $("#productTitle").on('keyup', function () {
        var productTitle = $("#productTitle").val();

        if (productTitle == "") {
            $("#titleCheckMsg").text('제목은 필수 입력 사항 입니다.');
            $("#titleCheckMsg").css('color', 'red');

            productTitleFlag = false;

            $("#btn_registerModal").attr('disabled', true);

            return false;
        }

        productTitleFlag = true;

        $("#titleCheckMsg").text('');

        if ((productTitleFlag && productContentFlag && startPriceFlag && categoryFlag) == true) {
            $("#btn_registerModal").attr('disabled', false);
        }

        return true;
    });

    $("#categoryName").on('change', function () {
       var categoryName = $("#categoryName option:selected").val();

        if (categoryName === "none") {
            $("#categoryMsg").text("카테고리 선택은 필수 입니다.");
            $("#categoryMsg").css('color','red');

            categoryFlag = false;

            $("#btn_registerModal").attr('disabled', true);

            return false;
        }

        categoryFlag = true;

        $("#categoryMsg").text("");

        if ((productTitleFlag && productContentFlag && startPriceFlag && categoryFlag) == true) {
            $("#btn_registerModal").attr('disabled', false);
        }

        return true;
    });

</script>
</html>

