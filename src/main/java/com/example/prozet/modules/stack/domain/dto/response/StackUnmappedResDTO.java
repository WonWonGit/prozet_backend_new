package com.example.prozet.modules.stack.domain.dto.response;

import com.example.prozet.enum_pakage.StackType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackUnmappedResDTO {

    private Long idx;
    private String name;
    private String icon;
    private StackType stackType;
    private String projectKey;
    private String stackCategory;

}
