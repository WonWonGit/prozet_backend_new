package com.example.prozet.common;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * not exist from db -> 500
 * controller not found -> 404
 * 
 */
@Getter
public enum ErrorCode {

    // token
    TOKEN_NULL_ERROR(HttpStatus.FORBIDDEN, "ECT000", "Token is not exist"),
    INVALIDED_REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ECT001", "Refresh Token is invalid"),
    INVALIDED_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ECT002", "Access Token is invalid"),
    EXPIRED_REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ECT003", "Refresh Token is expired"),
    EXPIRED_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ECT004", "Access Token is expired"),

    // user
    USER_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECU000", "User is not exist"),
    SAVE_MEMBERINFO_FAIL(HttpStatus.BAD_REQUEST, "ECU001", "Fail to save member information"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "ECU000", "User idx is not same"),
    MEMBER_INFO_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECU000", "Member information is not exist"),
    MEMBER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "ECU000", "Member information is not exist"),
    MEMBER_INFO_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "ECU000", "Member information update fail"),

    FILE_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECF000", "File is not exist"),
    FILE_MASTER_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECF000", "File master is not exist"),

    CREATE_PROJECT_FAIL(HttpStatus.BAD_REQUEST, "ECP000", "Fail create project"),
    PROJECT_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECP000", "Fail find project"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "ECP000", "Fail find project"),
    SAVE_PROJECT_FAIL(HttpStatus.BAD_REQUEST, "ECP001", "Faile save project"),
    PROJECT_OWNER_ONLY(HttpStatus.FORBIDDEN, "ECP000", "Project owner only"),

    FIND_PROJECT_INFO_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ECPI00", "Fail find project information"),
    PROJECT_INFO_EXIST(HttpStatus.BAD_GATEWAY, "ECPI01", "Project Info Already Exist"),
    PROJECT_INFO_UPDATE_FAIL(HttpStatus.BAD_GATEWAY, "ECPI02", "Fail update project information"),

    FAIL_MEMBER_INVITE(HttpStatus.BAD_REQUEST, "ECPM000", "Fail invite member"),
    FAIL_MEMBER_ACCESS_EDIT(HttpStatus.BAD_REQUEST, "ECPM001", "Fail update member access"),
    PROJECT_INVITATION_EXPIRED(HttpStatus.UNAUTHORIZED, "ECPM000", "Invitation is expired"),
    PROJECT_MEMBER_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECPM000", "Project member is not exist"),

    STACK_CATEGORY_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "ECS000", "Stack category is not exist");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
