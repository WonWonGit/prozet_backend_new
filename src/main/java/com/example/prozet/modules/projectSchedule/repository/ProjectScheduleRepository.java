package com.example.prozet.modules.projectSchedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;

public interface ProjectScheduleRepository extends JpaRepository<ProjectScheduleEntity, Long> {

    Optional<ProjectScheduleEntity> findByIdx(Long idx);

    List<ProjectScheduleEntity> findByScheduleEntity_Idx(Long idx);

    Optional<ProjectScheduleEntity> findByScheduleEntity_IdxAndProjectMemberEntity_Idx(Long scheduleIdx,
            Long projectMemberIdx);

}
