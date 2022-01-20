<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/18
  Time: 11:48 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>
    <form id="authorizeFrm" name="authorizeFrm" method="get" action="https://testapi.openbanking.or.kr/oauth/2.0/authorize">
        <input type="hidden" name="response_type" value="code"/>
        <input type="hidden" name="client_id" value="${memberId}"/>
        <input type="hidden" name="redirect_uri" value="http://localhost:8080/oauth/callback"/>
        <input type="hidden" name="scope" value="login inquiry transfer"/>
        <input type="hidden" name="state" value="12345678901234567890123456789012"/>
        <input type="hidden" name="auth_type" value="0"/>
        <button type="submit">토큰발급</button>
    </form>
    respnse_type = code (고정값)
    cliend_id = [발급받은 값]
    redirect_uri = [API Key 등록 시 입력한 callback_url]
    scope = [login inquiry transfer] (' ' 스페이스바로 구분)
    state = 32자리의 난수 (임의로 난수 32자리 입력하면 됨)
    auth_type = 0 (최초인증 : 0, 재인증 : 2)


    <a href="/check/account/number">token</a>

    <input type="button" id="test" name="test" value="test">
</body>
<script>

    // https://testapi.openbanking.or.kr/oauth/2.0/authorize?
    // response_type=code&
    // client_id=a2f49b05-ce98-4c0d-876c-5d1d9e4a96f9&
    // redirect_uri=http://localhost:8080/ouath/result&
    // scope=login inquiry transfer&
    // state=a80BLsfigm9OokPTjy03elbJqRHOfGSY&
    // auth_type=0

    $("#test").on('click', function () {
        alert("hello");
        var window = window.open('about:blank');
        window.location = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?" +
            + "response_type=code&"
            + "client_id=a2f49b05-ce98-4c0d-876c-5d1d9e4a96f9&"
            + "redirect_uri=http://localhost:8080/ouath/result&"
            + "scope=login inquiry transfer&"
            + "state=c80BLsfigm9OokPTjy03elbJqRHOfGSY&"
            + "auth_type=0"
    });
</script>
</html>
