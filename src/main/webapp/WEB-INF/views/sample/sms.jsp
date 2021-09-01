<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/21
  Time: 5:29 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">

</head>
<body>
<%--    <form method="get" id="smsForm">--%>
<%--        <ul>--%>
<%--            <li>보낼 사람 :: <input type="text" name="from"></li>--%>
<%--            <li>내용 :: <textarea name="text"></textarea></li>--%>
<%--            <li><input type="button" onclick="sednSMS('sendSms')" value="전송"></li>--%>
<%--        </ul>--%>
<%--    </form>--%>

    <form method="get" action="/sendTest" id="smsForm">
        핸드폰 번호 :: <input type="text" id="memberPhone"><br>
        <input type="submit" id="smsSubmit" value="전송" disabled>
    </form>
</body>
<script type="text/javascript">

    // function sendSMS(pageName) {
    //     console.log("문자를 전송합니다.");
    //     $("#smsForm").attr("action", pageName + ".do");
    //     $("smsForm").submit();
    // }

    $("#memberPhone").on('keyup', function () {
       var memberPhone = $("#memberPhone").val();

       if (memberPhone != "") {
           $("#smsSubmit").attr('disabled', false);

           return true
       }
    });




    // var memberPhone = $("#memberPhone").val();
    // var code;
    //
    // $("#phoneCheck").click(function () {
    //
    //     $.ajax({
    //         type: 'GET',
    //         url: '/sendSms',
    //         data: {
    //             'memberPhone' : memberPhone
    //         },
    //
    //         success: function (data) {
    //             code = data;
    //             console.log("code :: " + code);
    //             alert('전송 완료');
    //         },
    //
    //         error: function () {
    //             alert('error')
    //         }
    //     })
    //
    // });
</script>
</html>
