package com.example.prozet.modules.projectSchedule.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleEditReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;

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

    @Transactional
    public Map<ScheduleResDTO, List<ProjectScheduleResDTO>> editProjectScheduleType(Long idx,
            ScheduleType scheduleType) {

        List<ProjectScheduleEntity> projectScheduleEntity = projectScheduleRepository.findByScheduleEntity_Idx(idx);

        projectScheduleEntity.forEach((projectSchedule) -> {
            projectSchedule.editProjectScheduleType(scheduleType);
        });

        List<ProjectScheduleResDTO> projectScheduleResDTOs = projectScheduleEntity.stream()
                .map(ProjectScheduleEntity::toProjectScheduleResDTO).collect(Collectors.toList());

        Map<ScheduleResDTO, List<ProjectScheduleResDTO>> projectSchduleResDTO = projectScheduleResDTOs.stream()
                .collect(Collectors.groupingBy(ProjectScheduleResDTO::getScheduleResDTO));

        return projectSchduleResDTO;

    }

    public void editProjectScheudleDeleteMember(Long projectScheduleIdx, Long projectMemberIdx) {

        // ProjectScheduleEntity projectScheduleEntity =
        // projectScheduleRepository.findByIdxAndProjectMemberEntity_Idx(idx)
        // .orElseThrow(() -> new
        // CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        // projectScheduleRepository.delete(projectScheduleEntity);

    }

    public void editProjectScheudleAddMember(Long idx) {

    }

    public ProjectScheduleResDTO findProjectSchedule(Long idx) {

        ProjectScheduleEntity projectScheduleEntity = projectScheduleRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        return projectScheduleEntity.toProjectScheduleResDTO();
    }

}
