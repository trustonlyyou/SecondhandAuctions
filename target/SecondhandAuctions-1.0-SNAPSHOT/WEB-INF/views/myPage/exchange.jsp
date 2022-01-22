<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/18
  Time: 6:11 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="/resources/js/price.js"></script>

    <title>포인트 환전 | 중고 경매의 세계</title>

</head>
<body>
<%@include file="../includes/header.jsp"%>


<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-7 mx-auto">
            <div class="card card-signin flex-row my-5">
                <div class="card-body text-center">
                    <h2 class="card-title text-center">포인트 환불</h2>
                    <hr>
                    <br>

                    <form class="form-signin">
                        <h5 style="text-align: left" id="myPoint"></h5>
                        <br>
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="exchangePrice" name="exchangePrice"
                                   placeholder="환전할 포인트를 입력해주세요." aria-label="Large" aria-describedby="inputGroup-sizing-sm">
                        </div>
                        <div id="msg"></div>
                        <br>
                        <br>
                        <br>

                        <div>
                            <input type="button" id="exchangeSubmit" class="btn btn-dark btn-block" value="환불진행">
                        </div>

                        <br>
                        <hr>
                            <a href="/">Home</a>
                            <a href="/myPage/form">나의 페이지</a>
                        <br>


                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    $(document).ready(function () {
        $("#myPoint").text("현재 포인트 : " + numberWithCommas("${myPoint}"));
    });

    var priceFlag;

    $("#exchangePrice").on('keyup', function () {
        $("#exchangePrice").val(numberWithCommas(this.value));

        var num = $("#exchangePrice").val();
        var arr = num.split(",");
        var chk = arr[arr.length - 1];

        if (num === "") {
            $("#msg").text("환불금액을 입력해주세요.");
            $("#msg").css('color', 'red');

            priceFlag = false;
        }

        if (chk !== "000") {
            $("#msg").text("백원, 십원, 일원 단위의 가격은 책정 할 수 없습니다.");
            $("#msg").css('color', 'red');

            priceFlag = false;

            return false;
        } else {
            $("#msg").text("");

            priceFlag = true;

            return true;
        }
    });

    $("#exchangeSubmit").on('click', function () {
        var point = "${myPoint}";
        var reqPoint = $("#exchangePrice").val();
        var pointChk = comparisonPoint(point, reqPoint);

        if (priceFlag === false) {
            alert("환불 금액을 확인해주세요.");
            return false;

        } else if (pointChk === false) {
            alert("환불금액이 포인트 금액보다 큽니다.");

            return false;
        } else {
            alert("환불요청");

            $.ajax({
                url: '/point/exchange',
                type: 'post',
                data: {
                    reqPoint : reqPoint
                },

                success: function (data) {

                }
            });
        }
    });



</script>
