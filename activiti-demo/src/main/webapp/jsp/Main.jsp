<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>工作流管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/themes/default/easyui.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/icon.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/demo.css">
	
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/Config.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/main.js"></script>
</head>
<body class="easyui-layout" style="overflow: hidden" scroll="no">
	<div region="north" split="true" border="false" style="overflow: hidden; height: 40px;">
		<div align="right" >
			<a href="javascript:void(0);" id="loginOut" style="margin-top: 10px">注销</a>
		</div>
	</div>
	<div region="south" split="true" style="height: 40px; background: #D2E0F2; ">
		<p align="center">Copyright ©2016 nobody</p>
	</div>
	<div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
    	<div id='nav' class="easyui-accordion" fit="true" border="false">
    		<!--  导航内容 -->
    		<div title="模型管理" data-options="iconCls:'icon-save'" style="overflow:auto;padding:10px;">
    			<ul>
    				<li><a href="javascript:void(0);" url="modelview" icon="iconCls:'icon-blank'">模型列表</a></li>
    				<li style="margin-top: 10px;"><a href="javascript:void(0);" url="activitiview" icon="iconCls:'icon-blank'">流程定义及部署管理</a></li>
    			</ul>
    		</div>
    		
    		<div title="流程管理" data-options="iconCls:'icon-save'" style="overflow:auto;padding:10px;">
    			<ul>
    				<li><a href="javascript:void(0);" url="processview" icon="iconCls:'icon-blank">运行中流程</a></li>
    				<li style="margin-top: 10px;"><a href="javascript:void(0);" url="endview" icon="iconCls:'icon-blank">运行结束流程</a></li>
    			</ul>
    		</div>
    		
    		<div title="用户管理" data-options="iconCls:'icon-save'" style="overflow:auto;padding:10px;">
    		
    		</div>
    	</div>
    </div>
	<div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
            <div title="首页简介" style="padding:20px;overflow:hidden;" id="home">
                <div style="padding: 5px;">
                   	<p>暂无公告</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 右击菜单 -->
    <div id="mm" class="easyui-menu" style="width:150px;">
    	<div id="mm-tabupdate">刷新</div>
        <div class="menu-sep"></div>
        <div id="mm-tabclose">关闭</div>
        <div id="mm-tabcloseall">全部关闭</div>
        <div id="mm-tabcloseother">除此之外全部关闭</div>
        <div class="menu-sep"></div>
        <div id="mm-tabcloseright">当前页右侧全部关闭</div>
        <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
        <div class="menu-sep"></div>
        <div id="mm-exit">退出</div>
    </div>
</body>
</html>