package com.example.prozet.modules.projectStack.domain.dto.response;

import com.example.prozet.modules.stack.domain.dto.response.StackUnmappedResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectStackUnmappedResDTO {

    private Long idx;
    private StackUnmappedResDTO stackUnmappedResDTO;
    private String checkedYn;

}
