$(function(){
	$('#addModel').window('close');
	$("#modeltab").datagrid({
		fit: true,
//		fixRowHeight:10,
		fitColumn:true,
		singleSelect:true,
		rownumbers:true,
		columns:[[
			{field:'id',title:'ID',width:80},
			{field:'key',title:'KEY',width:100},
			{field:'name',title:'Name',width:100},
			{field:'version',title:'Version',width:50},
			{field:'createTime',title:'创建时间',width:100},
			{field:'lastUpdateTime',title:'最后更新时间',width:100},
			{field:'metaInfo',title:'元数据',width:400},
			{field:'action',title:'操作',width:300,
				formatter: function(value,row,index) {
					var e = '<a href="'+Config.editmodel+row.id+'">编辑</a>';
					var d = '<a href="javascript:void(0);" onclick="deploy('+row.id+')">部署<a/>';
					var b = '<a href="'+Config.downloadmodel+row.id+'/bpmn">BPMN<a/>';
					var j = '<a href="'+Config.downloadmodel+row.id+'/json">JSON<a/>';
					var de = '<a href="javascript:void(0);" onclick="deleteM('+row.id+')">删除<a/>';
					return e+"  "+d+" "+"导出("+b+"|"+j+") " + de;
				}
			}
		]],
        toolbar:['-',{
        	id:'create',
        	text:'创建',
        	iconCls:'icon-add',
        	handler:createModel
        }],
        onLoadSuccess:function(){
            $('#modeltab').datagrid('clearSelections');// 一定要加上这一句，不然datagrid会记住之前的选择状态，删除时会出现问题
        },
        onBeforeLoad:function(){
            // 加载数据
            ModelLoadData();
        }
	});
});

function ModelLoadData() {
	$.ajax({
		type:'get',
		url:Config.modellist,
		success:function(data) {
			$('#modeltab').datagrid('loadData',data.data);
		}
	});
}

function createModel() {
	 initWin();
	 $('#addModel').window('open');
	 $('#modelQ').unbind('click');
	 $('#modelQ').click(function(){
		 var name=$('#name').val();
		 var key=$('#key').val();
		 var description=$('#description').val();
		 $.post(Config.addmodel,{name:name,key:key,description:description},function(data){
			 $('#addModel').window('close');
			 if(data.code == 1) {
				 ModelLoadData();
				 var url = Config.editmodel + data.data;
				 window.location.href=url;
			 } else {
				 alert(data.message);
			 }
		 });
	 });
	 $('#modelC').click(function () {
		 $('#addModel').window('close');
	 });
}

function initWin() {
    $('#addModel').window({
        title:"创建模型",
        width:300,
        top:100,
        modal: true,
        shadow: true,
        closed: true,
        height: 350,
        resizable:false,
        onBeforeClose: function() {
        	$('#name').val('');
        	$('#key').val('');
        	$('#description').val('');
        }
    });
}

function deploy(id) {
	$.ajax({
		url:Config.deploymodel+id,
		type:'get',
		success:function(data) {
			alert(data.message);
		}
	});
}

function deleteM(id) {
	$.ajax({
		url:Config.deletemodel+id,
		type:'get',
		success:function(data) {
			alert(data.message);
			ModelLoadData();
		}
	});
}