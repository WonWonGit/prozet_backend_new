package com.example.prozet.modules.project.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.service.ProjectInfoService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/project")
public class ProjectApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping
    public ResponseEntity<?> createProject(Authentication authentication,
            @Valid @RequestPart(name = "projectInfo") ProjectInfoReqDTO projectInfoReqDTO,
            @RequestPart(name = "projectImg", required = false) MultipartFile projectImg) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        MemberEntity memberEntity = principalDetails.getMember();

        ProjectResDTO projectResDTO = projectService.createProject(memberEntity, projectInfoReqDTO, projectImg);

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.CREATE_PROJECT_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.CREATE_PROJECT_SUCCESS, projectResDTO);

    }

    @PutMapping("/{projectKey}")
    public ResponseEntity<?> deleteProject(
            @PathVariable String projectKey,
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        projectService.deleteProject(projectKey, principalDetails.getUsername());

        return ResponseDTO.toResponseEntity(ResponseEnum.DELETE_PROJECT_SUCCESS, null);
    }

}
