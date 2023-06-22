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

/*function*/
// Lấy giá trị của một input
// function getValue(id){
// 	return document.getElementById(id).value.trim();
// }
//
// // Hiển thị lỗi
// function showError(key, mess){
// 	document.getElementById(key + '_error').innerHTML = mess;
// }
//
// function validate()
// {
// 	var flag = true;
//
// 	// 1 username
// 	var name = getValue('name');
// 	if (name == '' || name.length < 5 || !/^[a-zA-Z0-9]+$/.test(name)){
// 		flag = false;
// 		showError('name', 'Vui lòng kiểm tra lại Username');
// 	}
//
// 	return flag;
// }

