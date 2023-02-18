package com.example.prozet.modules.projectMember.domain.dto.request;

import javax.validation.constraints.NotEmpty;

import com.example.prozet.enum_pakage.AccessType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberEditReqDTO {

    private AccessType accessType;
    @NotEmpty(message = "Require project key")
    private String projectKey;

}
