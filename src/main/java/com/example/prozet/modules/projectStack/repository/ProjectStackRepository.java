package com.example.prozet.modules.projectStack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;

public interface ProjectStackRepository extends JpaRepository<ProjectStackEntity, Long> {

    Optional<ProjectStackEntity> findByIdx(Long idx);

}
