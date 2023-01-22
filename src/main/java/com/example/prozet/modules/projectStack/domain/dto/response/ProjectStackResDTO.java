package com.example.prozet.modules.projectStack.domain.dto.response;

import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.domain.entity.StackEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.ion.apps.PrintApp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStackResDTO {

    private Long idx;
    private StackResDTO stackResDTO;

}
