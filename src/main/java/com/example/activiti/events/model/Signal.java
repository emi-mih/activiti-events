package com.example.activiti.events.model;

import java.io.Serializable;

public class Signal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileContent;
    private String processSignalName;

    public Signal(String fileName, String fileContent, String processSignalName) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.processSignalName = processSignalName;
    }

    public Signal() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getProcessSignalName() {
        return processSignalName;
    }

    public void setProcessSignalName(String processSignalName) {
        this.processSignalName = processSignalName;
    }
}
