package com.example.prozet.modules.projectSchedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;

public interface ProjectScheduleRepository extends JpaRepository<ProjectScheduleEntity, Long> {

}
