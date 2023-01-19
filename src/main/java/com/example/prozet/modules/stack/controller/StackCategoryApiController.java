package com.example.prozet.modules.stack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.stack.domain.dto.request.StackCategoryReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.service.StackCategoryService;
import com.example.prozet.security.auth.PrincipalDetails;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("v1/api/stackCategory")
public class StackCategoryApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StackCategoryService stackCategoryService;

    @PostMapping("/{projectKey}")
    public ResponseEntity<?> saveStackCategory(
            @RequestBody String stackCategory,
            @PathVariable String projectKey,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);
        String username = principalDetails.getUsername();

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean projectMemberAccess = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);
        boolean projectOwnerCheck = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (projectMemberAccess || projectOwnerCheck) {

            StackCategoryResDTO stackCategoryResDTO = stackCategoryService.stackCategorySave(stackCategory,
                    projectResDTO);

            if (stackCategory != null) {

                return ErrorResponse.toResponseEntity(ErrorCode.SAVE_STACK_CATEGORY_FAIL);

            } else {
                return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_STACK_CATEGORY_SUCCESS, stackCategoryResDTO);
            }

        } else {

            return ErrorResponse.toResponseEntity(ErrorCode.SAVE_STACK_CATEGORY_UNAUTHORIZED);

        }

    }

}
