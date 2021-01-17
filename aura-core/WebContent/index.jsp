<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Aura</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<body>
	<div style="position:absolute; left:50%; top:20%; transform: translate(-50%,-20%);">
		<img src="img/logoAura.jpg"/>
	</div>
	<div style="display:inline-block; width:100%; height: 100px;"/>
	<div style="position:absolute; left:50%; top:90%; transform: translate(-50%,-90%); width:400px; height: 200px; text-align:center; overflow:auto;">
		<form name="form" ng-app="speak" ng-controller="controller" ">
			<input type="text" name="text" ng-model="text" autocomplete="off" ng-keyup="$event.keyCode == 13 ? request() : null"/> 
		</form>
<br/>
	<b>	You </b>: 
		<span id="request" ></span>
		<br /><b> Aura</b>: 
		<span id="response" ></span>
	</div>
	<script type="text/javascript">
		var app = angular.module("speak", []);
		app
				.controller(
						"controller",
						function($scope, $http) {

							$scope.request = function() {

								var text = $scope.text;
								document.getElementById("request").innerHTML = text;
								$http
										.post("sendRequest.do", text, {headers : {'Content-Type': 'text/html', 'Accept': 'text/html, text/plain, */*'}})
										.success(
												function(result) {
													//First character uppercase
													
													document
															.getElementById("response").innerHTML = result.charAt(0).toUpperCase() + result.slice(1);
												}).error(
												function(result, statusText,
														responseText) {
													window.alert("inside error"
															+ statusText
															+ responseText);
												});
							};
						});
	</script>
</body>
</html>