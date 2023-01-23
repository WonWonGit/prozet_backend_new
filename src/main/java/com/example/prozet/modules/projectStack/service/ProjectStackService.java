package com.example.prozet.modules.projectStack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.modules.projectStack.domain.dto.response.ProjectStackResDTO;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.repository.ProjectStackRepository;

@Service
@Transactional(readOnly = true)
public class ProjectStackService {

    @Autowired
    private ProjectStackRepository projectStackRepository;

    @Transactional
    public List<ProjectStackResDTO> saveProjectStack(List<Long> projectStackIdxList) {

        List<ProjectStackResDTO> projectStackResDTOList = new ArrayList<ProjectStackResDTO>();

        projectStackIdxList.forEach(stackIdx -> {
            Optional<ProjectStackEntity> projecstStackEntity = projectStackRepository.findByIdx(stackIdx);
            if (projecstStackEntity.isPresent()) {
                ProjectStackEntity projectStackEntityPS = projectStackRepository.save(projecstStackEntity.get());
                projectStackResDTOList.add(projectStackEntityPS.toProjectStackResDTO());
            }
        });

        return projectStackResDTOList;

    }

}
