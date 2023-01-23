package com.example.prozet.modules.stack.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.service.StackService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/stack")
public class StackApiController {

    @Autowired
    private StackService stackService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("{projectKey}")
    public ResponseEntity<?> saveStack(
            @PathVariable String projectKey,
            @Valid @RequestPart(required = true) StackReqDTO stackReqDTO,
            @RequestPart(required = false, name = "iconImg") MultipartFile iconImg,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);
        String username = principalDetails.getUsername();

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean owner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);
        boolean memberAccessEdit = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);

        if (owner || memberAccessEdit) {
            StackResDTO stackResDTO = stackService.saveStack(stackReqDTO, iconImg);

            if (stackReqDTO == null) {
                return ErrorResponse.toResponseEntity(ErrorCode.SAVE_STACK_FAIL);
            } else {
                return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_STACK_SUCCESS, stackResDTO);
            }

        }

        return ErrorResponse.toResponseEntity(ErrorCode.STACK_UNAUTHORIZED);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStack(@PathVariable Long idx,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        StackResDTO stackResDTO = stackService.findByIdx(idx);

        if (stackResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.STACK_NOT_EXIST);
        }

        String projectKey = stackResDTO.getStackCategory().getProjectResDTO().getProjectKey();

        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        boolean projectMemberAccess = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(),
                username);
        boolean projectOwnerCheck = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (projectMemberAccess || projectOwnerCheck) {

            if (stackResDTO.getStackType().equals(StackType.CUSTOMSTACK)) {
                stackService.deleteStackService(stackResDTO);

                return ResponseDTO.toResponseEntity(ResponseEnum.DELETE_STACK_SUCCESS, null);

            }

            return ErrorResponse.toResponseEntity(ErrorCode.STACK_UNAUTHORIZED);

        }

        return ErrorResponse.toResponseEntity(ErrorCode.DELETE_STACK_FAIL);
    }

}
