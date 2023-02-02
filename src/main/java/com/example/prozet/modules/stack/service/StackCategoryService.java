package com.example.prozet.modules.stack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;

@Service
@Transactional(readOnly = true)
public class StackCategoryService {

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @Transactional
    public StackCategoryResDTO saveStackCategory(String stackCategory, ProjectResDTO projectResDTO) {

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category(stackCategory)
                .projectEntity(projectResDTO.toEntity())
                .stackType(StackType.CUSTOMSTACK)
                .build();

        StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

        if (stackCategoryEntityPS == null) {
            return null;
        }

        return stackCategoryEntityPS.toStackCategoryResDTO();

    }

    public List<StackCategoryFindResDTO> getStackCategory(String projectKey) {

        List<StackCategoryFindResDTO> stackCategoryList = stackCategoryRepository.getStackCategory(projectKey);

        if (stackCategoryList.isEmpty()) {
            return null;
        }

        return stackCategoryList;

    }

}
