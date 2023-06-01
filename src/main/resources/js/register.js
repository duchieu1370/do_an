var username = document.querySelector('#username')
var email = document.querySelector('#email')
var password = document.querySelector('#password')
var confirmPassword = document.querySelector('#cf-password')

function showError(input){
  input.parentElement = input.value.trim()
}
