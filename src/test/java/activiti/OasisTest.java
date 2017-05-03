package activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml", "classpath:spring-activiti.xml" })
public class OasisTest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    @Test
    public void deploy() {
        final DeploymentBuilder builder = this.repositoryService
                .createDeployment();
        // 设置发布信息
        builder.name("oasis测试流程")// 添加部署规则的显示别名
            .addClasspathResource("bpmn/oasis2.bpmn")// 添加规则文件
            .addClasspathResource("bpmn/oasis2.png");// 添加规则图片,不添加会自动产生一个图片不推荐
        // 完成发布
        builder.deploy();
    }

    @Test
    public void deployAntiFraud() {
        final DeploymentBuilder builder = this.repositoryService
                .createDeployment();
        // 设置发布信息
        builder.name("反欺诈流程")// 添加部署规则的显示别名
            .addClasspathResource("bpmn/antiFraud.bpmn")// 添加规则文件
            .addClasspathResource("bpmn/antiFraud.png");// 添加规则图片,不添加会自动产生一个图片不推荐
        // 完成发布
        builder.deploy();
    }

    @Test
    public void delDeployment() {
        final String deploymentId = "301201";
        final String[] array = deploymentId.split(",");
        if (ArrayUtils.isNotEmpty(array)) {
            for (final String id : array) {
                this.repositoryService.deleteDeployment(id, true);
            }
        }
    }

    @Test
    public void startProcess() {
        final Map<String, Object> vars = new HashMap<>();
        final Map<String,Boolean> map = new HashMap<>();
        vars.put("returnTasks", map);
        final ProcessInstance pi = this.runtimeService
                .startProcessInstanceByKey("oasis", vars);
        System.out.println("pid:" + pi.getId() + ",activitiId:"
                + pi.getActivityId());
    }

    /**
     * 通过
     */
    @Test
    public void complete1() {
        final String taskId = "1155008";
//        String processInstanceId = this.taskService.createTaskQuery().taskId(taskId).active().singleResult().getProcessInstanceId();
//        final Map<String, Object> vars = this.runtimeService.getVariables(processInstanceId);
//        vars.put("code", "004");
//        this.runtimeService.setVariables(processInstanceId,
//            vars);
        final Task task = this.taskService.createTaskQuery().taskId(taskId).active().singleResult();
        this.runtimeService.setVariableLocal(task.getExecutionId(),"code", "004");
        this.taskService.complete(taskId);

        final String tId = "1160004";
        final Task t = this.taskService.createTaskQuery().taskId(tId).active().singleResult();
        this.runtimeService.setVariableLocal(t.getExecutionId(),"code", "004");

        this.taskService.complete(tId);
    }

    /**
     * 欺诈提交
     */
    @Test
    public void antiFraudSubmit() {
        final String taskId = "1155006";
        final Task task = this.taskService.createTaskQuery().taskId(taskId).active().singleResult();
        this.runtimeService.setVariableLocal(task.getExecutionId(),"mainProcessInstanceId", task.getProcessInstanceId());
        this.runtimeService.setVariableLocal(task.getExecutionId(),"code", "007");
        this.taskService.complete(taskId);

        final String taId = "1137513";
        final Task t = this.taskService.createTaskQuery().taskId(taId).active().singleResult();
        this.runtimeService.setVariableLocal(t.getExecutionId(),"mainProcessInstanceId", t.getProcessInstanceId());
        this.runtimeService.setVariableLocal(t.getExecutionId(),"code", "007");
        this.taskService.complete(taId);
    }

    /**
     * 内部退回
     */
    @Test
    public void returnTasks() {
        final String taskId = "1152505";
        final String processInstanceId = this.taskService.createTaskQuery().taskId(taskId).active().singleResult().getProcessInstanceId();
        final Map<String, Object> vars = this.runtimeService.getVariables(processInstanceId);
        final Map<String,Boolean> map = new HashMap<>();
        map.put("taskProcessData", true);
        map.put("taskPeopleBankReport", true);
        vars.put("returnTasks", map);
        vars.put("flag", false);
        this.runtimeService.setVariables(processInstanceId,
            vars);

        this.taskService.complete(taskId);
    }

    /**
     * 拒绝发布
     */
    @Test
    public void rejectPulish() {
        final String taskId = "1140012";
        final Task task = this.taskService.createTaskQuery().taskId(taskId).active().singleResult();
        System.out.println("===: "+this.runtimeService.getVariableLocal(task.getProcessInstanceId(), "processInstanceId"));
        this.runtimeService.setVariableLocal(task.getProcessInstanceId(),"subcode", "001");

        final String taId = "1140023";
        final Task t = this.taskService.createTaskQuery().taskId(taId).active().singleResult();
        System.out.println("===: "+this.runtimeService.getVariableLocal(t.getProcessInstanceId(), "processInstanceId"));
        this.runtimeService.setVariableLocal(t.getProcessInstanceId(),"subcode", "001");

        this.taskService.complete(taskId);
        this.taskService.complete(taId);
    }

    /**
     * 欺诈驳回
     */
    @Test
    public void antiFraudReject() {

        final String taskId = "317671";
        final Task task = this.taskService.createTaskQuery().taskId(taskId).active().singleResult();
        System.out.println("===: "+this.runtimeService.getVariableLocal(task.getProcessInstanceId(), "processInstanceId"));
        this.runtimeService.setVariableLocal(task.getProcessInstanceId(),
            "subApprove", "010");
        this.runtimeService.setVariableLocal(task.getProcessInstanceId(),
            "approve", "010");
        this.taskService.complete(taskId);

//        final String taId = "1027520";
//        final Task t = this.taskService.createTaskQuery().taskId(taId).active().singleResult();
//        System.out.println("===: "+this.runtimeService.getVariableLocal(t.getProcessInstanceId(), "processInstanceId"));
//        this.runtimeService.setVariableLocal(t.getProcessInstanceId(),"subApprove", "010");
//        this.taskService.complete(taId);

    }

    @Test
    public void complete6() {
        final String taskId = "327351";
        this.taskService.complete(taskId);

    }

    /**
     * 删除流程实例
     */
    @Test
    public void deleteProcessInstance() {
        final String processInstanceId = "322613";
        final ProcessInstance processInstance = this.runtimeService
                .createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        this.runtimeService.deleteProcessInstance(processInstance.getId(), "");
    }

    /**
     * 完成等待人行返回
     */
    @Test
    public void signal() {
        final String processInstanceId = "329101";
        final Execution execution = this.runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .activityId("taskWaitingReportReturn").singleResult();
        this.runtimeService.signal(execution.getId());
    }

    @Test
    public void query() {
        final String processInstanceId = "317140";
        final List<Task> tasks = this.taskService.createTaskQuery()
                .processInstanceId(processInstanceId).active().list();
        if (CollectionUtils.isNotEmpty(tasks)) {
            System.out.println("==============");
            for (final Task task : tasks) {
                System.out.println(task.getName());
            }
            System.out.println("==============");
        }

        final List<HistoricTaskInstance> hisList = this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).unfinished().list();
        if(null != hisList && hisList.size()>0){
            for(final HistoricTaskInstance his : hisList){
                System.out.println("---:"+his.getId()+"---"+his.getTaskDefinitionKey()+"--"+his.getParentTaskId());
            }
        }
    }

    @Test
    public void queryExecution() {
        final String processInstanceId = "317609";
        final List<Execution> list = this.runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .activityId("taskWaitingReportReturn").list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (final Execution execution : list) {
                System.out.println("---:" + execution.getId() + "---"
                        + execution.getActivityId() + "--"
                        + execution.getProcessInstanceId());
            }
        }
    }

    /**
     * 根据ID挂起流程定义
     */
    @Test
    public void suspendProcessDefinitionById() {
        final String processDefinitionId = "oasis:9:326204";
        this.repositoryService
        .suspendProcessDefinitionById(processDefinitionId);
    }

    /**
     * 根据key挂起所有流程定义
     */
    @Test
    public void suspendProcessDefinitionByKey() {
        final String processDefinitionKey = "oasis";
        this.repositoryService
        .suspendProcessDefinitionByKey(processDefinitionKey);
    }

    /**
     * 根据ID激活流程定义
     */
    @Test
    public void activateProcessDefinitionById() {
        final String processDefinitionId = "oasis:9:326204";
        this.repositoryService
        .activateProcessDefinitionById(processDefinitionId);
    }

    /**
     * 根据key激活所有流程定义
     */
    @Test
    public void activateProcessDefinitionByKey() {
        final String processDefinitionKey = "oasis";
        this.repositoryService
        .activateProcessDefinitionByKey(processDefinitionKey);
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstanceById() {
        final String processInstanceId = "327333";
        this.runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 激活流程实例
     */
    @Test
    public void activateProcessInstanceById() {
        final String processInstanceId = "327333";
        this.runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 查询活动流程定义
     */
    @Test
    public void queryProcessDefinition() {
        final ProcessDefinitionQuery processDefinitionQuery = this.repositoryService
                .createProcessDefinitionQuery().processDefinitionKey("oasis")
                .active().orderByProcessDefinitionVersion().desc();
        final List<ProcessDefinition> list = processDefinitionQuery.list();
        if (CollectionUtils.isNotEmpty(list)) {
            System.out.println("====================");
            for (final ProcessDefinition pd : list) {
                System.out.print("Id:" + pd.getId() + "--key:" + pd.getKey());
                System.out.println("--version:" + pd.getVersion() + "--name:"
                        + pd.getName());
            }
            System.out.println("====================");
        }
    }
}