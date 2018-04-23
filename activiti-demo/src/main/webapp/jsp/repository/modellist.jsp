<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>模型管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/themes/default/easyui.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/icon.css">
	<link rel="stylesheet" type="test/css" href="<%=request.getContextPath() %>/themes/demo.css">
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/Config.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/repository/model.js"></script>
</head>
<body >
	<table id="modeltab" >
	</table>
	
	<div id = "addModel" class="easyui-window" title="创建模型" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 200px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
               		<tr>
                        <td>模型名称：</td>
                        <td><input id="name" type="text" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>模型key：</td>
                        <td><input id="key" type="text" class="txt01" /></td>
                    </tr>
                    <tr>
                    	<td>模型描述：</td>
                    	<td><textarea rows="10" style="width: 100%;resize:none;" id="description"></textarea> </td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <button id="modelQ" class="easyui-linkbutton" icon="icon-ok"  >确定</button> 
                <button id="modelC" class="easyui-linkbutton" icon="icon-cancel" >取消</button>
            </div>
        </div>
   	</div>
</body>
</html>