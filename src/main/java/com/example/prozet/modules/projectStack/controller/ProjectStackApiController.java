package com.example.prozet.modules.projectStack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.example.prozet.modules.projectStack.service.ProjectStackService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/project/stack")
public class ProjectStackApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectStackService projectStackService;

    @PostMapping("/{projectKey}")
    public ResponseEntity<?> saveProjectStack(@RequestBody List<Long> projectStackIdxList,
            @PathVariable String projectKey,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();
        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean membeAccessEdit = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);
        boolean projectOwner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (membeAccessEdit || projectOwner) {

            List<ProjectStackResDTO> projectStackResDTO = projectStackService.saveProjectStack(projectStackIdxList);

            if (projectStackResDTO.isEmpty()) {

                return ErrorResponse.toResponseEntity(ErrorCode.SAVE_PROJECT_STACK_FAIL);

            } else {
                return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_PROJECT_STACK_SUCCESS, projectStackResDTO);

            }

        }

        return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_STACK_UNAUTHORIZED);

    }

}
