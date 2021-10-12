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
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">

  <title>상품 판매 | 중고 경매의 세계</title>
</head>
<body>
<header>

  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="/">중고 경매의 세계</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
          <form class="d-flex">
            <input class="form-control me-2" type="search" placeholder="상품을 검색해 보세요." aria-label="Search">&nbsp;
            <button class="btn btn-outline-light" type="submit">검색</button>
          </form>
        </div>
      </nav>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
            <a class="nav-link" href="/">Home
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="member/login/form">로그인</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="member/join/form">회원가입</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">카테고리</a>
          </li>
          <c:if test="${sessionScope.member.memberId == null}">
          </c:if>
          <c:if test="${sessionScope.member.memberId != null}">
            <li class="nav-item">
              <a class="nav-link" href="member/login/form">${sessionScope.member.memberId}</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/member/logout/action">로그아웃</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">카테고리</a>
            </li>
          </c:if>
        </ul>
      </div>
    </div>
  </nav>
</header>

<div class="container">
  <div class="row">
    <div class="col-lg-10 col-xl-9 mx-auto">
      <div class="card card-signin flex-row my-5">
        <div class="card-body">
          <br>
          <h2 class="card-title text-center">상품 등록</h2>

          <!-- Form 시작 -->
          <form class="form-signin" action="/register/product" method="post" enctype="multipart/form-data">
            <br>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <label class="input-group-text" for="categoryName">카테고리</label>
              </div>
              <select class="custom-select" id="categoryName" name="categoryName" onchange="categoryCheck(this)">
                <option selected>카테고리 선택</option>
                <option value="digtal">전자</option>
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

            <div class="form-label-group">
<%--              이미지 첨부 <input type="file" multiple="multiple" id="productImg" name="productImg" class="form-control">--%>
<%--              <div id="preview" class="form-label-group"></div>--%>

              이미지 첨부
              <table border="1">
<%--                <tr>--%>
<%--                  <th align="center" width="900px">첨부파일</th>--%>
<%--                </tr>--%>
                <tr>
                  <td align="center" width="900px" >
                    <input type="file" name="multiFiles" id="multiFiles" multiple class="form-control">
                    <div id="preview"></div>
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

  var categoryFlag = false;
  var productTitleFlag = false;
  var productContentFlag = false;
  var startPriceFlag = false;

  function numberWithCommas(startPrice) {
    startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

    $("#startPrice").val(startPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
  }


  // id = productImg
  // 이미지 업로드
  $(document).ready(function (e) {
    $("#multiFiles").change(function (e) {

      $("#preview").empty();

      var files = e.target.files;
      var arr = Array.prototype.slice.call(files);

      for (var i = 0; i < files.length; i++) {
        if(!checkFile(files[i].name,files[i].size)) {
          return false;
        }
      }
      preview(arr);
    });

    function checkFile(fileName, fileSize) {
      var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
      var maxSize = 20971520;  //20MB

      if(fileSize >= maxSize){
        alert('파일 사이즈 초과');
        $("#uploadFile").val("");  //파일 초기화
        return false;
      }

      if(regex.test(fileName)){
        alert('업로드 불가능한 파일이 있습니다.');
        $("#uploadFile").val("");  //파일 초기화
        return false;
      }
      return true;
    }

    function preview(arr){
      arr.forEach(function(f){

        //파일명이 길면 파일명...으로 처리
        var fileName = f.name;
        if(fileName.length > 10){
          fileName = fileName.substring(0,7)+"...";
        }

        //div에 이미지 추가
        var str = '<div style="display: inline-flex; padding: 10px;"><li>';
        str += '<span>'+fileName+'</span><br>';

        //이미지 파일 미리보기
        if(f.type.match('image.*')){
          var reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성
          reader.onload = function (e) { //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러
            //str += '<button type="button" class="delBtn" value="'+f.name+'" style="background: red">x</button><br>';
            str += '<img src="'+e.target.result+'" title="'+f.name+'" width=100 height=100 />';
            str += '</li></div>';
            $(str).appendTo('#preview');
          }
          reader.readAsDataURL(f);
        }else{
          str += '<img src="/resources/img/fileImg.png" title="'+f.name+'" width=100 height=100 />';
          $(str).appendTo('#preview');
        }
      });//arr.forEach
    }

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

      if ((categoryFlag && productTitleFlag && productContentFlag && startPriceFlag) == true) {
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

      if ((categoryFlag && productTitleFlag && productContentFlag && startPriceFlag) == true) {
        $("#registerSubmit").attr('disabled', false);
      }

      return true;

    });

    // $("#productTitle").on('keyup', function () {
    //   var productTitle = $("#productTitle").val();
    //
    //   if (productTitle == "") {
    //     $("#productTitle_check").text('제목은 필수 입력 사항 입니다.');
    //     $("#productTitle_check").css('color', 'red');
    //
    //     productTitleFlag = false;
    //   }
    //
    //   productTitleFlag = true;
    //
    //   $("#productTitle_check").text('');
    //
    //   if ((categoryFlag && productTitleFlag && productContentFlag && startPriceFlag) == true) {
    //     $("#registerSubmit").attr('disabled', false);
    //   }
    // });


    // var categoryName;
    // categoryName = $("#categoryName option:selected").val()
    //
    // if (categoryName == "") {
    //   $("#category_check").text('카테고리는 필수 입력 사항입니다.');
    //   $("#category_check").css('color', 'red');
    // }


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

    if ((categoryFlag && productTitleFlag && productContentFlag && startPriceFlag) == true) {
      $("#registerSubmit").attr('disabled', false);
    }

    return true;
  });

  function categoryCheck(object) {
    var inputValue = $(object).val();

    if (inputValue == "") {
      $("#category_check").text('카테고리는 필수 입력 사항입니다.');
      $("#category_check").css('color', 'red');

      categoryFlag = false;

      return false;
    }

    categoryFlag = true;

    $("#category_check").text('');

    if ((categoryFlag && productTitleFlag && productContentFlag && startPriceFlag) == true) {
      $("#registerSubmit").attr('disabled', false);
    }

    return true;
  }

</script>
</html>
