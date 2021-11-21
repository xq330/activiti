package com.ithema.test;

import com.itheima.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试流程变量
 */
public class TestVariables {
    /**
     * 部署流程
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables")
                .addClasspathResource("bpmn/evection-global.bpmn20.xml")
                .deploy();
        System.out.println("流程部署ID="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }
    /**
     * 启动流程时候设置流程变量
     * 设置流程变量num
     * 设置任务负责人
     */
    @Test
    public void testStartProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Evection evection = new Evection();
        evection.setNum(6d);
        Map<String, Object> map = new HashMap<>();
        String key = "evection-global";
        map.put("evection",evection);
        map.put("assignee0","李四");
        map.put("assignee1","王经理");
        map.put("assignee2","杨总经理");
        map.put("assignee3","张财务");
        runtimeService.startProcessInstanceByKey(key,map);
    }
    @Test
    public void completTask(){
        String key = "evection-global";
        String taskAssingee = "张财务";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(taskAssingee)
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println(key+":"+taskAssingee+"--完成");
        }
    }
}