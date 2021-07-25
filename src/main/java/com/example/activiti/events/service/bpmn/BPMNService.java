package com.example.activiti.events.service.bpmn;

import com.example.activiti.events.controller.ActivitiController;
import com.example.activiti.events.model.Signal;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class BPMNService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BPMNService.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    public String startProcess(String processDefinitionKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        String processInstanceId = processInstance.getId();
        LOGGER.info("Process started. Process instance id: {}", processInstanceId);
        return processInstanceId;
    }

    public String deployProcess() throws IOException {
        Deployment deployment = repositoryService.createDeploymentQuery().processDefinitionKey("fileMergingProcessId").singleResult();
        if (deployment == null) {
            String processDefinition = loadProcessDefinition();
            deployment = repositoryService.createDeployment().name("file_merging_process_deployment").addString("file_merging_process.bpmn20.xml", processDefinition).deploy();
            LOGGER.info("Process deployed.");
        } else {
            LOGGER.info("Process already deployed.");
        }
        return deployment.getId();
    }

    public void signalProcess(String processInstanceId, Signal signal) {
        if (runtimeService.createExecutionQuery().processInstanceId(processInstanceId).count() == 0)
            throw new ActivitiException("No process instance found for processInstanceId" + processInstanceId);
        Execution signalExecution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).signalEventSubscriptionName(signal.getProcessSignalName()).singleResult();
        if (signalExecution == null)
            throw new ActivitiException("No signal event subscription with the name " + signal.getProcessSignalName() + " found for process with process instance Id " + processInstanceId + ".");
        Map<String, Object> processVariables = new HashMap<>();
        switch (signal.getProcessSignalName()) {
            case "file1ReceivedSignalName":
                processVariables.put("signal1", signal);
                break;
            case "file2ReceivedSignalName":
                processVariables.put("signal2", signal);
                break;
            case "file3ReceivedSignalName":
                processVariables.put("signal3", signal);
                break;
            default:
                LOGGER.warn("Process signal name not available in process!");
                throw new ActivitiException("Process signal name not available in process!");
        }
        runtimeService.signalEventReceived(signal.getProcessSignalName(), signalExecution.getId(), processVariables);
        LOGGER.info("Signal sent.");
    }

    public long countRunningProcesses() {
        return runtimeService.createExecutionQuery().count();
    }

    private String loadProcessDefinition() throws IOException {
        InputStream inputStream = new ClassPathResource("processes/file_merging_process.bpmn20.xml").getInputStream();
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStream, stringWriter, StandardCharsets.UTF_8);
        String processDefinition = stringWriter.toString();
        inputStream.close();
        stringWriter.close();
        return processDefinition;
    }

}
