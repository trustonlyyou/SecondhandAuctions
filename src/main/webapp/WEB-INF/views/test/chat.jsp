<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/14
  Time: 10:58 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>

</head>
<style>
    *{
        margin:0;
        padding:0;
    }
    .container{
        width: 500px;
        margin: 0 auto;
        padding: 25px
    }
    .container h1{
        text-align: left;
        padding: 5px 5px 5px 15px;
        color: #FFBB00;
        border-left: 3px solid #FFBB00;
        margin-bottom: 20px;
    }
    .chating{
        background-color: #000;
        width: 500px;
        height: 500px;
        overflow: auto;
    }
    .chating .me{
        color: #F6F6F6;
        text-align: right;
    }
    .chating .others{
        color: #FFE400;
        text-align: left;
    }
    input{
        width: 330px;
        height: 25px;
    }
    #yourMsg{
        display: none;
    }
</style>
<body>
<div id="container" class="container">
    <h1>채팅</h1>
    <input type="hidden" id="memberId" name="memberId" value="${memberId}"/>
    <input type="hidden" id="roomNo" name="roomNo" value="${roomNo}">
    <input type="hidden" id="bidder" name="bidder" value="${bidder}">
    <input type="hidden" id="seller" name="seller" value="${seller}">
    <div id="chating" class="chating">
        <c:forEach items="${list}" var="list">
            <c:if test="${list.memberId eq sessionScope.member}">
                <p class="me">나 : ${list.msg}</p>
            </c:if>
            <c:if test="${list.memberId ne sessionScope.member}">
                <p class="others">${list.memberId} : ${list.msg}</p>
            </c:if>
        </c:forEach>
    </div>


    <input id="chatting" placeholder="보내실 메시지를 입력하세요.">
    <button onclick="send()" id="sendBtn">보내기</button>
</div>
</body>
<script type="text/javascript">
    var socket;

    function socketOpen(){
        socket = SockJS('<c:url value="/chating/${roomNo}"/>');
        socketEvent();
    }

    function socketEvent () {
        socket.onopen = function(data){
            //소켓이 열리면 초기화 세팅하기
        }

        socket.onmessage = function(data) {
            var msg = data.data;
            if(msg != null && msg.trim() != ''){
                var json = JSON.parse(msg);


                if (json.type == "message") {

                    if (json.memberId == $("#memberId").val()) {
                        $("#chating").append("<p class='me'>나 :" + json.msg + "</p>");
                    } else {
                        $("#chating").append("<p class='others'>" + json.memberId + " :" + json.msg + "</p>");
                    }
                }
            }
        }

        document.addEventListener("keypress", function(e){
            if(e.keyCode == 13){ //enter press
                send();
            }
        });
    }

    function send() {
        var json = {
            type: "message",
            bidder : $("#bidder").val(),
            seller : $("#seller").val(),
            memberId : $("#memberId").val(),
            msg : $("#chatting").val(),
            roomNo : "${roomNo}"
        }

        socket.send(JSON.stringify(json))
        $('#chatting').val("");
    }

    $(document).ready(function () {
        var memberId = "${sessionScope.member}";

        if (memberId !== '') {
            console.log("connection");
            socketOpen();
        } else {
            console.log("not connection");
        }
    });
</script>
</html>
