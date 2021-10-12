<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/09/10
  Time: 3:21 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
  <h3>단일 파일 업로드</h3>
  <form action="/upload/action" method="post" enctype="multipart/form-data"> <h3>단일 파일 업로드</h3>
  <table>
    <tr>
      <td>Select File</td>
      <td><input type="file" name="mediaFile"></td>
      <td>
        <button type="submit">Upload</button>
      </td>
    </tr>
  </table>

    <form action="/upload/action/multiple" method="post" enctype="multipart/form-data"> <h3>다중 파일 업로드</h3>
      <table>
        <tr>
          <td>Select File</td>
          <td><input type="file" name="mediaFile" multiple></td>
          <td>
            <button type="submit">Upload</button>
          </td>
        </tr>
      </table>
    </form>
</body>
</html>

