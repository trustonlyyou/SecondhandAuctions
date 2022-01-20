<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/18
  Time: 3:08 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>token success</h1>
    <h2>${token}</h2>

    <form action="/real/name" method="get">
        <input type="text" name="token" id="token" value="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAxMDAyODU0Iiwic2NvcGUiOlsiaW5xdWlyeSIsImxvZ2luIiwidHJhbnNmZXIiXSwiaXNzIjoiaHR0cHM6Ly93d3cub3BlbmJhbmtpbmcub3Iua3IiLCJleHAiOjE2NTAyOTM4MjEsImp0aSI6ImZiNjMyNzg5LWQ5ZjktNDg2Yy04YzMyLWUxZGZiNWU1NjZlOCJ9.k6JZx_ZskVRKMIw6Pod1Aj8bpFwFXhwBqV8t1VYo7zc">
        이름 :: <input type="text" id="memberName" name="memberName">
        생년월일(951024) :: <input type="text" id="memberBirth" name="memberBirth">
        <select id="bank_code" name="bank_code" required>
            <option selected>은행 선택</option>
            <option value="088">신한</option>
            <option value="097">오픈</option>
        </select>
        계좌번호 <input type="text" id="account_num" name="account_num">
        <input type="submit" value="실명 인증">
    </form>

</body>
</html>
