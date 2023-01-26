package com.example.prozet.modules.projectStack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
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
    public List<ProjectStackResDTO> saveProjectStack(List<Long> stackIdxList, ProjectResDTO projectResDTO) {

        List<ProjectStackResDTO> projectStackResDTOList = new ArrayList<ProjectStackResDTO>();

        stackIdxList.forEach(stackIdx -> {
            Optional<StackEntity> stackEntity = stackRepository.findByIdx(stackIdx);
            if (stackEntity.isPresent()) {
                ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                        .projectEntity(projectResDTO.toEntity())
                        .stackEntity(stackEntity.get()).checkedYn("Y").build();
                ProjectStackEntity projectStackEntityPS = projectStackRepository.save(projectStackEntity);
                projectStackResDTOList.add(projectStackEntityPS.toProjectStackResDTO());
            }
        });

        return projectStackResDTOList;

    }

}
