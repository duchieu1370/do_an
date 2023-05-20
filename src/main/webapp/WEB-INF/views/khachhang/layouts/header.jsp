<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!-- import JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="main-header">

	<div class="header">

		<div class="title">
			<a href="${base }/home"> <img src="${base }/img/title.png">
			</a>
		</div>
		<div class="search">
			<input type="text" class="searchTerm"
				placeholder="Tìm kiếm sản phẩm...">
			<button type="submit" class="searchButton">
				<i class="fa fa-search"></i>
			</button>
		</div>
		<div class="form-login">
			<div class="item-icon">
				<a href="${base }/login"> <i class="far fa-address-book"></i>
				</a>
			</div>
			<div class="form-login-content">
				<form class="cart-flex">
					<!-- đang có vấn đề -->
					<button type="submit"
						style="background-color: white; border: 1px solid white;">
						<div class="sign-in">
							<a href="${base }/login" style="text-decoration: none;">Đăng
								nhập / Đăng ký</a>
						</div>
						<div class="acount">
							<a href="${base }/login" style="text-decoration: none;">Tài
								khoản của tôi <i class="fas fa-angle-down"></i>
							</a>
						</div>
					</button>

					<c:if test="${isLogined }">
						<button type="submit"
							style="background-color: white; border: 1px solid white;">
							<div class="acount">
								<i class="far fa-address-book"></i>${userLogined.email }</div>

						</button>
					</c:if>

				</form>
			</div>
		</div>
		<div class="carts">

			<form class="cart-flex">
				<button type="submit"
					style="background-color: white; border: 1px solid white;">
					<ul>
						<li><a href="${base }/cart/view"><i
								class="fas fa-cart-plus"></i></a></li>
						<li><a href="${base }/cart/view">Giỏ hàng</a></li>
						<li><span id="iconShowTotalItemsInCart">${totalItems }</span>
						</li>
					</ul>
				</button>


			</form>

		</div>
	</div>


	<!--open nav-->
	<div class="nav">
		<ul class="nav-chinh">
			<li><a href="${base }/home">Trang chủ</a></li>
			<li id="all-product"><a href="${base }/collection">Tất cả
					sản phẩm <i class="fas fa-angle-down"></i>
			</a>
				<ul class="all-control">
					<c:forEach items="${categories}" var="category">

						<li><a
							href="<c:url value="${base }/collection/${category.id }" />">${category.name }</a></li>
					</c:forEach>

				</ul></li>
			<li><a href="#">Blog</a></li>
			<li><a href="${base }/introduction">Giới thiệu</a></li>
			<li><a href="${base }/contact">Liên hệ</a></li>
			<li><a href="#">Kiểm tra đơn hàng</a></li>
		</ul>


	</div>
	<!--close nav-->
</div>
