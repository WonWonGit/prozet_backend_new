package com.example.prozet.modules.projectMember.repository;

import java.util.List;
import java.util.Optional;

import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberListResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;

public interface ProjectMemberRepositoryCustom {

    ProjectMemberResDTO getInvitedMember(String projectKey, String username);

    Optional<ProjectMemberResDTO> getProjectMember(Long idx);

    List<ProjectMemberListResDTO> getProjectMemberList(String projectKey);

    List<ProjectMemberListResDTO> getEditProjectMemberList(String projectKey);

}
