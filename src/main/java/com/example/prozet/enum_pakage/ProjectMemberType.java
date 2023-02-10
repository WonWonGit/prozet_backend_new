package com.example.prozet.enum_pakage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectMemberType {

    OWNER("OWNER"),
    TEAMMEMBER("TEAMMEMBER");

    private final String projectMemberType;

    private ProjectMemberType(String projectMemberType) {
        this.projectMemberType = projectMemberType;
    }

    @JsonValue
    public String projectMemberType() {
        return projectMemberType;
    }

    @JsonCreator
    public static ProjectMemberType from(String projectMemberType) {
        return ProjectMemberType.valueOf(projectMemberType.toUpperCase());
    }

}
