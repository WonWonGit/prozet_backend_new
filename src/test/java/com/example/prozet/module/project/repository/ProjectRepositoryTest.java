package com.example.prozet.module.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
public class ProjectRepositoryTest {

        @Autowired
        ProjectRepository projectRepository;

        @Autowired
        ProjectMemberRepository projectMemberRepository;

        @Autowired
        ProjectInfoRepository projectInfoRepository;

        @Autowired
        MemberRepository memberRepository;

        @Autowired
        JPAQueryFactory query;

        @BeforeEach
        public void projectSaveTest() {
                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder()
                                .title("title")
                                .content("content")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                ProjectInfoEntity projectInfoEntityPS = projectInfoRepository.save(projectInfoEntity);

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .deleteYn("N")
                                .projectInformation(projectInfoEntityPS)
                                .projectKey("projectKey").build();

                ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .projectEntity(projectEntityPS)
                                .memberEntity(getMemberEntity())
                                .projectMemberType(ProjectMemberType.OWNER)
                                .access(AccessType.EDIT)
                                .state(StateType.ACCEPTED)
                                .deleteYn("N")
                                .build();

                projectMemberRepository.save(projectMemberEntity);

                assertThat(projectEntityPS.getProjectKey()).isEqualTo("projectKey");
        }

        @Test
        public void findProjectListTest() {

                List<ProjectListDTO> results = projectRepository.getProjectList("username");

                assertThat(results.get(0).getTitle()).isEqualTo("title");

        }

        @Test
        public void getProjectByProjectKeyTest() {

                ProjectResDTO projectResDTO = projectRepository.getProjectByProjectKey("projectKey");

                System.out.println(projectResDTO + "####");
        }

        private MemberEntity getMemberEntity() {
                MemberEntity memberEntity = MemberEntity.builder()
                                .idx(1L)
                                .username("username")
                                .email("test@gmail.com")
                                .provider(Provider.GOOGLE)
                                .displayName("name")
                                .name("name")
                                .role(Role.USER)
                                .deleteYn("N")
                                .build();

                return memberRepository.save(memberEntity);
        }

}
