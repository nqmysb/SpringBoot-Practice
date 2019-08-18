package com.nqmysb.practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试activitiAPI使用-工作的产生与管理
 * 
 * 6版本中job表分了几张表
 * 
 * 工作的产生
 * 1.异步任务
 * 服务任务
 * serviceTask
 *      <serviceTask id="servicetask1" name="Service Task" activiti:async="true" 
         activiti:class="org.crazyit.act.c10.MyJavaDelegate"></serviceTask>
 * 2.定时事件产生工作  淘宝48小时订单取消
 *     <intermediateCatchEvent id="timerintermediatecatchevent1"
            name="TimerCatchEvent">
            <timerEventDefinition>
                <timeDuration>PT1M</timeDuration>
            </timerEventDefinition>
        </intermediateCatchEvent>
 * 3.暂停的工作 
 * 暂停流程 时的工作
 *   
        runService.suspendProcessInstanceById(pi.getId());
        
        
        Thread.sleep(10000);
        
        runService.activateProcessInstanceById(pi.getId());
 * 
 * 
 * 4.无法执行的工作
 * 
 * 

 
 * 
 * @author liaocan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiAPITest7 {
	
	
	/**
	 * 6版本 
	 */
	@Test
	public  void processTest1() {
		
	}
	
	
	

	
	
	
	

	

		


}
