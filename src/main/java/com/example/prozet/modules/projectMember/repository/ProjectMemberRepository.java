package com.example.prozet.modules.projectMember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMemberEntity, Long>, ProjectMemberRepositoryCustom {

    Optional<ProjectMemberEntity> findByProjectEntity_ProjectKey(String projectKey);

}
