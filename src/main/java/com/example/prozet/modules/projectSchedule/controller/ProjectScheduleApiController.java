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
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
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

        Map<Long, List<ProjectScheduleResDTO>> projectScheduleResDTO = projectScheduleResDTOList.stream()
                .collect(Collectors.groupingBy(projectSchedule -> projectSchedule.getScheduleResDTO().getIdx()));

        return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_PROJECT_SCHEDULE_SECCESS, projectScheduleResDTO);

    }

    @PutMapping("/scheduletype/{idx}")
    public ResponseEntity<?> updateScheduleType(@PathVariable Long idx, @RequestBody ScheduleType scheduleType) {

        ProjectScheduleResDTO projectScheduleResDTO = projectScheduleService.editProjectScheduleType(idx, scheduleType);

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_PROJECT_SCHEDULE_SECCESS, projectScheduleResDTO);

    }

}
