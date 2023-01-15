package com.example.prozet.modules.projectMember.domain.dto.response;

import com.example.prozet.enum_pakage.AccessType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectMemberListResDTO {

    private Long idx;
    private String username;
    private AccessType access;

    public ProjectMemberListResDTO(Long idx, AccessType access, String username) {
        this.idx = idx;
        this.access = access;
        this.username = username;
    }

}
