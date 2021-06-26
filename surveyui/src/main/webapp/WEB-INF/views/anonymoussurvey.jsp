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
		xhr.open("POST", "/saveanswer");
		xhr.setRequestHeader("Accept", "application/json");
		xhr.setRequestHeader("Content-Type", "application/json");

		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status==200) {
				   window.location.assign("/done")
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