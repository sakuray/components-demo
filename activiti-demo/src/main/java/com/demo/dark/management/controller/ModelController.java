package com.demo.dark.management.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.dark.common.dto.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping(value = "manage/model")
public class ModelController {
	
	protected Logger logger = LoggerFactory.getLogger(ModelController.class);
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value = "view")
	public String view() {
	    return "repository/modellist";
	}
	
	@RequestMapping(value = "list")
	@ResponseBody
	public Result list() {
	    List<Model> list = repositoryService.createModelQuery().list();
	    return new Result(1, "查询model数据成功", list);
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Result create(@RequestParam(value = "name")String name, @RequestParam("key")String key,
			@RequestParam("description")String description,HttpServletRequest request,
			HttpServletResponse response) {
		try {
			 ObjectMapper objectMapper = new ObjectMapper();
	            ObjectNode editorNode = objectMapper.createObjectNode();
	            editorNode.put("id", "canvas");
	            editorNode.put("resourceId", "canvas");
	            ObjectNode stencilSetNode = objectMapper.createObjectNode();
	            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
	            editorNode.put("stencilset", stencilSetNode);
	            Model modelData = repositoryService.newModel();

	            ObjectNode modelObjectNode = objectMapper.createObjectNode();
	            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
	            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
	            description = StringUtils.defaultString(description);
	            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
	            modelData.setMetaInfo(modelObjectNode.toString());
	            modelData.setName(name);
	            modelData.setKey(StringUtils.defaultString(key));

	            repositoryService.saveModel(modelData);
	            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

//	            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
	            return new Result(1, "创建model成功", modelData.getId());
	        } catch (Exception e) {
	            logger.error("创建模型失败：", e);
	            return new Result(0,"发生异常","");
	        }
	}
	
	/**
     * 根据Model部署流程
     */
    @RequestMapping(value = "deploy/{modelId}")
    @ResponseBody
    public Result deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;
            
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            return new Result(1,"部署成功,部署id为："+deployment.getId(),"");
//            redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={}", modelId, e);
            return new Result(-1,"发生异常，部署失败。请检查设计的流程图","");
        }
    }
    
    /**
     * 删除Model
     */
    @RequestMapping(value = "delete/{modelId}")
    @ResponseBody
    public Result delete(@PathVariable("modelId") String modelId) {
        try {
            repositoryService.deleteModel(modelId);
            return new Result(1, "删除model:"+modelId+"成功", "");
        } catch(Exception ex) {
            ex.printStackTrace();
            return new Result(0, "删除Model:"+modelId+"失败", "");
        }
    }
    
    /**
     * 导出Model
     */
    @RequestMapping(value = "export/{modelId}/{type}")
    public void export(@PathVariable("modelId") String modelId,
                       @PathVariable("type") String type,
                       HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

            JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 处理异常
            if (bpmnModel.getMainProcess() == null) {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.getOutputStream().println("no main process, can't export for type: " + type);
                response.flushBuffer();
                return;
            }

            String filename = "";
            byte[] exportBytes = null;

            String mainProcessId = bpmnModel.getMainProcess().getId();

            if (type.equals("bpmn")) {

                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);

                filename = mainProcessId + ".bpmn20.xml";
            } else if (type.equals("json")) {

                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";

            }

            ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
            IOUtils.copy(in, response.getOutputStream());

            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
        }
    }
}