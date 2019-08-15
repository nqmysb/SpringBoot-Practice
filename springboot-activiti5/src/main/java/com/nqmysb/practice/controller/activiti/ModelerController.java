package com.nqmysb.practice.controller.activiti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nqmysb.practice.service.activiti.ModelerService;
import com.nqmysb.practice.utils.JsonResult;
import com.nqmysb.practice.utils.ResultCode;

import io.swagger.annotations.ApiOperation;

/**
 * @author: he.feng
 * @date: 20:20 2017/11/30
 * @desc:
 **/
@RestController
@RequestMapping("/modeler")
public class ModelerController {

    @Autowired
    private ProcessEngine processEngine;

    private static final Logger logger = LoggerFactory.getLogger(ModelerController.class);

    @Resource
    private ModelerService modelerService;

    /**
     * 创建流程模型
     *
     * @param name
     * @param key
     * @param description
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "创建流程模型(空)")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public JsonResult<?> createModel(@RequestParam("name") String name, @RequestParam("key") String key,
                                  @RequestParam("description") String description,
                                  HttpServletRequest request, HttpServletResponse response) {
        logger.info("创建空modeler：name:{},key:{},description:{}", name, key, description);
        try {
            //创建空模型
            String modelId = modelerService.crateModel(name, key, description);
            if (StringUtils.isBlank(modelId)) {
                throw new RuntimeException("创建modeler失败modelId:" + modelId);
            }

            //获取当前地址
            String strBackUrl = "http://" + request.getServerName() + ":"
                    + request.getServerPort()+
                    "/modeler.html?modelId=" + modelId;

//            return "redirect:../modeler.html?modelId=" + modelId;
            return JsonResult.buildSuccessResult(strBackUrl);
        } catch (Exception e) {
            logger.error("创建模型失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 模型列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "模型列表")
    @RequestMapping("/model/list")
    public JsonResult<?> modelList() {
        List<Model> list = modelerService.queryModelList();
        return JsonResult.buildSuccessResult(list);
    }

    /**
     * 编辑模型
     *
     * @param modelId
     * @return
     */
    @ApiOperation(value = "编辑模型")
    @RequestMapping("/model/edit/{modelId}")
    public JsonResult<?> editModel(@PathVariable String modelId,HttpServletRequest request, HttpServletResponse response) {
        //获取当前地址
        String editModelUrl = "/modeler.html?modelId=" + modelId;
        return JsonResult.buildSuccessResult(editModelUrl);
    }



    /**
     * 删除模型
     * @param id
     * @return
     */
    @ApiOperation(value = "删除模型")
    @GetMapping("/model/delete/{id}")
    public JsonResult<?> deleteModel(@PathVariable("id")String id){
            modelerService.removeModel(id);
//            RepositoryService repositoryService = processEngine.getRepositoryService();
//            repositoryService.deleteModel(id);
            return JsonResult.buildSuccessResult("删除成功");

    }



    /**
     * 发布模型为流程定义
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发布模型为流程定义")
    @PostMapping("/deployment/{id}")
    public JsonResult<?> deploy(@PathVariable("id") String id) throws Exception {
        try {
            //获取模型
            RepositoryService repositoryService = processEngine.getRepositoryService();
            Model modelData = repositoryService.getModel(id);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

            if (bytes == null) {
                return JsonResult.buildFailuredResult(ResultCode.EXCEPTION,"模型数据为空，请先设计流程并成功保存，再进行发布。");
            }

            JsonNode modelNode = new ObjectMapper().readTree(bytes);

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (model.getProcesses().size() == 0) {
                return JsonResult.buildFailuredResult(ResultCode.EXCEPTION,"数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            //发布流程
            String processName = modelData.getName() + ".bpmn20.xml";
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"));

            Deployment deployment = deploymentBuilder.deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);

            return JsonResult.buildSuccessResult("流程发布成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.buildFailuredResult(ResultCode.EXCEPTION,"发布流程失败！");
        }

    }

    private Map<String, Object> failed(String reason) {
        Map<String, Object> map = new HashMap();
        map.put("status", false);
        map.put("reason", "操作失败：" + reason);
        return map;
    }

    private Map<String, Object> success() {
        Map<String, Object> map = new HashMap();
        map.put("status", true);
        map.put("reason", "操作成功");
        return map;
    }


    // 启动流程实例
    @Test
    public void engines() throws Exception {
// 按照流程定义的id启动：    myProcess:2:604
// runtimeService.startProcessInstanceById(processDefinitionId)
// 通过流程定义的key启动流程，会启动版本最高的流程
        // 按照流程定义的id启动：    myProcess:2:604
// runtimeService.startProcessInstanceById(processDefinitionId)

        List<String> assigneeList = Arrays.asList("张三", "李四", "王五");
        List<Integer> moneyList = Arrays.asList(250);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("list", assigneeList);
        variables.put("moneyList",moneyList);

// 通过流程定义的key启动流程，会启动版本最高的流程
        ProcessInstance pi = ProcessEngines.getDefaultProcessEngine()//
                .getRuntimeService()//获取正在执行的Service
                .startProcessInstanceByKey("multiInstance",variables);//按照流程定义的key启动流程实例，默认按照最新版本启动
        System.out.println("pid:" + pi.getId() + ",activitiId:" + ",pdId:" + pi.getProcessDefinitionId());

    }



    ProcessEngine processEnginexml = ProcessEngines.getDefaultProcessEngine();


    //删除流程实例
    @Test
    public void deleteProcessInstance(){
        RuntimeService runtimeService = processEnginexml.getRuntimeService();
        runtimeService.deleteProcessInstance("80001","删除原因");//删除流程
    }
    //删除流程模型
    @Test
    public void deleteModel(){
        RepositoryService runtimeService = processEnginexml.getRepositoryService();
        runtimeService.deleteModel("27502");//删除流程
    }

    //根据流程执行人查询个人任务
    @Test
    public void queryPersionalTask() {
        List<Task> yong = processEnginexml.getTaskService().createTaskQuery().taskAssignee("yong").list();
        for(Task y : yong){
            System.out.println("getTaskDefinitionKey:"+y.getTaskDefinitionKey()+"getId："+y.getId());
        }
    }


    //分配任务
    @Test
    public void setAssigneeTask(){
        processEnginexml.getTaskService().setAssignee("40016","张三");
        processEnginexml.getTaskService().setAssignee("40022","李四");
        processEnginexml.getTaskService().setAssignee("40028","王五");

    }

    private void taskListener(){
        System.out.println("创建任务！");
    }





    /**查看流程图
     * @throws IOException */
    @Test
    public void viewPic() throws IOException{
        /**将生成图片放到文件夹下*/
        String deploymentId = "10054";
        //获取图片资源名称
        List<String> list = processEnginexml.getRepositoryService()//
                .getDeploymentResourceNames(deploymentId);
        //定义图片资源的名称
        String resourceName = "";
        if(list!=null && list.size()>0){
            for(String name:list){
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }


        //获取图片的输入流
        InputStream in = processEnginexml.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);

        //将图片生成到D盘的目录下
        File file = new File("G:\\" + resourceName);
        //将输入流的图片写到D盘下
        FileUtils.copyInputStreamToFile(in, file);
    }








}
