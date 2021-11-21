package com.ithema.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {
    //部署流程
    @Test
    public void testDeployment(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/MyEvection.bpmn20.xml")
                .deploy();
        System.out.println("流程部署Id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }
    //启动流程
    @Test
    public void testStartProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        System.out.println("流程定义ID:"+instance.getProcessDefinitionId());
        System.out.println("流程实例ID:"+instance.getId());
        System.out.println("当前活动ID:"+instance.getActivityId());
    }
    //获取个人任务
    @Test
    public void testFIndPersionalTaskList(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("zhangsan")
                .list();
        for (Task task : list) {
            System.out.println("流程实例ID="+task.getProcessDefinitionId());
            System.out.println("任务ID="+task.getId());
            System.out.println("任务负责人="+task.getAssignee());
            System.out.println("任务名称="+task.getName());
            System.out.println("*******************************************");
        }
    }
    //完成任务
    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("30002");
    }
    //使用zip进行部署
    @Test
    public void deployProcessByZip(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/myEvection.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }
    //查询流程定义
    @Test
    public void queryProcessDefinition(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //输出信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义ID:"+processDefinition.getId());
            System.out.println("流程定义名称:"+processDefinition.getName());
            System.out.println("流程定义Key:"+processDefinition.getKey());
            System.out.println("流程定义版本:"+processDefinition.getVersion());
            System.out.println("---------------------------");
        }
    }
    //删除流程定义
    @Test
    public void deleteDeployment(){
        String deploymentId = "5001";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //普通删除
        //repositoryService.deleteDeployment(deploymentId);
        //强制删除 级联删除
        repositoryService.deleteDeployment(deploymentId,true);
        System.out.println("删除流程deploymentId:"+deploymentId+"成功");
    }
    //下载部署文件
    @Test
    public void getDeployment() throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        String diagramResourceName = processDefinition.getResourceName();
        //获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
        //构造OutputStream
        File file = new File("d:/evectionflow01.bpmn");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //输入输出流转换
        IOUtils.copy(inputStream,fileOutputStream);
        //关闭流
        fileOutputStream.close();
        inputStream.close();
    }
    /**
     *  查看历史信息
     */
    @Test
    public void findHistoryInfo(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = defaultProcessEngine.getHistoryService();
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        List<HistoricActivityInstance> instanceList = historicActivityInstanceQuery.processInstanceId("25001")
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance hi : instanceList) {
            System.out.println("getActivityId:" + hi.getActivityId());
            System.out.println("getActivityName:" + hi.getActivityName());
            System.out.println("getProcessDefinitionId:" + hi.getProcessDefinitionId());
            System.out.println("getProcessInstanceId:" + hi.getProcessInstanceId());
            System.out.println("***********************************");
        }
    }
}