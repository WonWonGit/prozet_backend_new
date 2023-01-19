package com.example.prozet.modules.stack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;

@Service
public class StackCategoryService {

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    public StackCategoryResDTO stackCategorySave(String stackCategory, ProjectResDTO projectResDTO) {

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

}
