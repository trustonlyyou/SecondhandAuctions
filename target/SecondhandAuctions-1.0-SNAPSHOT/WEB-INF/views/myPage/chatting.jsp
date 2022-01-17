<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2022/01/10
  Time: 8:16 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>


  <title>중고 경매의 세계 | 채팅</title>
  <style>
    .chatlist {
      background-color: dimgray;
      width: auto;
      height: 500px;
      overflow: auto;
      text-align: center;
    }

    .me {
      color: #F6F6F6;
      text-align: right;
    }

    .others {
      color: #FFE400;
      text-align: left;
    }

    .chatting {
      width: auto;
      resize: none;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-lg-10 col-xl-9 mx-auto">
      <div class="card text-center">
        <div class="card-body">
          <h4>이거 판매 할게요.</h4>
          <hr>

          <div class="chatlist" id="chatlist">
<%--            <p class="me">나 : 안녕하세요</p>--%>
<%--            <p class="others">zkem123456 : 안녕하세요.</p>--%>
            <c:forEach items="${list}" var="list">
              <c:if test="${list.memberId eq sessionScope.member}">
                <p class="me">나 : ${list.msg}</p>
              </c:if>
              <c:if test="${list.memberId ne sessionScope.member}">
                <p class="others">${list.memberId} : ${list.msg}</p>
              </c:if>
            </c:forEach>
          </div>
          <br>
          <div>
            <!-- <input type="text" class="chatting" id="chatting" name="chatting" placeholder="메세지를 입력해주세요."> -->
            <input class="chatting" id="chatting" name="chatting" placeholder="메시지를 입력해주세요.">
            <input type="button" class="btn btn-primary btn-sm" value="전송">
          </div>
        </div>
      </div>
    </div>
  </div>

  <input type="hidden" id="memberId" name="memberId" value="${memberId}"/>
  <input type="hidden" id="roomNo" name="roomNo" value="${roomNo}">
  <input type="hidden" id="bidder" name="bidder" value="${bidder}">
  <input type="hidden" id="seller" name="seller" value="${seller}">
</div>
</body>
<script>
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

        if (json.memberId == $("#memberId").val()) {
          $("#chatlist").append("<p class='me'>나 :" + json.msg + "</p>");
        } else {
          $("#chatlist").append("<p class='others'>" + json.memberId + " :" + json.msg + "</p>");

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