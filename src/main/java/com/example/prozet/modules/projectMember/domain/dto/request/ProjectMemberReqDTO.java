package com.example.prozet.modules.projectMember.domain.dto.request;

import javax.validation.constraints.NotBlank;

import com.example.prozet.enum_pakage.AccessType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberReqDTO {

    @NotBlank(message = "Username is empty")
    private String username;

    @NotBlank(message = "ProjectKey is empty")
    private String projectKey;

    private AccessType access;

}
