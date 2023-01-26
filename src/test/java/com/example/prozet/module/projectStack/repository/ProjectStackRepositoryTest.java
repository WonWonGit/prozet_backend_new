package com.example.prozet.module.projectStack.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.repository.ProjectStackRepository;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class ProjectStackRepositoryTest {

        @Autowired
        private ProjectStackRepository projectStackRepository;

        @Autowired
        private StackRepository stackRepository;

        @Autowired
        private StackCategoryRepository stackCategoryRepository;

        @Autowired
        private ProjectRepository projectRepository;

        @Test
        public void projectStackSaveTest() {

                ProjectEntity projectEntity = ProjectEntity.builder().projectKey("projectKey").build();

                ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

                StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                                .category("BACK END")
                                .stackType(StackType.DEFAULTSTACK)
                                .projectEntity(null)
                                .build();

                StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

                StackEntity stackEntity = StackEntity.builder()
                                .icon("iconUrl")
                                .name("spring")
                                .stackType(StackType.CUSTOMSTACK)
                                .projectEntity(projectEntityPS)
                                .stackCategory(stackCategoryEntityPS)
                                .build();

                StackEntity stackEntityPS = stackRepository.save(stackEntity);

                ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                                .projectEntity(projectEntity)
                                .checkedYn("Y")
                                .stackEntity(stackEntityPS).build();

                projectStackRepository.save(projectStackEntity);

        }

}
