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

</head>
<body>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body">
                    <br>
                    <h2 class="card-title text-center">상품 등록</h2>

                    <!-- Form 시작 -->
                    <form class="form-signin" method="post" action="/register/product/submit" id="registerProduct" enctype="multipart/form-data">
                        <br>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="categoryName">카테고리</label>
                            </div>
                            <select class="custom-select" id="categoryName" name="categoryName" required>
                                <option selected>카테고리 선택</option>
                                <option value="digital">전자</option>
                                <option value="computer">컴퓨터</option>
                                <option value="cloths">의류</option>
                                <option value="shoes">신발</option>
                                <option value="book">도서</option>

                            </select>
                        </div>
                        <div id="category_check" name="category_check" class="check_font"></div>
                        <br>

                        <div class="form-label-group">
                            제목 <img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="productTitle" name="productTitle" class="form-control"
                                   placeholder="상품의 제목을 입력해주세요." minlength="5" maxlength="20">
                        </div>
                        <div name="titleCheckMsg" id="titleCheckMsg"></div>
                        <br>

                        <div class="form-label-group">
                            내용 <img src="/resources/image/check.gif" alt="필수 입력사항"><br>

                            <textarea name="productContent" id="productContent" cols="30" rows="10" class="form-control"></textarea>
                        </div>
                        <div name="contentCheckMsg" id="contentCheckMsg"></div>
                        <br>

                        <div class="form-label-group row">
                            <%--              이미지 첨부 <input type="file" multiple="multiple" id="productImg" name="productImg" class="form-control">--%>
                            <%--              <div id="preview" class="form-label-group"></div>--%>

                            <%--                            이미지 첨부--%>
                            <table border="1">
                                <tr>
                                    <th align="center" width="900px">첨부파일</th>
                                </tr>
                                <tr>
                                    <td align="center" width="900px" >
                                        <input type="file" name="uploadFile" id="uploadFile" multiple class="form-control">
                                        <div id="uploadResult">
                                        </div>
                                        <div>
                                            <input type="hidden" name="fileCount" id="fileCount"/>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <br>

                        <div class="form-label-group">
                            경매 시작 가격<img src="/resources/image/check.gif" alt="필수 입력사항">
                            <input type="text" id="startPrice" name="startPrice" onkeyup="numberWithCommas(this.value)">&nbsp;원
                        </div>
                        <div id="priceCheckMsg" name="priceCheckMsg" class="check_font"></div>

                        <br>
                        <input type="submit" class="btn btn-lg btn-primary btn-block text-uppercase"
                               value="상품 등록" id="registerSubmit" name="registerSubmit" disabled>

                        <a class="d-block text-center mt-2 small" href="login_form"></a>
                        <hr class="my-4">
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<hr>
</body>

