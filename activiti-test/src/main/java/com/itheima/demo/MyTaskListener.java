package com.itheima.demo;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;


public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        //判断当前任务是创建申请 并且 是 create事件
        if("创建申请".equals(delegateTask.getName())) {
            if("create".equals(delegateTask.getEventName())){
                delegateTask.setAssignee("张三");
            }
        }
    }
}
