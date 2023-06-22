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
    <title>Giỏ Hàng - Msic</title>
    <link rel="stylesheet"
          href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
          integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
          crossorigin="anonymous"/>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${base}/css/cart.css">
</head>
<body>
<main class="container">

    <div class="free">Miễn phí vận chuyển với đơn hàng trên 1000k</div>
    <!--open header-->
    <jsp:include page="/WEB-INF/views/khachhang/layouts/header.jsp"></jsp:include>
    <!--close header-->

    <div class="navigation">
        <ul>
            <li><a href="${base }/home">Trang chủ </a></li>

            <li>/</li>

            <li>Giỏ hàng</li>
        </ul>
    </div>
    <div class="content">
        <h3>Giỏ hàng của bạn</h3>
        <p class="content-title-p1">
            Có <span class="content-title-p2">${totalItems }</span> sản phẩm trong giỏ hàng
        </p>
        <!-- start shopping cart table-->
        <div class="cart-table">
            <table id="table">
                <thead>
                <tr>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Product</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Price</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Quantity</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Total</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Remove</div>
                    </th>

                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cart.cartItems }" var="ci">
                    <tr>
                        <th scope="row" class="border-0">
                            <div class="p-2">
                                <img src="${ci.productPictures}" alt="" width="70"
                                     class="img-fluid rounded shadow-sm">
                                <div class="ml-3 d-inline-block align-middle">
                                    <h5 class="mb-0">
                                        <a href="#" class="text-dark "> ${ci.productName } </a>
                                    </h5>

                                </div>
                            </div>
                        </th>

                        <td class="border-0"><strong><fmt:setLocale
                                value="vi_VN"/> <fmt:formatNumber value="${ci.priceUnit}"
                                                                  type="currency"/></strong></td>
                        <td class="border-0">
                            <button type="button"
                                    onclick="UpdateQuanlityCart('${base }',${ci.productId })"
                                    class="cong">+
                            </button>
                            <strong><span id="quanlity_${ci.productId }">${ci.quanlity }</span></strong>

                            <button type="button"
                                    onclick="TruQuanlityCart('${base }',${ci.productId })"
                                    class="tru">-
                            </button>
                        </td>
                        <td class="border-0"><strong><fmt:setLocale
                                value="vi_VN"/> <fmt:formatNumber
                                value="${ci.priceUnit * ci.quanlity}" type="currency"/></strong></td>
                        <td class="border-0"><a
                                href="${base }/cart/remove/${ci.productId}" class="text-dark">
                            <i class="fa fa-trash" id="card-icon"></i>
                        </a></td>
                        <td class="border-0">
                            <a type="button" class="a-update"
                               href="${base }/cart/view">Update</a>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>


        <!-- End -->
        <form action="${base }/cart/checkout" method="post">
            <div>
                <div class="content-desc">
                    <div class="content-desc-p1">
                        <div class="note">
                            <h5>Ghi chú đơn hàng</h5>
                            <textarea rows="8" cols="40" name="note" id="note"
                                      placeholder="Ghi chú"></textarea>
                        </div>

                        <div class="sidebox">
                            <h5>Chính sách mua hàng</h5>
                            <ul>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    được đổi 1 lần duy nhất, không hỗ trợ trả.
                                </li>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    còn đủ tem mác, chưa qua sử dụng.
                                </li>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    nguyên giá được đổi trong 30 ngày trên toàn hệ thống.
                                </li>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    sale chỉ hỗ trợ đổi size (nếu cửa hàng còn) trong 7 ngày trên
                                    toàn hệ thống.
                                </li>
                            </ul>
                        </div>
                    </div>

                </div>

                <div class="content-desc">
                    <div class="form-row">
                        <div class="form-group">
                            <h4>Thông tin khách hàng</h4>
                        </div>

                        <c:if test="${isLogined }">
                            <div class="form-group">
                                <label for="customer_name">Họ và tên</label><br> <input
                                    type="text" class="form-control" id="customer_name"
                                    name="customer_name" placeholder="Name" value="${userLogined.username }">
                            </div>
                            <div class="form-group">
                                <label for="customer_email">Địa chỉ email</label><br> <input
                                    type="email" class="form-control" id="customer_email"
                                    name="customer_email" placeholder="Enter email" value="${userLogined.email }">
                                <!-- <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small> -->
                            </div>
                            <div class="form-group">
                                <label for="customer_phone">Số điện thoại</label><br> <input
                                    type="text" class="form-control" id="customer_phone"
                                    name="customer_phone" placeholder="Phone" value="${userLogined.phone}">
                            </div>
                            <div class="form-group">
                                <label for="customer_address">Địa chỉ</label><br> <input
                                    type="text" class="form-control" id="customer_address"
                                    name="customer_address" placeholder="Address" value="${userLogined.address }">
                            </div>
                        </c:if>

                        <c:if test="${!isLogined }">
                            <div class="form-group">
                                <label for="customer_name">Họ và tên</label><br> <input
                                    type="text" class="form-control" id="customer_name"
                                    name="customer_name" placeholder="Name" >

                            </div>
                            <div class="form-group">
                                <label for="customer_email">Địa chỉ email</label><br> <input
                                    type="email" class="form-control" id="customer_email"
                                    name="customer_email" placeholder="Enter email" >
                            </div>
                            <div class="form-group">
                                <label for="customer_phone">Số điện thoại</label><br> <input
                                    type="text" class="form-control" id="customer_phone"
                                    name="customer_phone" placeholder="Phone">
                            </div>
                            <div class="form-group">
                                <label for="customer_address">Địa chỉ</label><br> <input
                                    type="text" class="form-control" id="customer_address"
                                    name="customer_address" placeholder="Address" >
                            </div>
                        </c:if>
                    </div>
                    <div class="content-desc-p2">
                        <div class="p2-title">
                            <h4>Thông tin đơn hàng</h4>
                        </div>
                        <div class="p2-total">
                            <p>
                                Tổng tiền : <span class="total-price">
                                <fmt:setLocale value="vi_VN"/>
                                <fmt:formatNumber value="${cart.totalPrice }" type="currency"/>
                                </span>
                            </p>
                        </div>
                        <div class="p2-text">
                            <p>
                                Phí vận chuyển sẽ được tính ở trang thanh toán.<br> Bạn
                                cũng có thể nhập mã giảm giá ở trang thanh toán.
                            </p>
                        </div>
                        <div class="p2-action">
                            <button type="submit" class="thanhtoan" >
                                ĐẶT HÀNG
                            </button>
                            <p>
                                <a href="${base }/collection"><i class="fas fa-undo"></i>Tiếp
                                    tục mua hàng</a>
                            </p>
                        </div>
                    </div>
                </div>


            </div>
        </form>


    </div>

    <!--close content-->

    <!--open footer -->
    <jsp:include page="/WEB-INF/views/khachhang/layouts/footer.jsp"></jsp:include>
    <!--close footer-->
    <div class="copyright">
        Copyright <i class="far fa-copyright"></i> <a href="#">msic.</a> <a
            href="#">Powered by Haravan</a>
    </div>
</main>
</body>

<jsp:include page="/WEB-INF/views/khachhang/layouts/js.jsp"></jsp:include>
<script type="text/javascript">
    function cong() {
        var t = document.getElementById("textbox").value;
        document.getElementById("textbox").value = parseInt(t) + 1;
    }

    function tru() {
        var t = document.getElementById("textbox").value;
        document.getElementById("textbox").value = parseInt(t) - 1;
    }
</script>
<script>
    var rIndex, table = document.getElementById("table");
    // check the empty input
    // add Row
    // display selected row data into input text

    function removeSelectedRow() {
        table.deleteRow(rIndex);
        // clear input text
        document.getElementById("row").value = "";
        document.getElementById("").value = "";
    }
</script>
</html>