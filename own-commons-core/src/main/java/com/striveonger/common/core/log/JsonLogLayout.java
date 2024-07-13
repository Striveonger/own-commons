package com.striveonger.common.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.striveonger.common.core.web.SpringContextHolder;

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
