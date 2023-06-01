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
    <title>Register Form</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <h1>Register</h1>
    <div class="form-control ">
        <input id="username" type="text" placeholder="Username">
        <small></small>
        <span></span>
    </div>

    <div class="form-control ">
        <input id="email" type="email" placeholder="Email">
        <small>r</small>
        <span></span>
    </div>

    <div class="form-control ">
        <input id="password" type="password" placeholder="Password">
        <small></small>
        <span></span>
    </div>

    <div class="form-control ">
        <input id="cf-password" type="password" placeholder="Confirm Password">
        <small></small>
        <span></span>
    </div>

    <button type="submit" class="btn-submit">Register</button>

    <div class="signup-link">
        not a member? <a href="#">Signup</a>
    </div>
</div>

<script type="text/javascript" src="${base}/js/register.js"></script>
</body>
</html>
