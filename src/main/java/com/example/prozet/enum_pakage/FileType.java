package com.example.prozet.enum_pakage;

public enum FileType {

    PROJECT_MAIN("PROJECT_MAIN", "project/"),
    PROJECT_NOTE("PROJECT_NOTE", "project/"),
    MEMBER_PROFILE("MEMBER_PROFILE", "member/"),
    STACK_ICON("STACK_ICON", "stack/");

    private final String fileType;
    private final String bucketName;

    FileType(String fileType, String bucketName) {
        this.fileType = fileType;
        this.bucketName = bucketName;
    }

    public String fileType() {
        return fileType;
    }

    public String bucketName() {
        return bucketName;
    }
}
