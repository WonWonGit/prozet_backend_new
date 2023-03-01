package com.example.prozet.modules.projectSchedule.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleEditReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleListResDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.projectSchedule.service.ProjectScheduleService;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;
import com.example.prozet.modules.schedule.service.ScheduleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("v1/api/projectschedule")
public class ProjectScheduleApiController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ProjectScheduleService projectScheduleService;

    @Autowired
    private ProjectMemberService projectMemberService;

    public ResponseEntity<?> saveProjectSchedule(
            @RequestBody ProjectScheduleSaveReqDTO projectScheduleSaveReqDTO) {

        ScheduleResDTO scheduleResDTO = scheduleService.saveSchedule(projectScheduleSaveReqDTO.getScheduleReqDTO());

        List<ProjectScheduleResDTO> projectScheduleResDTOList = new ArrayList<>();

        if (scheduleResDTO == null) {

            return ErrorResponse.toResponseEntity(ErrorCode.SAVE_SCHEDULE_FAIL);

        }

        projectScheduleSaveReqDTO.getProjectMemberIdx().forEach(projectMemberIdx -> {
            ProjectMemberResDTO projectMemberResDTO = projectMemberService.getProjectMemberByIdx(projectMemberIdx);
            if (projectMemberResDTO != null) {
                ProjectScheduleResDTO projectScheduleResDTO = projectScheduleService.saveProjectSchedule(
                        projectMemberResDTO.getProjectResDTO(),
                        projectScheduleSaveReqDTO, projectMemberResDTO);
                projectScheduleResDTOList.add(projectScheduleResDTO);
            }
        });

        Map<ScheduleResDTO, List<ProjectScheduleResDTO>> projectScheduleResDTO = projectScheduleResDTOList.stream()
                .collect(Collectors.groupingBy(ProjectScheduleResDTO::getScheduleResDTO));

        return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_PROJECT_SCHEDULE_SECCESS, projectScheduleResDTO);

    }

    @PutMapping("/scheduletype/{idx}")
    public ResponseEntity<?> updateProjectScheduleType(@PathVariable Long idx, @RequestBody ScheduleType scheduleType) {

        ProjectScheduleListResDTO projectScheduleResDTO = projectScheduleService
                .editProjectScheduleType(idx, scheduleType);

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_PROJECT_SCHEDULE_SECCESS, projectScheduleResDTO);

    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> updateProjectSchedule(@PathVariable Long idx,
            @RequestBody ProjectScheduleEditReqDTO projectScheduleEditReqDTO) {

        ScheduleResDTO scheduleResDTO = scheduleService.editSchedule(projectScheduleEditReqDTO.getScheduleEditReqDTO());

        Map<ScheduleResDTO, List<ProjectScheduleResDTO>> projectScheduleResDTO = projectScheduleService
                .findProjectSchedule(idx);

        List<ProjectScheduleResDTO> projectScheduleResDTOList = new ArrayList<>();

        if (!projectScheduleEditReqDTO.getDeleteProjectMemberIdx().isEmpty()) {
            projectScheduleEditReqDTO.getDeleteProjectMemberIdx().stream().forEach(projectMemberIdx -> {
                projectScheduleService.editProjectScheudleDeleteMember(idx, projectMemberIdx);
            });
        }

        if (!projectScheduleEditReqDTO.getAddProjectMemberIdx().isEmpty()) {
            projectScheduleEditReqDTO.getAddProjectMemberIdx()
                    .stream().forEach(projectMemberIdx -> {
                        ProjectScheduleResDTO projectScheduleResDTO2 = projectScheduleService
                                .editProjectScheudleAddMember(idx, projectMemberIdx);
                        projectScheduleResDTOList.add(projectScheduleResDTO2);
                    });
        }

        projectScheduleResDTO.put(scheduleResDTO, projectScheduleResDTOList);

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_PROJECT_SCHEDULE_SECCESS, projectScheduleResDTO);

    }

}
