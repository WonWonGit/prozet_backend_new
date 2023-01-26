package com.example.prozet.modules.projectStack.domain.dto.response;

import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectStackResDTO {

    private Long idx;
    private StackResDTO stackResDTO;
    private String checkedYn;

}
