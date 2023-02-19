package com.example.prozet.modules.projectMember.controller;

import javax.validation.Valid;

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
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberEditReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/projectmember")
public class ProjectMemberApiController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> saveProjectMember(
            @RequestBody @Valid ProjectMemberReqDTO projectMemberReqDTO,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        ProjectMemberResDTO projectMemberResDTO = projectMemberService.saveProjectMember(projectMemberReqDTO,
                principalDetails.getUsername());

        if (projectMemberResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.FAIL_MEMBER_INVITE);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.SAVE_MEMBER_INVITE_SUCCESS, projectMemberResDTO);

    }

    @PutMapping(value = "/{idx}")
    public ResponseEntity<?> editProjectMemberAccess(
            @PathVariable Long idx,
            @RequestBody @Valid ProjectMemberEditReqDTO projectMemberEditReqDTO,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String username = principalDetails.getUsername();

        String projectKey = projectMemberEditReqDTO.getProjectKey();

        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        boolean owner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (!owner) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_MEMBER_UNAUTHORIZED);
        }

        ProjectMemberResDTO editedProjectMemberResDTO = projectMemberService.editProjectMemberAccess(idx,
                projectMemberEditReqDTO.getAccessType(),
                username);

        if (editedProjectMemberResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_INFO_UPDATE_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_MEMBER_ACCESS_SUCCESS, editedProjectMemberResDTO);

    }

    @PutMapping(value = "/delete/{idx}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable Long idx) {

        ProjectMemberResDTO projectMemberResDTO = projectMemberService.deleteProjectMember(idx);

        if (projectMemberResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_MEMBER_NOT_EXIST);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.DELETE_MEMBER_SUCCESS, null);
    }

}
