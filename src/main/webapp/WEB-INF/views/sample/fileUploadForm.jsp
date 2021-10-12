<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/09/10
  Time: 5:20 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/file/action" method="post" enctype="multipart/form-data"> <h3>다중 파일 업로드</h3>

    <div>
        <label for="categoryName"></label>
        <select id="categoryName" name="categoryName">
            <option selected>카테고리 선택</option>
            <option value="computer" id="computer" name="computer">컴퓨터</option>
        </select>
    </div>

    <div>
        제목 :: <input type="text" id="productTitle" name="productTitle">
    </div>
    <div>
        내용 :: <textarea id="productContent" name="productContent" cols="30" rows="10"></textarea>
    </div>

    <table>
        <tr>
            <td>Select File</td>
            <td><input type="file" id="mediaFile" name="mediaFile" multiple></td>
        </tr>
    </table>

    <div>
        <input type="text" id="startPrice" name="startPrice">
    </div>

    <button type="submit">등록</button>
</form>

</body>
</html>
