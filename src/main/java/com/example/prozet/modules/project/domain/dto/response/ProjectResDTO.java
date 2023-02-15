package com.example.prozet.modules.project.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
// @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectResDTO {

    private Long idx;
    private String projectKey;
    private String deleteYn;
    private ProjectMemberResDTO owner;
    private ProjectInfoResDTO projectInfoResDTO;
    private List<ProjectMemberResDTO> projectMemberResDTO;

    public ProjectEntity toEntity() {
        return ProjectEntity.builder()
                .idx(idx)
                .projectKey(projectKey)
                .deleteYn(deleteYn)
                // .owner(owner.toEntity())
                .projectInformation(projectInfoResDTO != null ? projectInfoResDTO.toEntity() : null)
                .projectMemberEntity(
                        projectMemberResDTO != null ? projectMemberResDTO.stream().map(ProjectMemberResDTO::toEntity)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public ProjectResDTO(Long idx, String projectKey, String deleteYn,
            ProjectInfoEntity projectInfoResDTO) {
        this.idx = idx;
        this.projectKey = projectKey;
        this.deleteYn = deleteYn;
        this.projectInfoResDTO = projectInfoResDTO.toProjectInfoResDTO();

    }

}
