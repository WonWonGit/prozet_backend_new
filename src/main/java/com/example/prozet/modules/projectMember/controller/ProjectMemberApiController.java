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
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/project/member")
public class ProjectMemberApiController {

    @Autowired
    private ProjectMemberService projectMemberService;

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
            AccessType accessType,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        ProjectMemberResDTO projectMemberResDTO = projectMemberService.editProjectMemberAccess(idx, accessType,
                principalDetails.getUsername());

        if (projectMemberResDTO != null) {

            return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_MEMBER_ACCESS_SUCCESS, projectMemberResDTO);
        }

        return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_INFO_UPDATE_FAIL);
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
