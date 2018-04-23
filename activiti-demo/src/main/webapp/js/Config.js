var host = 'http://127.0.0.1:8080';
var project = '/activiti-spring';
var base = host + project;
var context = base + '/activiti';
var Config = {
		'project' : project,

		'login' : context + "/login/in",
		'skip' : context + "/login/skip",
		'out' : context + "/login/out",
		
		'modelview' : context + "/manage/model/view",
		'modellist' : context + "/manage/model/list",
		'addmodel' : context + "/manage/model/create",
		'editmodel' : base + "/modeler.html?modelId=",
		'deploymodel' : context + "/manage/model/deploy/",
		'deletemodel' : context + "/manage/model/delete/",
		'downloadmodel' : context + "/manage/model/export/",
		
		'activitiview' : context + "/activiti/view",
		'activitilist' : context + "/activiti/list",
		'suspended' : context + "/activiti/update/",
		'delDeploy' : context + "/activiti/delete",
		'covertToModel': context + "/activiti/convert-to-model/",
		
		'resource' : context + "/activiti/resource/read",
		
		'processview' : context + "/process/view",
		'endview' : context + "/process/endview"
};