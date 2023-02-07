package com.example.prozet.module.projectMember.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberListResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@DataJpaTest
@Import(QueryDSLConfigTest.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class ProjectMemberRepositoryTest {

        @Autowired
        ProjectMemberRepository projectMemberRepository;

        @Autowired
        ProjectRepository projectRepository;

        @Autowired
        MemberRepository memberRepository;

        @Autowired
        JPAQueryFactory query;

        @BeforeEach
        public void saveProjectMember() {

                MemberEntity memberEntity = MemberEntity.builder().username("member").build();
                MemberEntity memberEntityPS = memberRepository.save(memberEntity);

                MemberEntity ownerEntity = MemberEntity.builder().username("owner").build();
                MemberEntity ownerEntityPS = memberRepository.save(ownerEntity);

                ProjectEntity projectEntity = ProjectEntity.builder().owner(ownerEntityPS).projectKey("projectKey")
                                .build();
                ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .access(AccessType.READONLY)
                                .state(StateType.PENDING)
                                .memberEntity(memberEntityPS)
                                .projectEntity(projectEntityPS)
                                .deleteYn("N")
                                .build();

                ProjectMemberEntity projectMemberEntityPS = projectMemberRepository.save(projectMemberEntity);

                System.out.println(projectMemberEntityPS);

                assertThat(projectMemberEntityPS.getAccess()).isEqualTo(AccessType.READONLY);

        }

        @Test
        @Order(1)
        public void findByProjectEntity_ProjectKeyTest() {

                List<ProjectMemberEntity> projectMemberEntity = projectMemberRepository
                                .findByProjectEntity_ProjectKey("projectKey");

                assertThat(projectMemberEntity.get(0).getState()).isEqualTo(StateType.PENDING);

        }

        @Test
        @Order(2)
        public void getInvitedMemberTest() {
                ProjectMemberResDTO result = projectMemberRepository.getInvitedMember("username",
                                "projectKey");
                assertThat(result).isEqualTo(null);
        }

        @Test
        @Order(3)
        public void getProjectMemberTest() {
                Optional<ProjectMemberResDTO> result = projectMemberRepository.getProjectMember(3L);

                if (result.isPresent()) {
                        assertThat(result.get().getState()).isEqualTo(StateType.PENDING);
                }
        }

        @Test
        public void getEditProjectMemberListTest() {

                List<ProjectMemberListResDTO> result = projectMemberRepository.getEditProjectMemberList("projectKey");
                assertThat(result).isNull();
        }

}
