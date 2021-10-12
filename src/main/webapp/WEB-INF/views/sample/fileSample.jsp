<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/09/22
  Time: 4:58 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/multiFile" method="post" enctype="multipart/form-data">
        <input type="file" name="productImg" id="productImg" multiple class="form-control">
        <input type="submit" name="action" id="action" value="등록">
    </form>
</body>
</html>
