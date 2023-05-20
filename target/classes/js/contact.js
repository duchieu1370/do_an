window.addEventListener("DOMContentLoaded", function() {
	// get the form elements defined in your form HTML above

	var form = document.getElementById("my-form");
	// var button = document.getElementById("my-form-button");
	var status = document.getElementById("status");


	// Success and Error functions for after the form is submitted

	function success() {
		form.reset();
		status.classList.add("success");
		status.innerHTML = "Cảm ơn, chúng tôi sẽ liên hệ sau!";
	}

	function error() {
		status.classList.add("error");
		status.innerHTML = "Bạn chưa nhập thông tin cá nhân!";
	}

	// handle the form submission event

	form.addEventListener("submit", function(ev) {
		ev.preventDefault();
		var data = new FormData(form);
		ajax(form.method, form.action, data, success, error);
	});
});

// helper function for sending an AJAX request

function ajax(method, url, data, success, error) {
	var xhr = new XMLHttpRequest();
	xhr.open(method, url);
	xhr.setRequestHeader("Accept", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState !== XMLHttpRequest.DONE) return;
		if (xhr.status === 200) {
			success(xhr.response, xhr.responseType);
		} else {
			error(xhr.status, xhr.response, xhr.responseType);
		}
	};
	xhr.send(data);
}

//test text null hay không?
/*function check() {
	var data = new Array();
	data[0] = document.getElementById('name').value;
	data[1] = document.getElementById('email').value;
	data[2] = document.getElementById('massage').value;

	var myerror = new Array();
	myerror[0] = "<span style=’color:red,font-weight:bold’>Bạn chưa nhập tên</span>";
	myerror[1] = "<span style=’color:red,font-weight:bold’>Bạn chưa nhập email</span>";
	myerror[2] = "<span style=’color:red,font-weight:bold’>Bạn chưa nhập ghi chu</span>";

	var nearby = new Array("z-name", "z-email", "z-massage");

	for (i in data) {
		var error = myerror[i];
		var div = nearby[i];


		if (data[i] == "") {
			document.getElementById(div).innerHTML = error;
		} else {
			document.getElementById(div).innerHTML = "OK!";
		}
	}
}*/

