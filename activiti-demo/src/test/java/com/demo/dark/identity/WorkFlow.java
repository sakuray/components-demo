package com.demo.dark.identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.demo.dark.base.BaseJunit;

public class WorkFlow extends BaseJunit{
    
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;
    
    // nothing userTask js
    private String processKey = "js";
    
    private String userId = "admin";
    
    private String taskId = "task1";
    
    @Test
    @Rollback(value=false)
    public void start() {
        Map<String,Object> variables = new HashMap<String,Object>();
//        variables.put("x", 2);
        runtimeService.startProcessInstanceByKey(processKey, "4",variables);
        logger.info("创建实例Key:"+processKey+"成功");
    }
    
    @Test
    public void history() {
//        List<HistoricActivityInstance> activityinstance = historyService.createHistoricActivityInstanceQuery().list();
        List<HistoricProcessInstance> processinstance = historyService.createHistoricProcessInstanceQuery().finished().list();
        for(HistoricProcessInstance instance: processinstance) {
            logger.info("完成的实例有：Key="+instance.getProcessDefinitionKey() + ",BussinessKey=" + instance.getBusinessKey());
        }
        logger.info("查询历史记录结束");
    }
    
    @Test
    public void runProcessInstance() {
        List<ProcessInstance> result =  runtimeService.createProcessInstanceQuery().list();
        for(ProcessInstance s : result) {
            logger.info("运行中的实例有：Key=" + s.getProcessDefinitionKey() + ",BussinessKey=" + s.getBusinessKey() +",当前活动的ACT_Id:" +s.getActivityId());
        }
        logger.info("查询当前活动记录结束");
    }
    
    @Test   // 查询所有活动中未被受理的任务
    public void taskUnassigned() {
        List<Task> tasks =  taskService.createTaskQuery().active().taskUnassigned().list();
        for(Task task : tasks) {
            logger.info("运行中未受理的任务,流程Id为："+task.getProcessInstanceId()+",任务节点Key:"+task.getTaskDefinitionKey());
        }
        logger.info("查询当前活动未受理记录结束");
    }
    
    @Test   // 查询所有已受理但未完成的任务(有问题)
    public void taskAssign() {
        List<Task> tasks = taskService.createNativeTaskQuery().sql("select * from " + managementService.getTableName(Task.class)+ " where ASSIGNEE_ != null").list();
        for(Task task : tasks) {
            logger.info("已受理但未完成的任务,流程Id为："+task.getProcessInstanceId()+",任务节点Key:"+task.getTaskDefinitionKey());
        }
        logger.info("查询已受理但未完成的任务结束");
    }
    
    @Test // 派遣任务
    @Rollback(value=false)
    public void taskClaim() {
        List<Task> tasks =  taskService.createTaskQuery().active().taskUnassigned().orderByTaskCreateTime().desc().list();
        Task aim = tasks.get(0);
        String taskId = aim.getId();
        logger.info("要完成的任务节点是："+aim.getTaskDefinitionKey()+",Id是："+taskId);
        taskService.claim(taskId, userId);
        logger.info("任务已经派给："+userId);
    }
    
    @Test
    public void taskOwner() {
        List<Task> tasks =  taskService.createTaskQuery().active().orderByTaskCreateTime().desc().list();
        Task aim = tasks.get(0);
        logger.info("任务节点："+ aim.getTaskDefinitionKey()+",派给了：" + aim.getAssignee()+",其属于：" + aim.getOwner());
    }
    
    @Test
    public void taskComplete() {
        List<Task> tasks =  taskService.createTaskQuery().active().orderByTaskCreateTime().desc().list();
        Task aim = tasks.get(0);
        taskService.complete(aim.getId());
        logger.info("任务节点："+ aim.getTaskDefinitionKey()+",任务Id:"+aim.getId()+"完成");
    }
}
