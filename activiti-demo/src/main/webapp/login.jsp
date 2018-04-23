<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>登陆系统</title>
	<script type="text/javascript" src="./js/jquery.min.js"></script>
	<script type="text/javascript" src="./js/Config.js"></script>
	<script type="text/javascript" src="./js/login.js"></script>
</head>
<body>
	<div align="center" style="margin-top: 100px;">
		<form id="login" method="post">
			<table>
				<tr>
					<td><label>用户名</label></td>
					<td><input type="text" id="userId"/></td>
				</tr>
				<tr>
					<td>密&nbsp;码</td>
					<td><input type="password" id="password"/></td>
				</tr>
				<tr>
					<td><input type="reset" value="重置" align="middle"/></td>
					<td align="right"><input type="button" value="登陆" id="denglu"/></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>