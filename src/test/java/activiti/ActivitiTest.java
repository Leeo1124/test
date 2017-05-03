package activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ActivitiTest {

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
        builder.name("test流程").addClasspathResource("bpmn/test.bpmn")
            .addClasspathResource("bpmn/test.png");
        builder.deploy();
    }

    @Test
    public void start() {
        Map<String, Object> var = new HashMap<>();
        var.put("name", "hcy");
        final ProcessInstance pi = this.runtimeService
            .startProcessInstanceByKey("test", "12345", var);
        System.out.println("pid:" + pi.getId() + ",activitiId:"
                + pi.getActivityId());
    }

    @Test
    public void claim() {
        String taskId = "35005";
        String userId = "hcy";
        this.taskService.claim(taskId, userId);
    }

    @Test
    public void unclaim() {
        String taskId = "35005";
        this.taskService.unclaim(taskId);
    }

    @Test
    public void complete() {
        String taskId = "35005";
        this.taskService.complete(taskId);
    }

    @Test
    public void deleteProcessInstance() {
        final String processInstanceId = "35001";
        this.runtimeService.deleteProcessInstance(processInstanceId, "删除");
    }

    @Test
    public void delDeployment() {
        final String deploymentId = "5001";
        final String[] array = deploymentId.split(",");
        if (ArrayUtils.isNotEmpty(array)) {
            for (final String id : array) {
                this.repositoryService.deleteDeployment(id, true);
            }
        }
    }
}