$(function(){
	$('#activitiTab').datagrid({
		fit:true,
		fitColumn:true,
		singleSelect:true,
		rownumbers:true,
		columns:[[
					{field:'id',title:'流程定义ID',width:150},
					{field:'deploymentId',title:'部署ID',width:60},
					{field:'name',title:'名称',width:100},
					{field:'key',title:'KEY',width:100},
					{field:'version',title:'版本号',width:50},
					{field:'xml',title:'XML',width:200,
						formatter: function(value,row,index) {
							var x = '<a target="_blank" href="'+Config.resource+'?processDefinitionId='+row.id+'&resourceType=xml">'
								+row.resourceName+'</a>';
							return x;
						}
					},
					{field:'picture',title:'图片',width:200,
						formatter: function(value,row,index) {
							var p = '<a target="_blank" href="'+Config.resource+'?processDefinitionId='+row.id+'&resourceType=image">'
								+row.diagramResourceName+'</a>';
							return p;
						}
					},
					{field:'deploymentTime',title:'部署时间',width:100},
					{field:'suspended',title:'是否挂起',width:100,
						formatter: function(value,row,index) {
							var s = row.suspended;
							if(s) {
								s += ' <a href="javascript:void(0);" onclick="activeA('+'\'active\''+",\'"+row.id+'\')">激活<a/>';
//								s += '| <a href="' + Config.suspended +"/active/" + row.id +'">激活</a>';
							} else {
//								s += '| <a href="' + Config.suspended +"/suspend/" + row.id +'">挂起</a>';
								s += ' <a href="javascript:void(0);" onclick="activeA('+'\'suspend\''+",\'"+row.id+'\')">挂起<a/>';
							}
							return s;
						}
					},
					{field:'action',title:'操作',width:200,
						formatter: function(value,row,index) {
							var d = '<a href="javascript:void(0);" onclick="deleteA('+row.deploymentId+')">删除<a/>';
							var c = '<a href="javascript:void(0);" onclick="convertM(\''+row.id+'\')">转换为Model<a/>';
							return d+" " +c;
						}
					}
				]],
		pagination:true,
		pageSize:10,
		pageList:[10,20,30,40],
		pageNumber:1,
		onLoadSuccess:function(){
            $('#activitiTab').datagrid('clearSelections');// 一定要加上这一句，不然datagrid会记住之前的选择状态，删除时会出现问题
        },
        onBeforeLoad:function(){
            // 加载数据
            ActivitiLoadData($('#activitiTab').datagrid('options').pageNumber,$('#activitiTab').datagrid('options').pageSize);
        }
	});
	
	 var mypage = $('#activitiTab').datagrid('getPager');
	 	$(mypage).pagination({
	 		beforePageText:'第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
	        onSelectPage : function(pPageIndex, pPageSize) {   
	            var gridOpts = $('#activitiTab').datagrid('options');   
	            gridOpts.pageNumber = pPageIndex;   
	            gridOpts.pageSize = pPageSize;       
	            ActivitiLoadData(pPageIndex,pPageSize);
	        },
	 	});
});

function ActivitiLoadData(pPageIndex,pPageSize) {
    $.ajax({
        type:'get',
        url : Config.activitilist,
        data:{"page":pPageIndex,"rows":pPageSize},
        success:function(data) {
        	if(data.code == 1) {
        		$('#activitiTab').datagrid("loadData",data.data);
        	} else {
        		alert(data.message);
        	}
        }
    });
}

function deleteA(deployId) {
	$.ajax({
		type:'get',
		url: Config.delDeploy+'?deploymentId='+deployId,
		success:function(data) {
			if(data.code == 1) {
				ActivitiLoadData($('#activitiTab').datagrid('options').pageNumber,$('#activitiTab').datagrid('options').pageSize);
				alert(data.message);
			} else {
				alert(data.message);
			}
		}
	});
}

function convertM(processDefId) {
	$.ajax({
		type:'get',
		url:Config.covertToModel+processDefId,
		success:function(data) {
			if(data.code == 1) {
				//alert(data.message);
				window.location.href=Config.modelview;
				//upUi();
			} else {
				alert(data.message);
			}
		}
	});
}

/*function upUi(){ 
	console.log($(".tabs-panels iframe"))
	console.log(document.getElementsByTagName("iframe"))
	$(".tabs-panels iframe").eq($(".tabs li").index($(".tabs-selected"))-1)[0].src=Config.modellist;
}*/

function activeA(stats,processDefId) {
	$.ajax({
		type:'get',
		url:Config.suspended+stats+"/"+processDefId,
		success:function(data) {
			if(data.code == 1) {
				alert(data.message);
				ActivitiLoadData($('#activitiTab').datagrid('options').pageNumber,$('#activitiTab').datagrid('options').pageSize);
			} else {
				alert(data.message);
			}
		}
	});
}