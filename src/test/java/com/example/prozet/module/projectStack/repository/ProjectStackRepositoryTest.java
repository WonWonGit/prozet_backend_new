package com.example.prozet.module.projectStack.repository;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.repository.ProjectStackRepository;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
@TestMethodOrder(OrderAnnotation.class)
public class ProjectStackRepositoryTest {

        @Autowired
        private ProjectStackRepository projectStackRepository;

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private StackRepository stackRepository;

        @Autowired
        private StackCategoryRepository stackCategoryRepository;

        @Autowired
        private ProjectRepository projectRepository;

        @BeforeEach
        public void projectStackSaveTest() {

                ProjectEntity projectEntity = getProjectEntity();

                ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

                StackCategoryEntity stackCategoryEntity = getStackCategoryEntity();

                StackCategoryEntity stackCategoryEntityPS = stackCategoryRepository.save(stackCategoryEntity);

                StackEntity stackEntity = getStackEntity(stackCategoryEntityPS);

                StackEntity stackEntityPS = stackRepository.save(stackEntity);

                ProjectStackEntity projectStackEntity = getProjectStackEntity(stackEntityPS, projectEntityPS);

                projectStackRepository.save(projectStackEntity);

        }

        @Test
        @Order(1)
        public void findByStackEntityIdxTest() {

                ProjectStackEntity projectStackEntity = projectStackRepository
                                .findByStackEntity_IdxAndProjectEntity_ProjectKey(1L, "projectKey")
                                .orElseThrow(() -> new IllegalArgumentException());

                assertThat(projectStackEntity.getStackEntity().getName()).isEqualTo("spring");
        }

        // ********** Create Model ***********/
        public ProjectEntity getProjectEntity() {

                MemberEntity memberEntity = MemberEntity.builder()
                                .username("username")
                                .name("name")
                                .email("email")
                                .build();

                MemberEntity memberEntityPS = memberRepository.save(memberEntity);

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .projectInformation(null)
                                .owner(memberEntityPS)
                                .build();

                return projectEntity;
        }

        public StackCategoryEntity getStackCategoryEntity() {
                StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder().category("back end")
                                .stackType(StackType.DEFAULTSTACK).build();

                return stackCategoryEntity;
        }

        public StackEntity getStackEntity(StackCategoryEntity stackCategoryEntity) {

                StackEntity stackEntity = StackEntity.builder().name("spring")
                                .stackCategory(stackCategoryEntity)
                                .stackType(StackType.DEFAULTSTACK).build();

                return stackEntity;
        }

        public ProjectStackEntity getProjectStackEntity(StackEntity stackEntity, ProjectEntity projectEntity) {

                ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                                .projectEntity(projectEntity)
                                .stackEntity(stackEntity)
                                .checkedYn("Y")
                                .build();

                return projectStackEntity;
        }

}
