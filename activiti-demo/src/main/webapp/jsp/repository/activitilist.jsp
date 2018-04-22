<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>流程管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/themes/default/easyui.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/icon.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/demo.css">
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/Config.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/repository/activiti.js"></script>
</head>
<body>
	<table id="activitiTab" >
	</table>
</body>
</html>