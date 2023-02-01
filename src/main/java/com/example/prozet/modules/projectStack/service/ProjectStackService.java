package com.example.prozet.modules.projectStack.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackUnmappedResDTO;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.repository.ProjectStackRepository;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackRepository;

@Service
@Transactional(readOnly = true)
public class ProjectStackService {

    @Autowired
    private ProjectStackRepository projectStackRepository;

    @Autowired
    private StackRepository stackRepository;

    @Transactional
    public ProjectStackUnmappedResDTO saveProjectStack(Long stackIdx, ProjectResDTO projectResDTO) {

        StackEntity stackEntity = stackRepository.findByIdx(stackIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.STACK_NOT_EXIST));

        ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                .projectEntity(projectResDTO.toEntity())
                .stackEntity(stackEntity)
                .checkedYn("Y").build();

        ProjectStackEntity projectStackEntityPS = projectStackRepository.save(projectStackEntity);

        return projectStackEntityPS.toProjectStackUnmmapedResDTO();

    }

    @Transactional
    public ProjectStackUnmappedResDTO editProjectStack(Long projctStackIdx) {

        ProjectStackEntity projectStackEntity = projectStackRepository.findByIdx(projctStackIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_STACK_NOT_EXIST));

        projectStackEntity.editCheckedYn(projectStackEntity.getCheckedYn());

        return projectStackEntity.toProjectStackUnmmapedResDTO();

    }

    public ProjectStackResDTO findProjectStack(Long stackIdx, String projectKey) {

        Optional<ProjectStackEntity> projectStackEntity = projectStackRepository
                .findByStackEntity_IdxAndProjectEntity_ProjectKey(stackIdx, projectKey);

        if (projectStackEntity.isPresent()) {
            return projectStackEntity.get().toProjectStackResDTO();
        } else {
            return null;
        }

    }

}
