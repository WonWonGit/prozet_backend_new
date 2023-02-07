package com.example.prozet.modules.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

}
