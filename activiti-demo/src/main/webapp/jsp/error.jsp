<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>错误信息</title>
</head>
<body>
	<label>你还没有登录</label>
	<p>将在<span id="mess">2</span>秒后返回登录界面</p>
	<script type="text/javascript" src="<%= request.getContextPath()%>/js/Config.js"></script>
	<script type="text/javascript">
		var i = document.getElementById("mess").innerHTML;
		var intervalid;
		intervalid = setInterval("fun()", 1000);
		function fun() {
			if(i==0) {
				window.location.href = Config.project;
				clearInterval(intervalid);
			}
			document.getElementById("mess").innerHTML = i;
			i--;
		}
	</script>
</body>
</html>