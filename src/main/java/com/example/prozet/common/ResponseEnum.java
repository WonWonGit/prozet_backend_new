package com.example.prozet.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    // 토큰 관련
    CREATE_TOKEN_SUCCESS(HttpStatus.OK, "Create Token Success"),

    // MEMEBR INFO
    SAVE_MEMBERINFO_SUCCESS(HttpStatus.OK, "Save member information"),
    FIND_MEMBERINFO_SUCCESS(HttpStatus.OK, "Find member information"),
    UPDATE_MEMBERINFO_SUCCESS(HttpStatus.OK, "Update member information"),

    CREATE_PROJECT_SUCCESS(HttpStatus.OK, "Create project"),
    DELETE_PROJECT_SUCCESS(HttpStatus.OK, "Delete project"),
    SAVE_PROJECT_INFO_SUCCESS(HttpStatus.OK, "Save project informaion"),
    UPDATE_PROJECT_INFO_SUCCESS(HttpStatus.OK, "Update Project information"),
    FIND_PROJECT_LIST(HttpStatus.OK, "Find project list"),

    SAVE_MEMBER_INVITE_SUCCESS(HttpStatus.OK, "Member invite done"),
    DELETE_MEMBER_SUCCESS(HttpStatus.OK, "delete done"),
    UPDATE_MEMBER_ACCESS_SUCCESS(HttpStatus.OK, "update done"),

    SAVE_STACK_SUCCESS(HttpStatus.OK, "Save Stack done"),
    DELETE_STACK_SUCCESS(HttpStatus.OK, "Delete Stack done"),

    SAVE_STACK_CATEGORY_SUCCESS(HttpStatus.OK, "Save stack category done"),
    FIND_STACK_CATEGORY_SUCCESS(HttpStatus.OK, "Stack Category list"),

    SAVE_PROJECT_STACK_SUCCESS(HttpStatus.OK, "Save project stack done"),
    UPDATE_PROJECT_STACK_SUCCESS(HttpStatus.OK, "Update project stack done"),

    FIND_STACK_SUCCESS(HttpStatus.OK, "Stack list"),

    SAVE_PROJECT_SCHEDULE_SECCESS(HttpStatus.OK, "Save project schedule");

    private final HttpStatus httpCode;
    private final String message;

    ResponseEnum(HttpStatus httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public HttpStatus httpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }
}
