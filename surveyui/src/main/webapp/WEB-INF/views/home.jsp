<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box;}

/* Full-width input fields */
input[type=text], input[type=password] {
  width: 100%;
  padding: 15px;
  margin: 5px 0 22px 0;
  display: inline-block;
  border: none;
  background: #f1f1f1;
}

/* Add a background color when the inputs get focus */
input[type=text]:focus, input[type=password]:focus {
  background-color: #ddd;
  outline: none;
}

/* Set a style for all buttons */
button {
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  opacity: 0.9;
}

button:hover {
  opacity:1;
}

/* Extra styles for the cancel button */
.cancelbtn {
  padding: 14px 20px;
  background-color: #f44336;
}

/* Float cancel and signup buttons and add an equal width */
.cancelbtn, .signupbtn {
  float: left;
  width: 50%;
}

/* Add padding to container elements */
.container {
  padding: 16px;
}

/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: #474e5d;
  padding-top: 50px;
}

/* Modal Content/Box */
.modal-content {
  background-color: #fefefe;
  margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
  border: 1px solid #888;
  width: 80%; /* Could be more or less, depending on screen size */
}

/* Style the horizontal ruler */
hr {
  border: 1px solid #f1f1f1;
  margin-bottom: 25px;
}
 
/* The Close Button (x) */
.close {
  position: absolute;
  right: 35px;
  top: 15px;
  font-size: 40px;
  font-weight: bold;
  color: #f1f1f1;
}

.close:hover,
.close:focus {
  color: #f44336;
  cursor: pointer;
}

/* Clear floats */
.clearfix::after {
  content: "";
  clear: both;
  display: table;
}

/* Change styles for cancel button and signup button on extra small screens */
@media screen and (max-width: 300px) {
  .cancelbtn, .signupbtn {
     width: 100%;
  }
}
</style>
</head>
<body>
<script>
function login() {

	var loginRequest = {
			userName : "",
			password : ""
		};
		loginRequest.userName = document.getElementsByName("userName")[0].value;
		loginRequest.password = document.getElementsByName("password")[0].value;
		var code = document.getElementById("code").value;
		var version = document.getElementById("version").value;
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/login?code=" + code + "&version=" + version);
		xhr.setRequestHeader("Accept", "application/json");
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.onreadystatechange = function() {
			if (xhr.readyState === 4 && xhr.status==200) {
				console.log(xhr.status);
				console.log(xhr.responseText);

			}
		};

		xhr.send(JSON.stringify(loginRequest));
	}
</script>
<form action="login?code=${code}&version=${version}" style="border:1px solid #ccc" method="post">
  <div class="container">
    <h1 align="center">log in</h1>
    
    <hr>

    <label for="userName"><b>User Name</b></label>
    <input type="text" placeholder="Enter User Name" name="userName" required>
    
    <label for="pass"><b>Password</b></label>
    <input type="password" placeholder="Enter Password" name="password" required>
    <br>
    <c:if test="${not empty error}}">
 	  <span style="color:red"> <c:out value="${error}" ></c:out></span>
    </c:if>

       
    <div class="clearfix">
      <button type="submit" class="signupbtn" style='background-color: blue;'>login</button>
      <c:if test="${not empty code}">
      <a href="unauthsurvey?code=${code}&version=${version}"><button type="button" class="signupbtn">Proceed Without login</button></a>
      </c:if>
    </div>
  </div>
</form>
</body>
</html>