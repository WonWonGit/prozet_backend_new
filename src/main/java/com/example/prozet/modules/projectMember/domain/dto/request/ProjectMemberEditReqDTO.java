package com.example.prozet.modules.projectMember.domain.dto.request;

import com.example.prozet.enum_pakage.AccessType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberEditReqDTO {

    private Long idx;
    private AccessType accessType;

}
