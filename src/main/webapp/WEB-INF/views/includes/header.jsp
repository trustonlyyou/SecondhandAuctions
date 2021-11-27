<%--
  Created by IntelliJ IDEA.
  User: 렁환이
  Date: 2021-06-10
  Time: 오후 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/">중고 경매의 세계</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <nav class="navbar navbar-dark bg-dark">
                <div class="container-fluid">
                    <form class="form-inline my-2 my-lg-0">
                        <input class="form-control me-2" type="search" placeholder="상품을 검색해 보세요." aria-label="Search">&nbsp;
                        <button class="btn btn-outline-light" type="submit">검색</button>
                    </form>
                </div>
            </nav>
            <div class="collapse navbar-collapse" id="navbarResponsive">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/">Home
                            <span class="sr-only">(current)</span>
                        </a>
                    </li>
                    <c:if test="${sessionScope.member == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="/member/login/form">로그인</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/member/join/form">회원가입</a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.member != null}">
                        <li class="nav-item">
                            <a class="nav-link" href="/myPage">마이페이지</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/member/logout/action">로그아웃</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/register/product/form">상품 등록</a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
</header>

<br>
<br>
<br>
<hr>