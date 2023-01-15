package com.example.prozet.modules.project.domain.dto.response;

import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
// @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectResDTO {

    private Long idx;
    private String projectKey;
    private String deleteYn;
    private MemberResDTO owner;
    private ProjectInfoResDTO projectInfoResDTO;

    public ProjectEntity toEntity() {
        return ProjectEntity.builder()
                .idx(idx)
                .projectKey(projectKey)
                .deleteYn(deleteYn)
                .owner(owner.toEntity())
                .projectInformation(projectInfoResDTO.toEntity())
                .build();
    }

}
