package com.example.prozet.modules.projectMember.domain.dto.response;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectMemberFindResDTO {

    private Long idx;
    private AccessType access;
    private StateType state;
    private String deleteYn;
    private MemberResDTO memberResDTO;

}
