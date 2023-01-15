package com.example.prozet.modules.stack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

@Service
@Transactional(readOnly = true)
public class StackService {

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @Transactional
    public void saveStack(StackReqDTO stackReqDTO, MultipartFile iconImg, String projectKey, String username) {

        ProjectEntity projectEntity = projectRepository.findByProjectKey(projectKey)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_EXIST));

        StackCategoryEntity stackCategoryEntity = stackCategoryRepository.findByIdx(stackReqDTO.getStackCategoryIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.STACK_CATEGORY_NOT_EXIST));

        if (iconImg != null) {

        } else {
            StackEntity stackEntity = stackReqDTO.toEntity(stackReqDTO.getIconUrl(), stackCategoryEntity);
            StackEntity stackEntityPS = stackRepository.save(stackEntity);
        }

    }

}
