<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>

<!-- SPRING FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!doctype html>
<html class="no-js" lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<title>Product</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<jsp:include page="/WEB-INF/views/quantrivien/layouts/css.jsp"></jsp:include>

</head>

<body>
<!--[if lt IE 8]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->
<!-- preloader area start -->
<div id="preloader">
	<div class="loader"></div>
</div>
<!-- preloader area end -->
<!-- page container area start -->
<div class="page-container">
	<!-- sidebar menu area start -->
	<jsp:include page="/WEB-INF/views/quantrivien/layouts/sidebar.jsp"></jsp:include>
	<!-- sidebar menu area end -->
	<!-- main content area start -->
	<div class="main-content">
		<!-- header area start -->
		<jsp:include page="/WEB-INF/views/quantrivien/layouts/header.jsp"></jsp:include>
		<!-- header area end -->
		<!-- page title area start -->
		<<jsp:include page="/WEB-INF/views/quantrivien/layouts/title.jsp"></jsp:include>
		<!-- page title area end -->
		<form class="list" action="${base }/admin/product" method="get">
			<div class="main-content-inner">
				<!-- sales report area start -->

				<div class="card-body"
					 style="display: flex; justify-content: space-between">
					<div style="display: flex; padding-right: 15px">
						<input type="hidden" id="page" name="page" class="form-control">
						<input type="text" id="keyword" name="keyword"
							   class="form-control" placeholder="Search"
							   value="${searchModel.keyword }"
							   style="margin-right: 5px; height: 46px;"> <select
							class="form-control" name="categoryId" id="categoryId"
							style="margin-right: 5px; height: 46px;">
						<c:forEach items="${categories}" var="category">
							<option value="${category.id }">${category.name }</option>
						</c:forEach>
					</select>
						<button type="submit" id="btnSearch" name="btnSearch"
								value="Search" class="btn btn-flat btn-outline-secondary mb-3">Search</button>
					</div>
					<div>
						<button type="button"
								class="btn btn-flat btn-outline-secondary mb-3">
							<a href="${base }/admin/product/add-product"> Thêm mới sản phẩm</a>
						</button>

						<button type="button"
								class="btn btn-flat btn-outline-secondary mb-3">
							<a href="${base }/admin/product/add-category"> Thêm mới danh mục</a>
						</button>
					</div>
				</div>
			</div>
			<!-- Dark table start -->
			<!-- Dark table end -->

			<div class="single-table"
				 style="margin: 0px 30px; padding-bottom: 15px">
				<div class="table-responsive">
					<table class="table text-center">
						<thead class="text-uppercase bg-primary">
						<tr class="text-white">
							<th scope="col">ID</th>
							<th scope="col">Title</th>
							<th scope="col">Price</th>
							<th scope="col">Category</th>
							<th scope="col">Status</th>
							<th scope="col">Avatar</th>
							<th scope="col">Action</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${productsWithPaging.data}" var="product"
								   varStatus="loop">
							<tr>
								<th scope="row">${loop.index + 1}</th>
								<td>${product.title }</td>
								<td><fmt:setLocale value="vi_VN" scope="session" /> <fmt:formatNumber
										value="${product.price }" type="currency" /></td>
								<td>${product.categories.name }</td>
								<td><span id="_product_status_${product.id} }"> <c:choose>
									<c:when test="${product.status }">
												<input type="checkbox" checked="checked"
													   readonly="readonly">
									</c:when>
									<c:otherwise>
										<input type="checkbox" readonly="readonly">
									</c:otherwise>
								</c:choose>
										</span></td>
								<td><img src="${base }/upload/${product.avatar}"
										 width="100" height="100"></td>
								<td><a class="btn btn-primary"
									   href="${base }/admin/product/add-product/${product.id}" role="button">Edit</a>
									<a class="btn btn-danger" role="button"
									   href="${base }/delete/${product.id}">Delete</a></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>

				</div>
			</div>
			<!-- Paging -->
			<div class="row">
				<div class="col-12 d-flex justify-content-center">
					<div id="paging"></div>
				</div>
			</div>
		</form>
	</div>

	<!-- main content area end -->
	<!-- footer area start-->
	<footer>
		<div class="footer-area">
			<p>
				© Copyright 2018. All right reserved. Template by <a
					href="https://colorlib.com/wp/">Colorlib</a>.
			</p>
		</div>
	</footer>
	<!-- footer area end-->
</div>
<!-- page container area end -->
<!-- offset area start -->
<jsp:include page="/WEB-INF/views/quantrivien/layouts/offset.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/quantrivien/layouts/js.jsp"></jsp:include>
</body>
<script type="text/javascript">
	function DeleteProduct(productId) {
		<!--var element = ${product.id};
	element.classList.remove(${product.id});-->
	}

	$( document ).ready(function() {
		$("#categoryId").val(${searchModel.categoryId});
		$("#paging").pagination({
			currentPage: ${productsWithPaging.currentPage},
			items: ${productsWithPaging.totalItems},
			itemsOnPage: 20,
			cssStyle: 'dark-theme',
			onPageClick: function(pageNumber, event) {
				$('#page').val(pageNumber);
				$('#btnSearch').trigger('click');
			},
		});
	});
</script>

</html>
