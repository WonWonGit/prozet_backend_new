package com.example.prozet.module.stack.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class StackCategoryRepositoryTest {

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    public void stackCategorySave() {

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectKey("projectKey")
                .build();

        ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

        StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                .category("BACK END")
                .stackType(StackType.DEFAULTSTACK)
                .projectEntity(null)
                .build();

        StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

        StackEntity stackEntity = StackEntity.builder()
                .name("spring")
                .projectEntity(null)
                .stackCategory(stackCategoryEntityPS)
                .stackType(StackType.DEFAULTSTACK)
                .build();

        stackRepository.save(stackEntity);

        System.out.println(stackCategoryRepository.findAll() + "$$$$");

    }

    @Test
    public void findStackCategoryByIdxTest() {
        Optional<StackCategoryEntity> stackCategoryEntity = stackCategoryRepository.findByIdx(1);

        if (stackCategoryEntity.isPresent()) {
            assertThat(stackCategoryEntity.get().getCategory()).isEqualTo("BACK END");
        } else {
            assertThat(stackCategoryEntity).isEmpty();
        }

    }

    @Test
    public void getStackCategory() {

        List<StackCategoryFindResDTO> categoryList = stackCategoryRepository
                .getStackCategory("projectKey");

        assertThat(categoryList.get(0).getCategory()).isEqualTo("BACK END");
    }

}
