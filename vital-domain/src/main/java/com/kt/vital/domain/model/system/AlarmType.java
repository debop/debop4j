package com.kt.vital.domain.model.system;

/**
 * com.kt.vital.domain.model.system.AlarmType
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 5:21
 */
public enum AlarmType {

    None("None"),
    Trace("Trace"),
    Debug("Debug"),
    Info("Info"),
    Warn("Warn"),
    Error("Error"),
    Fatal("Fatal");

    private final String alarmType;

    AlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
}
