package com.example.prozet.modules.projectMember.domain.dto.response;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectMemberResDTO {

    private Long idx;
    private AccessType access;
    private StateType state;
    private String deleteYn;
    private MemberResDTO memberResDTO;
    private ProjectResDTO projectResDTO;
    private ProjectMemberType projectMemberType;

    public ProjectMemberEntity toEntity() {
        return ProjectMemberEntity.builder()
                .idx(idx)
                .access(access)
                .state(state)
                .deleteYn(deleteYn)
                .memberEntity(memberResDTO.toEntity())
                .projectEntity(projectResDTO.toEntity())
                .projectMemberType(projectMemberType)
                .build();
    }

}
