package com.example.prozet.modules.projectInformation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.service.ProjectInfoService;
import com.example.prozet.security.auth.PrincipalDetails;

@RestController
@RequestMapping("v1/api/project")
public class ProjectInformationApiController {

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private ProjectService projectService;

    @PutMapping("/information/{projectKey}")
    public ResponseEntity<?> updateProjectInfo(
            @PathVariable String projectKey,
            Authentication authentication,
            @Valid @RequestPart(name = "projectInfo") ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO,
            @RequestPart(name = "projectImg", required = false) MultipartFile projectImg) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String username = principalDetails.getUsername();

        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        if (projectResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_NOT_EXIST);
        }

        boolean editMember = ProjectUtil.projectMemberAccessEditCheck(projectResDTO.getProjectMemberResDTO(), username);
        boolean owner = ProjectUtil.projectOwnerCheck(projectResDTO.getOwner(), username);

        if (!editMember && !owner) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_INFO_UPDATE_UNAUTHORIZED);
        }

        ProjectInfoResDTO projectInfoResDTO = projectInfoService.updateProjectInfo(projectKey,
                principalDetails.getUsername(), projectInfoUpdateReqDTO, projectImg);

        if (projectInfoResDTO == null) {
            return ErrorResponse.toResponseEntity(ErrorCode.PROJECT_INFO_UPDATE_FAIL);
        }

        return ResponseDTO.toResponseEntity(ResponseEnum.UPDATE_PROJECT_INFO_SUCCESS, projectInfoResDTO);
    }

}
