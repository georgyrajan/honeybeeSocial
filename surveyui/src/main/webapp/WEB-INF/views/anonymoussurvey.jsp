<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
 <link  rel="stylesheet" href="survey.css">
</head>
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
:root {
  --radius: 2px;
  --baseFg: dimgray;
  --baseBg: white;
  --accentFg: #006fc2;
  --accentBg: #bae1ff;
}

.theme-pink {
  --radius: 2em;
  --baseFg: #c70062;
  --baseBg: #ffe3f1;
  --accentFg: #c70062;
  --accentBg: #ffaad4;
}

.theme-construction {
  --radius: 0;
  --baseFg: white;
  --baseBg: black;
  --accentFg: black;
  --accentBg: orange;
}

select {
  font: 400 12px/1.3 sans-serif;
  -webkit-appearance: none;
  appearance: none;
  color: var(--baseFg);
  border: 1px solid var(--baseFg);
  line-height: 1;
  outline: 0;
  padding: 0.65em 2.5em 0.55em 0.75em;
  border-radius: var(--radius);
  background-color: var(--baseBg);
  background-image: linear-gradient(var(--baseFg), var(--baseFg)),
    linear-gradient(-135deg, transparent 50%, var(--accentBg) 50%),
    linear-gradient(-225deg, transparent 50%, var(--accentBg) 50%),
    linear-gradient(var(--accentBg) 42%, var(--accentFg) 42%);
  background-repeat: no-repeat, no-repeat, no-repeat, no-repeat;
  background-size: 1px 100%, 20px 22px, 20px 22px, 20px 100%;
  background-position: right 20px center, right bottom, right bottom, right bottom;   
}

select:hover {
  background-image: linear-gradient(var(--accentFg), var(--accentFg)),
    linear-gradient(-135deg, transparent 50%, var(--accentFg) 50%),
    linear-gradient(-225deg, transparent 50%, var(--accentFg) 50%),
    linear-gradient(var(--accentFg) 42%, var(--accentBg) 42%);
}

select:active {
  background-image: linear-gradient(var(--accentFg), var(--accentFg)),
    linear-gradient(-135deg, transparent 50%, var(--accentFg) 50%),
    linear-gradient(-225deg, transparent 50%, var(--accentFg) 50%),
    linear-gradient(var(--accentFg) 42%, var(--accentBg) 42%);
  color: var(--accentBg);
  border-color: var(--accentFg);
  background-color: var(--accentFg);
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
<body>
<script>
function saveAnonymCustomerAnswer() {
		var customerDTOJS = JSON.parse('${customerAnswerStr}');
		var questions = customerDTOJS.surveyDetails.questions;
		var text = "";
		for (var i = 0; i < questions.length; i++) {
			var question = questions[i];
			var answers = questions[i].answers;
			
			if (question.inputtype == 'text') {

				for (var j = 0; j < answers.length; j++) {
					var id = answers[j].id + '-' + question.code;
					var answertxt = document.getElementById(id).value;
					answers[j].answer = answertxt;
				}

			}
			if (question.inputtype == 'select') {
				var id = question.name + '-' + question.code;
				var answer = document.getElementById(id).value;
				for (var j = 0; j < answers.length; j++) {
					if(answer == answers[j].text){
						answers[j].priority='selected';
					} else if(answers[j].priority=='selected'){
						answers[j].priority='notSelected';
					}
				}
			}
			if (question.inputtype == 'checkbox') {
				for (var j = 0; j < answers.length; j++) {
					var id = answers[j].id + '-' + question.code;
					var answer = document.getElementById(id).checked;
					if(answer==true){
						answers[j].priority='selected';
					} else {
						answers[j].priority='notSelected';
					}
				}
			}

		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/survey/saveanswer");
		xhr.setRequestHeader("Accept", "application/json");
		xhr.setRequestHeader("Content-Type", "application/json");

		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status==200) {
				   window.location.assign("/survey/done")
			}};

		
		xhr.send(JSON.stringify(customerDTOJS));
	}
</script>
<form onsubmit="void(0)" style="border:1px solid #ccc">
  <div class="container" id="anonymcontainer" style="border:1px solid black;margin-left:auto;margin-right:auto;margin-top: auto;">>
    <h1 align="center">${customerAnswerDTO.surveyDetails.name}</h1>
    <hr>
	<c:forEach var="question" items="${questions}">

		<c:if test = "${question.inputtype == 'text'}">
		<div class="questions">
				<label for="${question.name}"><b>${question.text}</b> </label>
				<c:forEach items="${question.answers}" var="answer">
		         <input type="text" placeholder="${question.text}" id="${answer.id}-${question.code}" name="${question.name}">
		         </c:forEach>
		  </div>
		 </c:if>
		 <c:if test = "${question.inputtype == 'checkbox'}">
		 <div class="questions">
				<b>${question.text}</b> <br>
				<c:forEach items="${question.answers}" var="answer">
				<c:if test="${answer.priority != 'selected'}">
		         <input type="checkbox" value="${answer.text}" id="${answer.id}-${question.code}" name="${answer.id}-${question.code}">
		         </c:if>
		         <c:if test="${answer.priority =='selected'}">
		         <input type="checkbox" value="${answer.text}" id="${answer.id}-${question.code}" name="${answer.id}-${question.code}" checked="checked">
		         </c:if>
		         <label for="${answer.id}-${question.code}">${answer.text}</label><br>
		         </c:forEach>
		          <br>
		  </div>
		 </c:if>
		<c:if test = "${question.inputtype == 'select'}">
		<div class="questions">
				<label for="${question.name}"><b>${question.text}</b> </label><br>
				 <br>
		         <select name="${question.name}" id="${question.name}-${question.code}">
   					
  					  <c:forEach items="${question.answers}" var="answer">
      					  <c:if test="${answer.priority != 'selected'}">
        				    <option value="${answer.text}">${answer.text}</option>
       				 </c:if>
       				 <c:if test="${answer.priority =='selected'}">
        				    <option value="${answer.text}" selected>${answer.text}</option> 
       				 </c:if>
    				</c:forEach>
				</select>
				 <br>
				 <br>
			</div>	
		 </c:if>
	</c:forEach>

    <input type="hidden" name="code"  hidden=true  value="<%
    		out.print(request.getParameter("code"));
    %>">
    <input type="hidden" name="version"  hidden=true value="<%
    		out.print(request.getParameter("version"));
    %>">

       
    <div class="clearfix">
      <button type="button" class="signupbtn" style='background-color: blue;' onclick="saveAnonymCustomerAnswer()">submit</button>
      <!-- <a href="signup"><button type="button" class="signupbtn">save for later</button></a> -->
    </div>
  </div>
</form>
</body>
</html>