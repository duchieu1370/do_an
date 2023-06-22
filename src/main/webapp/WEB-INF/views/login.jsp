<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!-- SPRING FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${base}/css/register.css">
    <title>Login Form</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>
</head>
<body>
<div class="container">

    <form method="POST" action="${base }/perform_login">

        <!-- Kiểm tra xem đã login thành công hay không qua biến login_error -->
        <c:if test="${not empty param.login_error }">
            <div class="TB" style="color: red;">
                Dang nhap khong thanh cong, thu lai!!!
            </div>
        </c:if>

        <h1>Login</h1>
        <div class="form-control ">
            <!-- bắt buộc name phải để là "username" -->
            <input id="username" type="text" name="username" placeholder="Username"/>
            <small></small>
            <span></span>
        </div>

        <div class="form-control ">
            <!-- bắt buộc name phải để là "password" -->
            <input id="password" type="password" name="password" placeholder="Password"/>
            <small></small>
            <span></span>
        </div>

        <button type="submit" class="btn-submit">Login</button>

        <div class="signup-link">
            not a member? <a href="${base }/register">Register</a>
        </div>
    </form>

</div>

</body>
</html>
