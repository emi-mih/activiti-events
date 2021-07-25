package com.example.activiti.events.controller;

import com.example.activiti.events.model.Signal;
import com.example.activiti.events.service.bpmn.BPMNService;
import com.example.activiti.events.service.simulation.SignalProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("process")
public class ActivitiController {

    @Autowired
    BPMNService bpmnService;

    @Autowired
    SignalProducerService signalProducerService;

    @GetMapping("signal")
    public void signalProcess(@RequestParam String processInstanceId, @RequestParam String processSignalName, @RequestParam String fileName, @RequestParam String fileContent) {
        Signal signal = new Signal();
        signal.setProcessSignalName(processSignalName);
        signal.setFileName(fileName);
        signal.setFileContent(fileContent);
        bpmnService.signalProcess(processInstanceId, signal);
    }

    @GetMapping("start")
    public String startProcess(@RequestParam String processDefinitionKey) {
        String processInstanceId = bpmnService.startProcess(processDefinitionKey);
        return processInstanceId;
    }

    @GetMapping("deploy")
    public String deployProcess() throws IOException {
        String deploymentId = bpmnService.deployProcess();
        return deploymentId;
    }

    @GetMapping("simulate/parallel")
    public void simulateParallel() throws IOException {
        signalProducerService.generateSignalsConcurrent();
    }

    @GetMapping("simulate/sequential")
    public void simulateSequential() throws IOException {
        signalProducerService.generateSignalsSequential();
    }
}
