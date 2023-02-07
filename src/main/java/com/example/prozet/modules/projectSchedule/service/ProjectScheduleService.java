package com.example.prozet.modules.projectSchedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;

@Service
public class ProjectScheduleService {

    @Autowired
    private ProjectScheduleRepository projectScheduleRepository;

    public ProjectScheduleResDTO saveProjectSchedule(ProjectResDTO projectResDTO,
            ProjectScheduleSaveReqDTO projectScheduleSaveReqDTO,
            ProjectMemberResDTO projectMemberResDTO) {

        ProjectScheduleEntity projectScheduleEntity = ProjectScheduleEntity.builder()
                .projectEntity(projectResDTO.toEntity())
                .scheduleEntity(projectScheduleSaveReqDTO.getScheduleReqDTO().toEntity())
                .projectMemberEntity(projectMemberResDTO.toEntity())
                .scheduleType(projectScheduleSaveReqDTO.getScheduleType())
                .build();

        ProjectScheduleEntity projectScheduleEntityPS = projectScheduleRepository.save(projectScheduleEntity);

        if (projectScheduleEntityPS != null) {
            return projectScheduleEntityPS.toProjectScheduleResDTO();
        }

        return null;
    }

}
