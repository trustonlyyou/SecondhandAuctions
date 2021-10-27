<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/10/16
  Time: 1:47 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <style>
        .fileDrop {
            width: 100%;
            height: 200px;
            border: 1px dotted blue;
        }

        small {
            margin-left: 3px;
            font-weight: bold;
            color: gray;
        }
    </style>
</head>
<body>
    <h3>Ajax File upload</h3>
    <div class="fileDrop"></div>
    <div class="uploadedList"></div>

    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script>
        $(".fileDrop").on("dragenter dragover", function (event) {
            event.preventDefault();

            var files = event.originalEvent.dataTransfer.files;

            var file = files[0];

            console.log(file);

        });

        $(".fileDrop").on("drop", function (event) {
            event.preventDefault();
        });
    </script>
</body>
</html>
