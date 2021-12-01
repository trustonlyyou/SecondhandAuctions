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
          <h2 class="card-title text-center">상품 등록</h2>
          <!-- Form 시작 -->
          <form class="form-signin" id="registerProduct" enctype="multipart/form-data">
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

              <textarea name="productContent" id="productContent" cols="30" rows="10" class="product_textarea form-control"></textarea>
            </div>
            <div name="contentCheckMsg" id="contentCheckMsg"></div>
            <br>

            <%--                        <div class="filebox card-body">--%>
            <%--                            <label for="file">이미지 첨부를 원하시면 버튼을 누르세요.</label>--%>
            <%--                            <input type="file" id="file" multiple>--%>
            <%--                        </div>--%>

            <%--                        <div class="col-lg-12">--%>
            <%--                            <div class="row" id="imgLoad">--%>
            <%--                            </div>--%>
            <%--                        </div>--%>

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
          <input type="button" class="btn btn-lg btn-primary btn-block text-uppercase"
                 value="상품 등록" id="registerSubmit" name="registerSubmit" disabled>
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



      formData.append(fileIndex, file);

      var fileReader = new FileReader();

      fileReader.onload = function (e) {
        var img_html =
                "<div class='col-lg-4 col-md-4 mb-4' id=\"img_"+imgIndex+"\">"
                + "<div class='card h-100'>"
                + "<img id=\"img_id_" + imgIndex + "\" src=\"" + e.target.result + "\" width='100%' height='100px'  />"
                + "<input type='button' id=\"deleteBtn_"+imgIndex+"\" value='X' onclick=\"deleteImg("+imgIndex+")\">"
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


  // $("#upload").on("click", function () {
  //   $.ajax({
  //     url: '/upload/ajax/test',
  //     type: 'post',
  //     data: formData,
  //     processData: false,
  //     contentType: false,
  //
  //     success: function (result) {
  //       alert("hello");
  //     }
  //   });
  // });

  // todo :: 500 error
  // $("#check").on("click", function () {
  //   window.alert("click");
  //   var productId = 18;
  //
  //   formData.append("productId", productId);
  //
  //   for (var value of formData.values()) {
  //     console.log(value);
  //   }
  //
  //   $.ajax({
  //     url: '/upload/ajax/test',
  //     type: 'post',
  //     data: formData,
  //     processData: false,
  //     contentType: false,
  //
  //     success: function (result) {
  //       alert("hello");
  //     },
  //     error: function (request,status,error) {
  //       console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  //     }
  //   });
  // });

  $("#registerSubmit").on('click', function () {
    $.ajax({
      url: '/product/submit',
      type: 'post',
      data: $("form").serialize(),

      success: function (result) {
        var check = result.check;
        var productId = result.productId;

        alert("product register result :: " + check);

        if (check === 0) {
          window.alert("게시물 등록 실패, 다시 등록해주세요..");
          window.location.replace("/register/product/form");
          return;
        }

        if (check === 1) {
          window.alert("게시물 등록 성공!!");
          formData.append("productId", productId);
        }

        // switch (check) {
        //   case 0 :
        //     window.alert("게시물 등록 실패, 다시 등록해주세요..");
        //     window.location.replace("/register/product/form");
        //     break;
        //
        //   case 1 :
        //     formData.append("productId", productId);
        //     break;
        // }

        $.ajax({
          url: '/upload/image',
          type: 'post',
          data: formData,
          processData: false,
          contentType: false,

          success: function (result) {
            var check = result.check;

            window.alert("이미지 업로드 성공");
            window.alert("image result :: " + check);

            if (check === 1) {
              window.location.replace("/product/register/success");
            }
          },
          error: function (request,status,error) {
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          }
        });
      }
    });
  });


  // ================================================================================
  var categoryFlag = false;
  var productTitleFlag = false;
  var productContentFlag = false;
  var startPriceFlag = false;

  function numberWithCommas(startPrice) {
    startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

    $("#startPrice").val(startPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
  }

  $(document).ready(function (e) {
    $("#startPrice").on('keyup', function() {
      var startPrice = $("#startPrice").val();
      var priceReg = "/\B(?=(\d{3})+(?!\d))/g";
      var replacePrice = "";

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

</script>
</html>

