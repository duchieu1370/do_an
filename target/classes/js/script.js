$("#btn-sidebar").on('click', function(event) {
	event.preventDefault();
	$('.menu').css('left', '0px');
});

$(document).mouseup(function(event) {
	var menu = $('.menu');
	if (!menu.is(event.target) && menu.has(event.target).length == 0) {
		menu.css('left', '-320px');
	}
});

/*phân trang*/
