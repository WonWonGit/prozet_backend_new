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
    EDIT_MEMBER_ACCESS_SUCCESS(HttpStatus.OK, "update done");

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
