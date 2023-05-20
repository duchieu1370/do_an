<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fashion Msic</title>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${base}/css/contact.css">
</head>
<body>
<main class="container">
    <div class="free">Miễn phí vận chuyển với đơn hàng trên 1000k</div>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/header.jsp"></jsp:include>

    <%--		open content--%>
    <%--    <section>--%>
    <%--        <c:if test="${not empty TB }">--%>
    <%--            <div class="alert alert-primary" id="status" role="alert">${TB }</div>--%>
    <%--        </c:if>--%>
    <%--        <div class="container-content">--%>
    <%--            <sf:form modelAttribute="contact" action="${base }/contact"--%>
    <%--                     method="POST" id="my-form" enctype="multipart/form-data">--%>

    <%--                <div class="form-group" id="z-name">--%>
    <%--                    <label for="name"> Name</label>--%>
    <%--                    <sf:input path="name" type="text" id="name" name="name"--%>
    <%--                              pattern="^[a-zA-Z\\s]+"--%>
    <%--                              title="Name gồm chữ cái"/>--%>
    <%--                </div>--%>

    <%--                <div class="form-group" id="z-email">--%>
    <%--                    <label for="email">Email</label>--%>
    <%--                    <sf:input path="email" type="email" id="email" name="email"--%>
    <%--                              pattern="^[A-Za-z0-9]{6,32}@([a-zA-Z0-9]{2,12})(.[a-zA-Z]{2,12})+$"--%>
    <%--                    />--%>
    <%--                    <span id="text" class="text"></span>--%>
    <%--                </div>--%>

    <%--                <div class="form-group" id="z-massage">--%>
    <%--                    <label for="massage">Massage</label>--%>
    <%--                    <sf:textarea path="massage" name="massage" id="massage" cols="30"--%>
    <%--                                 rows="10" pattern="^[a-zA-Z\\s]+"/>--%>
    <%--                </div>--%>

    <%--                <button type="submit" class="sub">Submit</button>--%>
    <%--            </sf:form>--%>
    <%--        </div>--%>
    <%--    </section>--%>
    <form action="${base }/contact" modelAttribute="contact" method="post">
        <div class="contact-body">
            <div class="container-content">
                <div id="TB_AJAX_CONTACT"
                     style="text-align: center; margin-top: 15px; color: #766b6b; margin-bottom: -16px;">
                </div>

                <div class="form-group">
                    <label for="name"> Name</label>
                    <input path="name" type="text" id="name" name="name"
                           pattern="^[a-zA-Z\\s]+"
                           title="Name gồm chữ cái">
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input path="email" type="email" id="email" name="email">
                    <span class="text"></span>
                </div>

                <div class="form-group">
                    <label for="massage">Massage</label>
                    <textarea path="massage" name="massage" id="massage" cols="30"
                              rows="10" pattern="^[a-zA-Z\\s]+"></textarea>
                </div>
                <div class="form-group">
                    <button type="button" onclick="contact('${base}');" class="sub">Submit</button>
                </div>

            </div>
        </div>
    </form>
    <%--close content --%>
    <jsp:include page="/WEB-INF/views/khachhang/layouts/footer.jsp"></jsp:include>
    <div class="copyright">
        Copyright <i class="far fa-copyright"></i> <a href="#">msic.</a> <a
            href="#">Powered by Haravan</a>
    </div>
</main>

</body>
<jsp:include page="/WEB-INF/views/khachhang/layouts/js.jsp"></jsp:include>
<script type="text/javascript">

</script>
</html>