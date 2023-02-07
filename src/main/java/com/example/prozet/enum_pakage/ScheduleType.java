package com.example.prozet.enum_pakage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ScheduleType {

    OPEN("OPEN"),
    INPROGRESS("INPROGRESS"),
    COMPLETED("COMPLETED");

    private final String scheduleType;

    private ScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    @JsonValue
    public String scheduleType() {
        return scheduleType;
    }

    @JsonCreator
    public static ScheduleType from(String scheduleType) {
        return ScheduleType.valueOf(scheduleType.toUpperCase());
    }

}
