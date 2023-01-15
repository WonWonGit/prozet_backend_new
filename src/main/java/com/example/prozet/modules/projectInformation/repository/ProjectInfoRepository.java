package com.example.prozet.modules.projectInformation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, Long> {

    // Optional<ProjectInfoEntity> findByProjectEntity_ProjectKey(String
    // projectKey);

}