<script>

    // $(document).ready(function () {
    //     history.replaceState({}, null, null);
    //
    //     if (history.state) {
    //         alert("history!!");
    //     }
    // });

    var categoryFlag = false;
    var productTitleFlag = false;
    var productContentFlag = false;
    var startPriceFlag = false;

    function numberWithCommas(startPrice) {
        startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
        startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

        $("#startPrice").val(startPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
    }


    var uploadFilesArr = [];
    var tmpArr1 = [];
    var tmpArr2 = [];
    var fileCount = 0;
    var maxFileCount = 5;

    // upload file
    $("input[type='file']").on("change", function (e) {
        var formData = new FormData();
        var fileInput = $('input[name="uploadFile"]');
        var fileList = fileInput[0].files;
        var fileObj = fileList[0];


        for (var i = 0; i < fileList.length; i++) {
            formData.append("uploadFile", fileList[i]);
        }

        // fileCount = fileCount + fileList.length;

        $.ajax({
            url: '/upload/ajax',
            type: 'post',
            data: formData,
            dataType: 'json',
            processData: false,
            contentType: false,

            success: function (result) {
                console.log(result);

                uploadFilesArr = uploadFilesArr.concat(result);

                $("#uploadResult").empty(); // 또 다른 이미지를 업로드할 때 비우고 다시 보여준다.

                showImage(uploadFilesArr);

                fileCount += result.length;

                $("#fileCount").attr('value', fileCount);

                console.log("UploadFileCount :: " + fileCount)

            },
            error: function (request,status,error) {
                alert("fail");
                console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
    });

    var regex = new RegExp("(.*?)\.(jpg|png|jpeg)$");
    var maxSize = 1048576; //1MB

    function fileCheck(fileName, fileSize){

        if(fileSize >= maxSize){
            alert("파일 사이즈 초과");
            return false;
        }

        if(!regex.test(fileName)){
            alert("해당 종류의 파일은 업로드할 수 없습니다.");
            return false;
        }

        return true;
    }

    var uploadResult = $("#uploadResult");

    function showImage(uploadFiles) {
        /* 전달받은 데이터 검증 */
        if(!uploadFiles || uploadFiles.length == 0) {
            return
        }

        var str = "";

        $(uploadFiles).each(function (i, obj) {

            var fullPath = encodeURIComponent(obj.uploadPath + "/" + obj.uploadFileName);

            str += "<div id=\"index_"+i+"\">"
            str += "<img src='/view?fileName=" + fullPath + "' width='100px' height='100px' '>";
            str += "<div onclick='deleteImageIndex("+i+")' class=\"imageDeleteBtn_"+i+"\" data-file='" + fullPath + "' datatype='image'>X</div>";

            str += "<input type='hidden' id=\"imageList[" + i + "].uploadPath\" name=\"imageList[" + i + "].uploadPath\" value='"+ obj.uploadPath +"'>";
            str += "<input type='hidden' id=\"imageList[" + i + "].uploadFileName\" name=\"imageList[" + i + "].uploadFileName\" value='"+ obj.uploadFileName +"'>";
            str += "<input type='hidden' id=\"imageList[" + i + "].uploadFileName\" name=\"imageList[" + i + "].fileSize\" value='"+ obj.fileSize +"'>";
            str += "</div>";

            // console.log("fullPath :: " + fullPath);

            var getId = "#imageList[" + i + "].uploadFileName";
            var getIdValue = $(getId).val();

            console.log("getIdValue :: " + getIdValue);
        });

        uploadResult.append(str);

    }

    // $("#uploadResult").on('click', ".imageDeleteBtn", function () {
    //     alert("DeleteFunction");
    //     deleteUploadFile();
    // })


    //====================TEST====================
    function deleteImageIndex(index) {
        console.log("Delete Index :: " + index);

        deleteImageAction(index);
    }

    function deleteImageAction(index) {
        var targetDivId = "#index_" + index;
        var targetFileClass = ".imageDeleteBtn_" + index

        var targetFile = $(targetFileClass).data("file");
        // var targetFile = $(".imageDeleteBtn").data("file");


        deleteUploadFile(targetFile); // 서버에 등록된 이미지 파일 삭제

        // 하나의 배열을 바라보고 하자. Test
        uploadFilesArr.splice(index, 1);

        $("#uploadResult").empty();

        fileCount--;

        $("#fileCount").attr('value', fileCount);

        console.log("DeleteAction FileCount :: " + fileCount);

        showImage(uploadFilesArr);

        for (var i = 0; i < uploadFilesArr.length; i++) {
            console.log("TmpArray 요소 :: " + uploadFilesArr[i].uploadFileName);
        }

        // console.log("fileCount :: " + fileCount);
    }

    // $("#uploadResult").on('click', ".imageDeleteBtn", function () {
    //     var targetFile = $(".imageDeleteBtn").data("file"); // 하나만바로보고 있다.
    //
    //     console.log("targetFile :: " + targetFile);
    //
    //     deleteUploadFile(targetFile);
    // })

    function deleteUploadFile(targetFile) {
        // var targetFile = $(".imageDeleteBtn").data("file");
        var targetDiv = $("#result");

        console.log("targetFile :: " + targetFile);

        $.ajax({
            url: '/delete/image',
            data: {fileName : targetFile},
            datType: 'text',
            type: 'post',

            success: function (result) {
                console.log(result);

                targetDiv.remove();
                $("input[type='file']").val("");



                // $("#uploadResult").empty(); // 비우고
                // showImage(uploadFilesArr); // 다시 재배열 해야한다.
            },
            error: function (result) {
                alert("파일을 삭제하지 못하였습니다.");
                console.log(result);
            }
        });
    }

    // id = productImg
    // 이미지 업로드
    $(document).ready(function (e) {
        $("#startPrice").on('keyup', function() {
            var startPrice = $("#startPrice").val();
            var priceReg = "/\B(?=(\d{3})+(?!\d))/g";
            var replacePrice = "";

            // replacePrice = replacePrice.toString.replace(priceReg, ',');

            if (startPrice == "") {
                $("#priceCheckMsg").text('경매 시작가는 필수 입력입니다.');
                $("#priceCheckMsg").css('color', 'red');

                startPriceFlag = false;

                return false;
            }

            startPriceFlag = true;

            $("#priceCheckMsg").text('');

            if ((productTitleFlag && productContentFlag && startPriceFlag) == true) {
                $("#registerSubmit").attr('disabled', false);
            }

            return true;

        });

        $("#productContent").on('keyup', function () {
            var productContent = $("#productContent").val();

            if (productContent == "") {
                $("#contentCheckMsg").text('내용은 필수 입력 사항 입니다.');
                $("#contentCheckMsg").css('color', 'red');

                productContentFlag = false;

                return false;
            }

            productContentFlag = true;

            $("#contentCheckMsg").text('');

            console.log(productContentFlag);

            if ((productTitleFlag && productContentFlag && startPriceFlag) == true) {
                $("#registerSubmit").attr('disabled', false);
            }

            return true;

        });
    });

    $("#productTitle").on('keyup', function () {
        var productTitle = $("#productTitle").val();

        console.log(productTitle);

        if (productTitle == "") {
            $("#titleCheckMsg").text('제목은 필수 입력 사항 입니다.');
            $("#titleCheckMsg").css('color', 'red');

            productTitleFlag = false;

            return false;
        }

        productTitleFlag = true;

        $("#titleCheckMsg").text('');

        if ((productTitleFlag && productContentFlag && startPriceFlag) == true) {
            $("#registerSubmit").attr('disabled', false);
        }

        return true;
    });

    // client 뒤로가기시 file 을 비워줘야지, 페이지 리로드 하면서 server 에 이미지가 다시 저장이 안된다.
    // controller 는 어차피 hidden value 로 값을 받기 때문에 상관 없다.
    $("#registerSubmit").on('click', function () {
        $("#uploadFile").val("");
    });
</script>
</html>
