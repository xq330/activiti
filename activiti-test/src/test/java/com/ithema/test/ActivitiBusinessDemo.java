package com.ithema.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ActivitiBusinessDemo {
    /**
     * 添加业务key到activiti的表
     */
    @Test
    public void addBusinessKey(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //第一个参数流程定义key，第二个参数businesskey
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        System.out.println("getBusinessKey:"+instance.getBusinessKey());
    }

    /**
     *  全部流程实例的挂起 激活
     */
    @Test
    public void suspendAllProcessInstance(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        String id = processDefinition.getId();
        boolean suspended = processDefinition.isSuspended();
        // 修改act_re_procdef表中的suspension_state字段
        if(suspended){
            //如果是挂起，激活流程
            repositoryService.activateProcessDefinitionById(id);
            System.out.println("流程定义ID:"+id+",已激活");
        }else {
            //如果是激活，则挂起
            repositoryService.suspendProcessDefinitionById(id);
            System.out.println("流程定义ID:"+id+",已挂起");
        }
    }
    /**
     *  单个流程实例的挂起 激活
     */
    @Test
    public void suspendSingleProcessInstance(){
        String instanceId = "35001";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        boolean suspended = instance.isSuspended();
        // 修改act_ru_execution表中的suspension_state字段
        if(suspended){
            //如果是挂起，激活流程
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例ID:"+instanceId+",已激活");
        }else {
            //如果是激活，则挂起
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例ID:"+instanceId+",已挂起");
        }
    }
    @Test
    public void completTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId("35001")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println("流程实例ID=="+task.getProcessDefinitionId());
        System.out.println("流程任务ID=="+task.getId());
        System.out.println("负责人=="+task.getAssignee());
        System.out.println("任务名称=="+task.getName());
        taskService.complete(task.getId());
    }
}
