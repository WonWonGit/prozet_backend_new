package com.example.prozet.module.stack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.service.StackCategoryService;

@ExtendWith(MockitoExtension.class)
public class StackCategoryServiceTest {

    @InjectMocks
    private StackCategoryService stackCategoryService;

    @Mock
    private StackCategoryRepository stackCategoryRepository;

    public ProjectEntity getProjectEntity() {

        MemberEntity owner = MemberEntity.builder().username("name").role(Role.USER).build();

        ProjectInfoEntity projectInfo = ProjectInfoEntity.builder().title("title").build();

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectKey("projectKey")
                .projectInformation(projectInfo)
                .owner(owner)
                .build();

        return projectEntity;
    }

    @Test
    public void stackCategorySaveTest() {

        ProjectEntity projectEntity = getProjectEntity();

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("backend")
                .stackType(StackType.CUSTOMSTACK)
                .projectEntity(projectEntity)
                .build();

        when(stackCategoryRepository.save(any())).thenReturn(stackCategoryEntity);

        StackCategoryResDTO stackCategoryResDTO = stackCategoryService.stackCategorySave("stack Category",
                projectEntity.toProjectResDTO());

        assertThat(stackCategoryResDTO.getCategory()).isEqualTo("backend");

    }

}
