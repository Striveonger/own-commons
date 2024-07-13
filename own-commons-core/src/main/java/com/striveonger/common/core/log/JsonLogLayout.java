package com.cecbrain.omm.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.cecbrain.omm.core.utils.SpringContextHolder;

public class JsonLogLayout extends LayoutBase<ILoggingEvent> {

    private final LogJSONConverter converter = new LogJSONConverter();

    @Override
    public String doLayout(ILoggingEvent event) {
        if (SpringContextHolder.initialize()) {
            InitMDC.start();
        }
        return converter.convert(event);
    }
}
