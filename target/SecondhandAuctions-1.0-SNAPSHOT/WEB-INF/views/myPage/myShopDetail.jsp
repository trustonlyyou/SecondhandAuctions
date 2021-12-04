<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/11/08
  Time: 12:17 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

    <title>상품 조회 | 중고 경매의 세계</title>

</head>
<style>
    .card {
        width: 800px;
    }
    .carousel {
        /* width: 750px; */
        width: 100%;
        height: 500px;
    }

    .carousel-inner > .carousel-item > img{
        /* width: 700px;  */
        width: 80%;
        height: 500px;
    }

    .carousel-control-prev-icon, .carousel-control-next-icon {
        background-color: rgba(0, 0, 0, 1);
    }
    /* .card {
      position: absolute;
    } */
</style>
<body>


<div class="container">

    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card text-center">
                <div class="card-body">
                    <div class="carousel slide" data-ride="carousel" id="banner" >
                        <!-- 이미지 부분 -->
                        <div class="carousel-inner" id="images">
                            <c:forEach var="fileName" items="${fileName}" varStatus="stauts">
                                <div class="carousel-item ${stauts.index == 0 ? "active" : ""}">
                                    <img src="/detail/show?fileName=${fileName}">
                                </div>
                            </c:forEach>
                        </div>
                        <!-- 인디케이션 부분 -->
                        <ul class="carousel-indicators">
<%--                            <li data-target="#banner" data-slide-to="0" class="active"></li>--%>
<%--                            <li data-target="#banner" data-slide-to="1"></li>--%>
<%--                            <li data-target="#banner" data-slide-to="2"></li>--%>
                            <c:forEach var="count" items="${fileName}" varStatus="status">
                                <li data-target="#banner" data-slide-to="${status.index}" class="${status.index == 0 ? "active" : ""}"></li>
                            </c:forEach>
                        </ul>
                        <!-- 이동 버튼 부분 -->
                        <a class="carousel-control-prev" href="#banner" data-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                        </a>
                        <a class="carousel-control-next" href="#banner" data-slide="next">
                            <span class="carousel-control-next-icon"></span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="card card-signin flex-row my-5">
                <div class="card-body">
                    <%--                    <h2 class="card-title text-center">상품 조회</h2>--%>
                    <!-- Form 시작 -->
                        <br>


                        <div class="form-label-group">
<%--                            제목<br>--%>
                            <label for="productTitle">제목</label>
                            <input type="text" id="productTitle" name="productTitle" class="form-control"
                                                                     readonly value="${product.productTitle}">
                        </div>
                        <br>

                        <div class="form-label-group">
<%--                            내용<br>--%>

                            <label for="productContent">내용</label>
                            <textarea name="productContent" id="productContent" cols="30" rows="10" class="form-control" readonly>
                                <c:out value="${product.productContent}"/>
                            </textarea>
                        </div>
                        <br>

                        <br>

                        <div class="form-label-group">
                            현재 가격 <c:out value="${product.startPrice}"/>
                            <%--                            <input type="text" id="startPrice" name="startPrice" readonly value="">&nbsp;원--%>
                        </div>
                        <br>
                        <div class="row">
                            <form id="deleteForm" action="/myShop/product/delete" method="post">
                                <input class="deleteBtn btn btn-danger btn-sm float-right" type="button" name="deleteProduct" id="deleteProduct" value="게시물 삭제" >
                                <input type="hidden" name="deleteProductId" id="deleteProductId" value="${product.productId}">
                            </form>&nbsp;
                            <form id="modifyForm">
                                <input type="button" class="listBtn btn btn-warning btn-sm float-right" value="게시물 수정">
                            </form>&nbsp;
                            <form id="actionForm" action="/myShop/list">
                                <input type="button" class="listBtn btn btn-primary btn-sm float-right" value="조회 페이지">
                            </form>
                        </div>
                </div>
            </div>
            <div class="card text-left">
                <div class="card-body">
                    <h4>Q&A</h4><br>
                    <br>
                    <table class="table">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">작성자</th>
                            <th scope="col">제목</th>
                            <th scope="col">날짜</th>
                            <th scope="col">답변 여부</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%-- foreach --%>
                        <c:forEach items="${qna}" var="qna">
                            <tr>
                                <td>${qna.get("questionMemberId")}</td>
                                <td>${qna.get("questionTitle")}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd" value='${qna.get("regdate")}' /></td>
                                <td>
                                    <c:if test='${qna.get("isAnswer") eq true}'>
                                        <a style="color: green;" data-toggle="collapse" href='#${qna.get("questionId")}' role="button" aria-expanded="false" aria-controls="collapseExample">
                                            답변완료
                                        </a>
                                    </c:if>
                                    <c:if test='${qna.get("isAnswer") eq false}'>
                                        <a style="color: red;" data-toggle="collapse" href='#${qna.get("questionId")}' role="button" aria-expanded="false" aria-controls="collapseExample">
                                            미답변
                                        </a>
                                        &nbsp;&nbsp;
<%--                                        <input type="button" class="answer_btn btn btn-primary btn-sm" value="답변 달기">--%>
                                        <a href='/myShop/product/answer/form?productId=${product.productId}&questionId=${qna.get("questionId")}'
                                           class="answer_btn btn btn-primary btn-sm">답변 달기</a>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <c:if test='${qna.get("isAnswer") eq true}'>
                                    <td colspan="4">
                                        <div class="collapse" id='${qna.get("questionId")}'>
                                            <c:out value='${qna.get("answer")}' />
                                        </div>
                                    </td>
                                </c:if>

                                <c:if test='${qna.get("isAnswer") eq false}'>
                                    <td colspan="4">
                                        <div class="collapse" id='${qna.get("questionId")}'>
                                            아직 답변이 달리지 않았습니다.
                                        </div>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        <%-- end foreach --%>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
<%--<./container>--%>



<hr>
</body>
<script>
    $(document).ready(function () {
        var msg = "${msg}";
        var actionForm = $("#actionForm");

        if (msg === "productNull") {
            alert(msg);
            window.location.replace("/shop");
        }

        $(".listBtn").on("click", function (e) {
            e.preventDefault();
            actionForm.submit();
        });

        var deleteForm = $("#deleteForm");

        $(".deleteBtn").on("click", function (e) {
            e.preventDefault();

            // if (confirm("해당 게시물을 정말로 취소 하시겠습니끼?") === true) {
            //     deleteForm.submit();
            // } else {
            //     alert("게시물 삭제 취소");
            // }


            var productId = $("#productId").val();

            var formData = {
                productId : productId
            }

            $.ajax({
                url: '/myPage/myShop/product/delete',
                type: 'post',
                data: formData,

                success: function (result) {
                    var check = result.check;

                    switch (check) {
                        case 0 :
                            window.alert("삭제할 수 있는 권한이 없습니다. 다시 시도해 주세요.");
                            break;

                        case 1 :
                            window.alert("게시물 삭제가 완료 되었습니다.");
                            window.location.replace("/myShop/list")

                            break;
                    }
                },

                error: function (request,status,error) {

                }
            });
        });

        $(".answer_btn").on("click", function () {

        });
    });
</script>
</html>

