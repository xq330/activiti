package com.ithema.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.HashMap;

public class TestAssigneeUel {

    /**
     *  流程部署
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-uel")
                .addClasspathResource("bpmn/MyEvection1.bpmn20.xml")
                .deploy();
        System.out.println("流程部署ID="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }
    /**
     *  开始流程
     */
    @Test
    public void startAssigneeUel(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0","张三");
        assigneeMap.put("assignee1","李经理");
        assigneeMap.put("assignee2","王总经理");
        assigneeMap.put("assignee3","赵财务");
        runtimeService.startProcessInstanceByKey("myEvection1", assigneeMap);
    }
}
