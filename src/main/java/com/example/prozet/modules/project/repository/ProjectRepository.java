package com.example.prozet.modules.project.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prozet.modules.project.domain.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectRepositoryCustom {

    Optional<ProjectEntity> findByProjectKeyAndDeleteYn(String projectKey, String deleteYn);

    // List<ProjectEntity> findByOwner_Username(String username);

}
