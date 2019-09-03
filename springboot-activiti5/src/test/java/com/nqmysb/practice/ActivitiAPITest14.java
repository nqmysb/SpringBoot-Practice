package com.nqmysb.practice;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-顺序流和网关
 * 
 * 
 * 顺序流：条件顺序流，默认顺序流
 * 1.可以为顺序流定义一个条件。离开一个BPMN 2.0节点时， 默认会计算外出顺序流的条件。 如果条件结果为true, 就会选择外出顺序流继续执行。当多条顺序流被选中时， 就会创建多条分支， 流程会继续以并行方式继续执行。
 * 2.默认顺序流通过对应节点的default属性定义。 下面的XML代码演示了排他网关设置了默认顺序流flow 2。 只有当conditionA和conditionB都返回false时， 才会选择它作为外出连线继续执行。


 * 
 * 二、网关

1、排它网关：单向网关， 内部是一个“X”图标，用来在流程中实现决策。 当流程执行到这个网关，所有外出顺序流都会被处理一遍。 其中条件解析为true的顺序流会被选中，让流程继续运行。 
如果没有选中任何顺序流，会抛出一个异常。 和条件顺序流不同的是 只能选择一条执行
<exclusiveGateway id="exclusiveGw" name="Exclusive Gateway" />
2、并行网关： 内部是一个“加号”图标。它允许将流程 分成多条分支，也可以把多条分支 汇聚到一起
<parallelGateway id="myParallelGateway" />
3、包含网关： 内部包含一个圆圈图标，可以看做是排他网关和并行网关的结合体。 和排他网关一样，你可以在外出顺序流上定义条件，包含网关会解析它们。 但是主要的区别是包含网关可以选择多于一条顺序流，这和并行网关一样。
<inclusiveGateway id="myInclusiveGateway" />  根据条件去选择  被选择就并行执行
4、基于事件网关： 网关的每个外出顺序流都要连接到一个中间捕获事件。 当流程到达一个基于事件网关，网关会进入等待状态：会暂停执行。 与此同时，会为每个外出顺序流创建相对的事件订阅。
 *  <eventBasedGateway id="gw1" /> 
 *  事件触发执行流程
 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest14 {
	
	
    /**
     * 
     * 
     * 条件顺序流：一个条件对应的顺序流
     * 
     *     <exclusiveGateway id="exclusivegateway1" name="Exclusive Gateway"></exclusiveGateway>
    <sequenceFlow id="flow2" sourceRef="usertask1" targetRef="exclusivegateway1"></sequenceFlow>
    <sequenceFlow id="flow3" sourceRef="exclusivegateway1" targetRef="usertask2">
            <conditionExpression xsi:type="tFormalExpression"><![CDATA[
            ${days <= 3}
            ]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow4" sourceRef="exclusivegateway1" targetRef="usertask3">
            <conditionExpression xsi:type="tFormalExpression"><![CDATA[
            ${days > 3}
            ]]></conditionExpression>
    </sequenceFlow>
    
    如果days 的参数两个都不符合就直接结束
    留一个默认顺序流
    
    <exclusiveGateway id="exclusiveGw" name="Exclusive Gateway" default="flow2" />
<sequenceFlow id="flow1" sourceRef="exclusiveGw" targetRef="task1">
  <conditionExpression xsi:type="tFormalExpression">${conditionA}</conditionExpression>
</sequenceFlow>
<sequenceFlow id="flow2" sourceRef="exclusiveGw" targetRef="task2"/>
<sequenceFlow id="flow3" sourceRef="exclusiveGw" targetRef="task3">
  <conditionExpression xsi:type="tFormalExpression">${conditionB}</conditionExpression>
</sequenceFlow>
     */
	@Test
	public  void processTest1() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
        // 运行时服务
        RuntimeService runService = engine.getRuntimeService();
        // 任务服务
        TaskService taskService = engine.getTaskService();
        Deployment dep = rs.createDeployment().addClasspathResource("test/sequence.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
       
        
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("days", 6);
//        vars.put("days", 2);

        ProcessInstance pi = runService.startProcessInstanceById(pd.getId(), vars);
        
        
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
        // 完成第一个任务
        taskService.complete(task.getId());
        
        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
		
	}
	
	

	

		


}
