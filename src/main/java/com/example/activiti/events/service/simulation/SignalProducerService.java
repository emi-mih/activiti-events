package com.example.activiti.events.service.simulation;

import com.example.activiti.events.model.Signal;
import com.example.activiti.events.service.bpmn.BPMNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class SignalProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalProducerService.class);

    private static String PROCESS_DEFINITION_KEY = "fileMergingProcessId";
    private static List<String> SIGNAL_NAMES_HASH = List.of("file1ReceivedSignalName", "file2ReceivedSignalName", "file3ReceivedSignalName");

    @Autowired
    BPMNService bpmnService;

    public void generateSignalsConcurrent() throws IOException {
        LocalDateTime startDateTime = LocalDateTime.now();
        LOGGER.info("Simulation started at {}.", startDateTime);
        LOGGER.info("Running process instances : {}", bpmnService.countRunningProcesses());
        bpmnService.deployProcess();
        IntStream.range(0, 1000).parallel().forEach(processIndex -> {
            String processInstanceId = bpmnService.startProcess(PROCESS_DEFINITION_KEY);
            Runnable senSignalsRunnable = () -> {
                final Object processExecutionLock = new Object();
                IntStream.range(0, 3).parallel().forEach(signalIndex -> {
                    synchronized (processExecutionLock) {
                        Signal signal = new Signal();
                        signal.setFileName("file_" + processIndex + "_" + signalIndex);
                        signal.setFileContent("fileContent_" + processIndex + "_" + signalIndex);
                        signal.setProcessSignalName(SIGNAL_NAMES_HASH.get(signalIndex));
                        bpmnService.signalProcess(processInstanceId, signal);
                    }
                });
            };
            Thread sendSignalsThread = new Thread(senSignalsRunnable);
            sendSignalsThread.start();
        });
        LocalDateTime endDateTime = LocalDateTime.now();
        LOGGER.info("Simulation finished at {} and took {} to send all signals.", endDateTime, Duration.between(startDateTime, endDateTime));
        LOGGER.info("Running process instances : {}", bpmnService.countRunningProcesses());
    }

    public void generateSignalsSequential() throws IOException {
        LocalDateTime startDateTime = LocalDateTime.now();
        LOGGER.info("Simulation started at {}.", startDateTime);
        LOGGER.info("Running process instances : {}", bpmnService.countRunningProcesses());
        bpmnService.deployProcess();
        IntStream.range(0, 1000).forEach(processIndex -> {
            String processInstanceId = bpmnService.startProcess(PROCESS_DEFINITION_KEY);
            IntStream.range(0, 3).forEach(signalIndex -> {
                Signal signal = new Signal();
                signal.setFileName("file_" + processIndex + "_" + signalIndex);
                signal.setFileContent("fileContent_" + processIndex + "_" + signalIndex);
                signal.setProcessSignalName(SIGNAL_NAMES_HASH.get(signalIndex));
                bpmnService.signalProcess(processInstanceId, signal);
            });
        });
        LocalDateTime endDateTime = LocalDateTime.now();
        LOGGER.info("Simulation finished at {} and took {} to send all signals.", endDateTime, Duration.between(startDateTime, endDateTime));
        LOGGER.info("Running process instances : {}", bpmnService.countRunningProcesses());
    }
}
