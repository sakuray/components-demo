package com.demo.dark.management.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.dark.common.dto.ProcessDef;
import com.demo.dark.common.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("activiti")
public class ActivitiController {
    
    @Autowired
    private RepositoryService repositoryService;
    
    @RequestMapping(value = "view")
    public String view() {
        return "repository/activitilist";
    }
    
    @RequestMapping(value = "list")
    @ResponseBody
    public Result list(@RequestParam(required=true)String page,@RequestParam(required=true)String rows) {
        int cpage = Integer.parseInt((page == null || page == "0") ? "1": page);
        int crow =  Integer.parseInt((rows == null || rows == "0") ? "10": rows);
        int start = (cpage - 1)*crow;
//        List<Object[]> objects = new ArrayList<Object[]>();
        List<ProcessDef> list = new ArrayList<ProcessDef>();
        try {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
        /*    NativeProcessDefinitionQuery definitionQuery =  repositoryService.createNativeProcessDefinitionQuery().sql(
                    "select ID_ id, NAME_ name,KEY_ 'key', VERSION_ version,DEPLOYMENT_ID_ deploymentId,SUSPENSION_STATE_ suspended from ACT_RE_PROCDEF order by DEPLOYMENT_ID_ desc");*/
            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(start, crow);
//            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(start,start+crow-1);
           for (ProcessDefinition processDefinition : processDefinitionList) {
                String deploymentId = processDefinition.getDeploymentId();
                Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
                
                ProcessDef def = new ProcessDef();
                def.setId(processDefinition.getId());
                def.setDeploymentId(processDefinition.getDeploymentId());
                def.setName(processDefinition.getName());
                def.setKey(processDefinition.getKey());
                def.setVersion(processDefinition.getVersion());
                def.setDeploymentTime(deployment.getDeploymentTime());
                def.setSuspended(processDefinition.isSuspended());
                def.setResourceName(processDefinition.getResourceName());
                def.setDiagramResourceName(processDefinition.getDiagramResourceName());
                
                list.add(def);
//                objects.add(new Object[]{processDefinition, deployment});
            }
            return new Result(1, "查询定义的流程成功", list);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new Result(0,"发生异常情况，查询失败","");
        }
    }
    
    /**
     * 读取资源，通过部署ID
     * @param processDefinitionId 流程定义
     * @param resourceType        资源类型(xml|image)
     * @throws Exception
     */
    @RequestMapping(value = "/resource/read")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    /**
     * 删除部署的流程，级联删除流程实例
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Result delete(@RequestParam("deploymentId") String deploymentId) {
        try {
            repositoryService.deleteDeployment(deploymentId, true);
            return new Result(1,"删除部署流程:"+deploymentId+"成功","");
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(-1,"发生异常情况,部署的流程" + deploymentId+"未删除成功","");
        }
    }
    
    /**
     * 将定义的流程转变成model
     */
    @RequestMapping(value = "/convert-to-model/{processDefinitionId}")
    @ResponseBody
    public Result convertToModel(@PathVariable("processDefinitionId") String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId).singleResult();
            InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                    processDefinition.getResourceName());
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
    
            BpmnJsonConverter converter = new BpmnJsonConverter();
            com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
            Model modelData = repositoryService.newModel();
            modelData.setKey(processDefinition.getKey());
            modelData.setName(processDefinition.getResourceName());
            modelData.setCategory(processDefinition.getDeploymentId());
    
            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());
    
            repositoryService.saveModel(modelData);
    
            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
            return new Result(1,"已定义的流程"+processDefinitionId+"转换为model成功","");
        } catch(Exception ex) {
            return new Result(-1, "发生异常情况，定义的流程："+processDefinitionId+"转化model失败", "");
        }
    }
    
    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "update/{state}/{processDefinitionId}")
    @ResponseBody
    public Result updateState(@PathVariable("state") String state, @PathVariable("processDefinitionId") String processDefinitionId) {
        try {
            if (state.equals("active")) {
                repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
                return new Result(1,"已激活ID为[" + processDefinitionId + "]的流程定义。","");
            } else if (state.equals("suspend")) {
                repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
                return new Result(1,"已挂起ID为[" + processDefinitionId + "]的流程定义。","");
            }
            return new Result(-1,"操作["+state+"]"+"非合法操作","");
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(-1,"发生异常情况，激活或挂起["+processDefinitionId+"]操作失败","");
        }
    }
}
