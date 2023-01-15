package com.example.prozet.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ResponseDTO<T> {

    private final int httpCode;
    private final String message;
    private final T content;

    // public ResponseDTO(ResponseEnum respEnum) {
    //     this.httpCode = respEnum.getHttpCode();
    //     this.message = respEnum.getMessage();
    //     this.content = null;
    // }

    public static ResponseEntity<?> toResponseEntity(ResponseEnum responseEnum, Object content){

        return ResponseEntity.status(responseEnum.getHttpCode())
                             .body(ResponseDTO.builder()
                                            .httpCode(responseEnum.httpCode().value())
                                            .message(responseEnum.getMessage())
                                            .content(content)
                                            .build()
                                            );

    }
}