<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/08/30
  Time: 1:11 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
    <input type="email" id="memberEmail" name="memberEmail">
    <span id="msg"></span>
</body>
<script>
    $("#memberEmail").on('propertychange change keyup paste input', function () {
        var memberEmail = $("#memberEmail").val();
        var data = {memberEmail : memberEmail}

        $.ajax({
            type: "post",
            url: "/check/email",
            data: data,
            success : function (check) {
                console.log("Email :: " + memberEmail);
                console.log("성공여부 :: " + check);
            }
        });
    });
</script>
</html>
