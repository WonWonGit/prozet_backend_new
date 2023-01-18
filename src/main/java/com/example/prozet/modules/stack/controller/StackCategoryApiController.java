package com.example.prozet.modules.stack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.stack.domain.dto.request.StackCategoryReqDTO;
import com.example.prozet.modules.stack.service.StackCategoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("v1/api/stackCategory")
public class StackCategoryApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StackCategoryService stackCategoryService;

    @PostMapping("/{projectKey}")
    public void saveStackCategory(
            @RequestBody String stackCategory,
            @PathVariable String projectKey,
            Authentication authentication) {

        ProjectResDTO projectResDTO = projectService.findByProjectKey(projectKey);

        if (projectResDTO != null) {

            stackCategoryService.stackCategorySave(stackCategory, projectResDTO);
        }

    }

}
