package com.example.prozet.modules.projectStack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackUnmappedResDTO;
import com.example.prozet.modules.projectStack.service.ProjectStackService;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.service.StackService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/project/stack")
public class ProjectStackApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StackService stackService;

    @Autowired
    private ProjectStackService projectStackService;

    @PostMapping("/{projectKey}")
    public ResponseEntity<?> saveProjectStack(@RequestBody List<Long> stackIdxList,
            @PathVariable String projectKey,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();
        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        List<ProjectStackUnmappedResDTO> projectStackResDTOList = new ArrayList<>();

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean membeAccessEdit = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);
        boolean projectOwner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (membeAccessEdit || projectOwner) {

            stackIdxList.forEach(stackIdx -> {

                ProjectStackUnmappedResDTO projectStackResDTO = projectStackService.saveProjectStack(stackIdx,
                        projectResDTO);
                projectStackResDTOList.add(projectStackResDTO);

            });

            if (projectStackResDTOList.isEmpty()) {

                return ErrorResponse.toResponseEntity(ErrorCode.SAVE_PROJECT_STACK_FAIL);

            } else {

                return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_PROJECT_STACK_SUCCESS, projectStackResDTOList);

            }

        }

        return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_STACK_UNAUTHORIZED);

    }

    @PutMapping("/{projectKey}")
    public ResponseEntity<?> editProjectStack(@RequestBody List<Long> stackIdxList,
            @PathVariable String projectKey,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();
        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        List<ProjectStackUnmappedResDTO> projectStackResDTOList = new ArrayList<>();

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean membeAccessEdit = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);
        boolean projectOwner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (membeAccessEdit || projectOwner) {

            stackIdxList.forEach(stackIdx -> {
                ProjectStackResDTO projectStackResDTO = projectStackService.findProjectStack(stackIdx, projectKey);

                if (projectStackResDTO != null) {
                    ProjectStackUnmappedResDTO projectStackUnmappedResDTO = projectStackService
                            .editProjectStack(projectStackResDTO.getIdx());
                    projectStackResDTOList.add(projectStackUnmappedResDTO);

                } else {

                    ProjectStackUnmappedResDTO projectStackUnmappedResDTO = projectStackService.saveProjectStack(
                            stackIdx,
                            projectResDTO);
                    projectStackResDTOList.add(projectStackUnmappedResDTO);

                }

            });

            if (projectStackResDTOList.isEmpty()) {

                return ErrorResponse.toResponseEntity(ErrorCode.UPDATE_PROJECT_STACK_FAIL);

            } else {
                return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_PROJECT_STACK_SUCCESS, projectStackResDTOList);

            }

        }

        return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_STACK_UNAUTHORIZED);

    }

}
