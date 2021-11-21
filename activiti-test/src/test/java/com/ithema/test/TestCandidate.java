package com.ithema.test;

import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class TestCandidate {
    /**
     *  流程部署
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("出差申请流程-Candidate")
                .addClasspathResource("bpmn/evection-candidate.bpmn20.xml")
                .deploy();
    }

    /**
     *  启动流程
     */
    @Test
    public void startCandidate(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("evection-candidate");
    }
    /**
     *  完成个人任务
     */
    @Test
    public void completeTask(){
        String assignee = "zhangsan";
        String definitionKey = "evection-candidate";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processDefinitionKey(definitionKey)
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("任务完成");
        }
    }
    /**
     *  查询组任务
     */
    @Test
    public void findGroupTask(){
        String assignee = "wangwu";
        String definitionKey = "evection-candidate";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser(assignee)
                .processDefinitionKey(definitionKey)
                .list();
        for (Task task : list) {
            System.out.println("===========");
            System.out.println("taskId:"+task.getId());
        }
    }
    /**
     *  候选人拾取任务
     */
    @Test
    public void claimTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String taskId = "42502";
        String candidateUser = "wangwu";
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if(task != null){
            taskService.claim(taskId,candidateUser);
            System.out.println("用户拾取完成");
        }
    }
    /**
     *  查询个人任务
     */
    @Test
    public void findPersonalTaskList(){
        String processDefinitionKey = "evection-candidate";
        String assignee = "wangwu";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(assignee)
                .list();
        for (Task task : list) {
            System.out.println(task.getId());
        }
    }
    /**
     *  归还任务
     */
    @Test
    public void testAssigneeToGroupTask(){
        String taskId = "";
        String assignee = "wangwu";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(task != null){
            //归还任务就是设置负责人为空
            taskService.setAssignee(taskId,null);
        }
    }
    /**
     *  任务交接
     */
    @Test
    public void SetAssigneeToCandidate(){
        String taskId = "";
        String assignee = "wangwu";
        String candidate = "zhaoliu";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(task != null){
            //归还任务就是设置负责人为空
            taskService.setAssignee(taskId,candidate);
        }
    }
}
