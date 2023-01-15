package com.example.prozet.modules.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.prozet.common.ResponseDTO;
import com.example.prozet.common.ResponseEnum;
import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.security.auth.PrincipalDetails;

@Controller
@RequestMapping("v1/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getProjectList(
            Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        List<ProjectListDTO> projectResDTO = projectService.getProjectList(principalDetails.getUsername());

        return ResponseDTO.toResponseEntity(ResponseEnum.FIND_PROJECT_LIST, projectResDTO);

    }

    @GetMapping("/{projectKey}")
    public ResponseEntity<?> getProject() {
        return null;
    }

}
