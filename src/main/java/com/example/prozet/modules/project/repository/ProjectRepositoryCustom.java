package com.example.prozet.modules.project.repository;

import java.util.List;

import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;

public interface ProjectRepositoryCustom {

    List<ProjectListDTO> getProjectList(String username);
}
