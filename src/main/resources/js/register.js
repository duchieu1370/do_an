var username = document.querySelector('#username')
var email = document.querySelector('#email')
var password = document.querySelector('#password')
var confirmPassword = document.querySelector('#cf-password')
var form = document.querySelector('form')

function showError(input, message){
  let parent = input.parentElement;
  let small = parent.querySelector('small')

  parent.classList.add('error')
  small.innerText = message
}

function showSuccess(input){
  let parent = input.parentElement;
  let small = parent.querySelector('small')

  parent.classList.remove('error')
  small.innerText = ''
}

function checkEmptyError(listInput){
  let isEmptyError = false;
  listInput.forEach(input => {
    input.value = input.value.trim();

    if(!input.value){
      isEmptyError = true;
      showError(input, 'Khong duoc de trong')
    }else{
      showSuccess(input)
    }
  });

  return isEmptyError
}

function checkEmailError(input){
  const regexEmail = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
  input.value = input.value.trim();
  let isEmailError = !regexEmail.test(input.value)

  if(regexEmail.test(input.value)){
    showSuccess()
  }else {
    showError(input, 'Email Invalid')
  }

  return isEmailError
}

function checkLengthError(input, min, max){
  input.value = input.value.trim();

  if(input.value.length < min){
    showError(input, 'Phai co it nhat 6 ky tu')
    return true
  }

  if(input.value.length > max){
    showError(input, 'Khong duoc qua 20 ky tu')
    return true
  }

  return false
}

function checkMatchPasswordError(passwordInput, cfPasswordInput){
  if(passwordInput.value !== cfPasswordInput.value){
    showError(cfPasswordInput, 'Mat khau khong trung khop')
    return true
  }

  return false
}

form.addEventListener('submit', function (e){
  e.preventDefault()

  let isEmptyError = checkEmptyError([username, email, password, confirmPassword])
  let isEmailError = checkEmailError(email)
  let isUsernameLengthError = checkLengthError(username, 6, 20)
  let isPasswordLengthError = checkLengthError(password, 6, 20)
  let isConfirmPasswordLengthError = checkLengthError(confirmPassword, 6, 20)
  let isCheckMatchPasswordError = checkMatchPasswordError(password, confirmPassword)

  if(isEmptyError || isEmailError || isUsernameLengthError || isPasswordLengthError || isConfirmPasswordLengthError || isCheckMatchPasswordError){
    //do nothing
  }else{
    //logic
  }
})
