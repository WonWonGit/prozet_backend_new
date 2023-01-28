package com.example.prozet.modules.projectStack.domain.dto.response;

import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackUnmappedResDTO;

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
    private ProjectResDTO projctResDTO;
    private String checkedYn;

    public ProjectStackEntity toEntity() {
        return ProjectStackEntity.builder()
                .idx(idx)
                .projectEntity(projctResDTO != null ? projctResDTO.toEntity() : null)
                .checkedYn(checkedYn)
                .build();
    }

}
