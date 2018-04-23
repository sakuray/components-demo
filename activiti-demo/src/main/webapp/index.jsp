<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>工作流展示</title>
</head>
<body>
	<h2 align="center">创建模型</h2>
	<form action="<%=request.getContextPath()%>/activiti/manage/model/create" method="post">
		模型名称：<input name="name" type="text">
		模型关键字：<input name="key" type="text">
		模型描述:<input name="description" type="text">
		<input type="submit" value="提交">
	</form>
	
</body>
</html>
