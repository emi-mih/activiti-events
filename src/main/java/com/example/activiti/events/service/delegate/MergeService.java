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
public class MergeService implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeService.class);

    private Expression signal1;
    private Expression signal2;
    private Expression signal3;

    @Override
    public void execute(DelegateExecution execution) {
        Signal signal1Value = (Signal) signal1.getValue(execution);
        Signal signal2Value = (Signal) signal2.getValue(execution);
        Signal signal3Value = (Signal) signal3.getValue(execution);
        LOGGER.info("Merging results from files [{},{},{}]. Result content : [{}].", signal1Value.getFileName(), signal2Value.getFileName(), signal3Value.getFileName(), signal1Value.getFileContent() + signal2Value.getFileContent() + signal3Value.getFileContent());
    }

    public Expression getSignal1() {
        return signal1;
    }

    public void setSignal1(Expression signal1) {
        this.signal1 = signal1;
    }

    public Expression getSignal2() {
        return signal2;
    }

    public void setSignal2(Expression signal2) {
        this.signal2 = signal2;
    }

    public Expression getSignal3() {
        return signal3;
    }

    public void setSignal3(Expression signal3) {
        this.signal3 = signal3;
    }
}
