<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${base}/css/register.css">
    <title>Register Form</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <form action="${base }/register" method="POST" modelAttribute="user">
        <h1>Register</h1>
        <div id="TB_REGISTER" class="form-control">

        </div>
        <div class="form-control ">
            <input path="username" id="username" type="text" name="username" placeholder="Username"/>
            <small></small>
            <span></span>
        </div>

        <div class="form-control ">
            <input path="email" id="email" type="email" name="email" placeholder="Email"/>
            <small></small>
            <span></span>
        </div>

        <div class="form-control ">
            <input path="phone" id="phone" type="number" name="phone" placeholder="Phone"/>
            <small></small>
            <span></span>
        </div>

        <div class="form-control ">
            <input path="password" id="password" type="password" name="password" placeholder="Password"/>
            <small></small>
            <span></span>
        </div>

        <div class="form-control ">
            <input path="address" id="address" type="text" name="address" placeholder="Address"/>
            <small></small>
            <span></span>
        </div>

        <button type="submit" class="btn-submit">Register</button>

        <div class="signup-link">
            <a href="${base }/login">Back Login</a>
        </div>
    </form>


</div>

</body>
</html>
