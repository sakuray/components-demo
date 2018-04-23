$(function() {
	$('#denglu').click(function() {
		var userId = $('#userId').val();
		var password = $('#password').val();
		$.post(Config.login,{userId:userId,password:password},function(data,status){
			if(status == "success") {
				console.log(data);
				if(data.code == 1) {
					window.location.href = Config.skip;
				} else {
					alert("密码或用户名不正确",$("#password").val(""));
				}
			} else {
				alert("发生未知错误,F12调试"+status);
			}
		})
	});
	
	$("body").keydown(function(){
		if(event.keyCode == "13") {
			$('#denglu').click();
		}
	});
});