package com.example.activiti.events.service.delegate;

import com.example.activiti.events.model.Signal;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class LogService implements JavaDelegate {

    private Expression signal;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Override
    public void execute(DelegateExecution execution) {
        Signal signalValue = (Signal) signal.getValue(execution);
        LOGGER.info("Received file [{}] with content [{}].", signalValue.getFileName(), signalValue.getFileContent());
    }


    public Expression getSignal() {
        return signal;
    }

    public void setSignal(Expression signal) {
        this.signal = signal;
    }
}

