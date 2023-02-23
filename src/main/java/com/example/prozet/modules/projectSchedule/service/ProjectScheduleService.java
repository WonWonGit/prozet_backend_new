package com.example.prozet.modules.projectSchedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;

@Service
@Transactional(readOnly = true)
public class ProjectScheduleService {

    @Autowired
    private ProjectScheduleRepository projectScheduleRepository;

    @Transactional
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

    public ProjectScheduleResDTO editProjectScheduleType(Long idx, ScheduleType scheduleType) {

        ProjectScheduleEntity projectScheduleEntity = projectScheduleRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        projectScheduleEntity.editProjectScheduleType(scheduleType);

        return projectScheduleEntity.toProjectScheduleResDTO();

    }

    public ProjectScheduleResDTO findProjectSchedule(Long idx) {

        ProjectScheduleEntity projectScheduleEntity = projectScheduleRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        return projectScheduleEntity.toProjectScheduleResDTO();
    }

}
