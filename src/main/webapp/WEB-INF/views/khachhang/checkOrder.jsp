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
    <link rel="stylesheet" type="text/css" href="${base}/css/checkOrder.css">
</head>
<body>
<main class="container">
    <div class="free">Miễn phí vận chuyển với đơn hàng trên 1000k</div>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/header.jsp"></jsp:include>

    <form action="${base }/checkOrder" method="get">
        <div class="check-body">
            <div class="container-content">
                <h3>Tra cứu đơn hàng</h3>
                <div id="TB_AJAX_CONTACT"
                     style="text-align: center; margin-top: 15px; color: #766b6b; margin-bottom: -16px;">
                </div>

                <div class="form-group">
                    <input path="email" type="email" id="email" name="email" placeholder="Email"
                           value="${searchModel.keyword }">
                </div>

                <div class="form-group">
                    <button type="submit" class="sub" >Tra cứu đơn hàng</button>
                </div>

                <div class="showOrder">

<%--                    <table class="">--%>
<%--                        <thead class="">--%>
<%--                        <tr class="">--%>
<%--                            <th scope="col">Mã đơn</th>--%>
<%--                            <th scope="col">Email</th>--%>
<%--                            <th scope="col">Tên sản phẩm</th>--%>
<%--                            <th scope="col">Giá sản phẩm</th>--%>
<%--                            <th scope="col">Số lượng</th>--%>
<%--                            <th scope="col">Tổng tiền</th>--%>
<%--                        </tr>--%>
<%--                        </thead>--%>

<%--                        <tbody>--%>

<%--                        <c:forEach items="${orderWithPaging.data}" var="orderProduct"--%>
<%--                                   varStatus="loop">--%>
<%--                            <tr>--%>
<%--                                <td>${orderProduct.saleOrder.code }</td>--%>
<%--                                <td>${orderProduct.saleOrder.customer_email }</td>--%>
<%--                                <td>${orderProduct.product.title }</td>--%>
<%--                                <td>${orderProduct.product.price }</td>--%>
<%--                                <td>${orderProduct.quality }</td>--%>
<%--                                <td>${orderProduct.quality * orderProduct.product.price} </td>--%>
<%--                            </tr>--%>
<%--                        </c:forEach>--%>
<%--                        </tbody>--%>
<%--                    </table>--%>

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