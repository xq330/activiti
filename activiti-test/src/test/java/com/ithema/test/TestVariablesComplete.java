package com.ithema.test;

import com.itheima.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试流程变量 任务完成时添加流程变量
 */
public class TestVariablesComplete {
    //获取变量
    private Evection getEvection(double num){
        Evection evection = new Evection();
        evection.setNum(num);
        return evection;
    }
    /**
     * 部署流程
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables-complete")
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
        Map<String, Object> map = new HashMap<>();
        String key = "evection-global";
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
            Map<String, Object> map = new HashMap<>();
            map.put("evection",getEvection(2d));
            taskService.complete(task.getId(),map);
            System.out.println(key+":"+taskAssingee+"--完成");
        }
    }



    /**
     * 通过流程实例ID设置全局变量，该实例必须未执行完成。
     */
    @Test
    public void setGlobalVarialbleExecutionId(){
        String exectionId="2422";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Evection evection = getEvection(2d);
        runtimeService.setVariable(exectionId,"evection",evection);
        //runtimeService.setVariableLocal(exectionId,"evection",evection);
    }
    /**
     * 通过任务ID设置全局变量,任务id必须存在
     */
    @Test
    public void setGlobalVarialbleByTaskId(){
        String taskId="2422";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Evection evection = getEvection(2d);
        taskService.setVariable(taskId,"evection",evection);
    }
}