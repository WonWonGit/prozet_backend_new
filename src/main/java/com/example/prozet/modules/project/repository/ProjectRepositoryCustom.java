package com.example.prozet.modules.project.repository;

import java.util.List;

import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;

public interface ProjectRepositoryCustom {

    List<ProjectListDTO> getProjectList(String username);

    ProjectResDTO getProjectByProjectKey(String projectKey);

}
