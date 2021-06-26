<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.min.css">
<style>
body {
	font-family: Arial, Helvetica, sans-serif;
}

* {
	box-sizing: border-box;
}

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
	opacity: 1;
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
	margin: 5% auto 15% auto;
	/* 5% from the top, 15% from the bottom and centered */
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

.close:hover, .close:focus {
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
	<script type="text/javascript">
$(document).ready(function() {
    $('#customerview').DataTable();
} );
function logout(userId,jwt) {
	var logoutRequest = {
			userId : "",
			jwt : ""
		};
	logoutRequest.userId= userId;
	logoutRequest.jwt=jwt;
	
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/logout");
	xhr.setRequestHeader("Accept", "application/json");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status==200) {
			 window.location.href = '/home'; 

		}
	};
	xhr.send(JSON.stringify(logoutRequest));
	
}


</script>

<header>
<button type="submit" onclick="logout('${userId}','${AuthJWT}')" style="background-color: #f35806;width: 10%;margin-left: 90%;">log out</button>
</header>

	<form onsubmit="void(0)" style="border: 1px solid #ccc" method="get">
		<div class="container">
			<table id="customerview" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>Name</th>
						<th>Code</th>
						<th>Description</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="customerAnswer" items="${customerAnswerList}">
						<tr>
							<td>${customerAnswer.surveyDetails.name}</td>
							<td>${customerAnswer.surveyCode}</td>
							<td>${customerAnswer.surveyDetails.description}</td>
							<c:if test="${customerAnswer.isComplete}">
								<td>completed</td>
							</c:if>
							<c:if test="${not customerAnswer.isComplete}">
								<td>In progress</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form>
</body>
</html>