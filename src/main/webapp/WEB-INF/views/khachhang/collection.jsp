<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Tất cả sản phẩm</title>
<link rel="stylesheet"
	href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
	integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
	crossorigin="anonymous" />
<jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="${base}/css/collection.css">
<style>
.pagination {
	display: flex;
    justify-content: center;

}

.pagination a {
	color: black;
	float: left;
	padding: 8px 16px;
	text-decoration: none;
	transition: background-color .3s;
	border: 1px solid #ddd;
}

.pagination a.active {
	background-color: #ff5f17;
	color: white;
	border: 1px solid #ff5f17;
}

.pagination a:hover:not(.active) {
	background-color: #ff5f17;
}
</style>
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

				<li><a href="#">Danh mục</a></li>

				<li>/</li>

				<li>Tất cả sản phẩm</li>
			</ul>
		</div>
		<!-- open content -->
		<div class="content">

			<div class="content-bar">
				<div class="title-bar">
					<div class="title-bar-p1">THƯƠNG HIỆU</div>
					<div class="filter-price">
						<ul class="checkbox-list">
							<li><input type="checkbox" id="data-brand-p1" value="Khác"
								name="brand-filter"> <label for="data-brand-p1">Khác</label>
							</li>
						</ul>
					</div>
				</div>

				<div class="title-bar">
					<div class="title-bar-p1">GIÁ SẢN PHẨM</div>
					<div class="filter-price">
						<ul class="checkbox-list">
							<li><input type="checkbox" id="p1" name="cc"
								data-price="(price:product<=300000)"> <label for="p1">Dưới
									300,000đ</label></li>
							<li><input type="checkbox" id="p2" name="cc"
								data-price="((price:product>)&amp;&amp;(price:product<=))">
								<label for="p2">0đ - 0đ</label></li>
							<li><input type="checkbox" id="p3" name="cc"
								data-price="((price:product>)&amp;&amp;(price:product<=))">
								<label for="p3">0đ - 0đ</label></li>
							<li><input type="checkbox" id="p4" name="cc"
								data-price="((price:product>)&amp;&amp;(price:product<=))">
								<label for="p4">0đ - 0đ</label></li>
							<li><input type="checkbox" id="p5" name="cc"
								data-price="(price:product>=3000000)"> <label for="p5">Trên
									300,000đ</label></li>
						</ul>
					</div>
				</div>
				<div class="title-bar-price">
					<div class="title-bar-p1">Size</div>
					<div class="filter-price">
						<ul class="price">
							<li>
								<button type="button" id="size">M</button>
							</li>
							<li>
								<button type="button" id="size">L</button>
							</li>
							<li>
								<button type="button" id="size">XL</button>
							</li>
						</ul>
					</div>
				</div>
			</div>

			<form class="list" action="${base }/collection" method="get">
				<div class="content-collection">
					<div class="collection-title">
						<div class="all-coll">
							<h3>Tất cả sản phẩm</h3>
						</div>
						<div class="card-body">
							<div class="card-body-a">
								
								<input type="text" id="keyword" name="keyword"
									class="form-control" placeholder="Search"
									value="${searchModel.keyword }">
								<button type="submit" id="btnSearch" name="btnSearch"
									value="Search" class="btn btn-flat btn-outline-secondary mb-3">Search</button>
							</div>
						</div>
					</div>
					
					<div class="list-product" style="flex-wrap: wrap;">

						<c:forEach items="${productsWithPaging.data }" var="product">
							<div class="item" style="margin-left: 52px;">

								<div class="item-images">
									<a href="${base }/product/details/${product.seo}"> <img src="${base }/upload/${product.avatar}"
										width="100%">
									</a>
								</div>
								<div class="item-content">
									<a href="${base }/product/details/${product.seo}">
										${product.title } </a>
									<div class="price">
										<fmt:setLocale value="vi_VN" />
										<fmt:formatNumber value="${product.price}" type="currency" />
									</div>
								</div>
							</div>
						</c:forEach>

					</div>
					<!-- Paging -->
							<div class="pagination" >
								<a href="#">&laquo;</a> 
								<a href="#">1</a> 
								<a href="#" class="active">2</a> 
								<a href="#">3</a> 
								<a href="#">4</a> 
								<a href="#">5</a> 
								<a href="#">6</a> 
								<a href="#">&raquo;</a>
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
	
</script>
</html>