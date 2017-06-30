<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>注册页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/regist.css'/>">

	<!-- <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script> -->
<!-- 	<script type="text/javascript" src="<c:url value='/jsps/js/user/regist.js'/>"></script>   -->
	<script type="text/javascript" src= "<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src=  "<c:url value='/jsps/js/user/regist.js'/>"></script>
	
  </head>
  
  <body>
	<div id="divMain">
		<div id="divTitle">
			<span id="spanTitle">新用户注册</span>
		</div>
		<div id="divBody">
		<form action= "<c:url value='/UserServlet'/>" method="post" id="registForm">
			<input type="hidden" name="method" value="regist" /> <!-- 就相当于 /UserServlet?method=regist -->
			<table id="tableForm" >
			<tr >
				<td class="tdText">
				用户名：
				</td>
				<td class="tdInput">
				<input class="inputClass" type="text" name="loginname" id="loginname" value="${form.loginname }"/>
				</td>
				<td class="tdError">
				<label id="loginnameError" class="errorClass">${errors.loginname }</label>
				</td>
			</tr>
			<tr>
				<td class="tdText">
				登录密码：
				</td>
				<td class="tdInput">
				<input class="inputClass" type="password" name="loginpass" id="loginpass" value="${form.loginpass }"/>
				</td>
				<td class="tdError">
				<label id="loginpassError" class="errorClass">${errors.loginpass }</label>
				</td>
			</tr><tr>
				<td class="tdText">
				确认密码：
				</td>
				<td>
				<input class="inputClass" type="password" name="reloginpass" id="reloginpass" value="${form.reloginpass }"/>
				</td>
				<td>
				<label id="reloginpassError" class="errorClass">${errors.reloginpass }</label>
				</td>
			</tr><tr>
				<td class="tdText">
				Email：
				</td>
				<td>
				<input class="inputClass" type="text" name="email" id="email" value="${form.email }"/>
				</td>
				<td>
				<label id="emailError" class="errorClass">${errors.email }</label>
				</td>
			</tr><tr>
				<td class="tdText">
				验证码：
				</td>
				<td>
				<input class="inputClass" type="text" name="verifyCode" id="verifyCode" value= "${form.verifyCode}"/>
				</td>
				<td>
				<label id="verifyCodeError" class="errorClass">${errors.verifyCode }</label>
				</td>
			</tr><tr>
				<td class="tdText">
				
				</td>
				<td><div id="divVerifyCode">
				<img id ="imgVerifyCode" src="<c:url value='/VerifyCodeServlet' />"/ >
				</div>
				
				</td>
				<td>
				<div><a href= "javascript:_changeImage()" >换一张</a></div>
				</td>
			</tr><tr>
				<td>
				
				</td>
				<td>
				<input   id="submitBtn" type="image" src= "<c:url value='/images/regist1.jpg'/>"/>
				</td>
				<td>
				<label id="imageError" ></label>
				</td>
			</tr>
			
			</table>
			</form>
		</div>
	</div>
  </body>
</html>
