package com.example.prozet.module.projectMember.repository;

import static com.example.prozet.modules.member.domain.entity.QMemberEntity.memberEntity;
import static com.example.prozet.modules.projectMember.domain.entity.QProjectMemberEntity.projectMemberEntity;
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

        MemberEntity memberEntity = MemberEntity.builder().build();
        MemberEntity memberEntityPS = memberRepository.save(memberEntity);

        ProjectEntity projectEntity = ProjectEntity.builder().projectKey("projecKey").build();
        ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                .access(AccessType.READONLY)
                .state(StateType.PANDING)
                .memberEntity(memberEntityPS)
                .projectEntity(projectEntityPS)
                .deleteYn("N")
                .build();

        ProjectMemberEntity projectMemberEntityPS = projectMemberRepository.save(projectMemberEntity);

        assertThat(projectMemberEntityPS.getAccess()).isEqualTo(AccessType.READONLY);

    }

    @Test
    @Order(1)
    public void findByProjectEntity_ProjectKeyTest() {

        Optional<ProjectMemberEntity> projectMemberEntity = projectMemberRepository
                .findByProjectEntity_ProjectKey("projectKey");

        if (projectMemberEntity.isPresent()) {
            assertThat(projectMemberEntity.get().getState()).isEqualTo(StateType.PANDING);
        }

    }

    @Test
    @Order(2)
    public void getInvitedMemberTest() {
        ProjectMemberEntity result = query.selectFrom(projectMemberEntity)
                .where(memberEntity.username.eq("username"),
                        projectMemberEntity.projectEntity.projectKey.eq("projectKey"),
                        projectMemberEntity.deleteYn.eq("N"))
                .fetchFirst();

        assertThat(result).isEqualTo(null);
    }

    @Test
    @Order(3)
    public void getProjectMemberTest() {
        ProjectMemberEntity result = query.selectFrom(projectMemberEntity)
                .where(memberEntity.idx.eq(3L),
                        projectMemberEntity.deleteYn.eq("N"))
                .fetchFirst();

        assertThat(result.getState()).isEqualTo(StateType.PANDING);
    }

    @Test
    public void getEditProjectMemberListTest() {

        List<ProjectMemberListResDTO> result = query
                .select(Projections.constructor(ProjectMemberListResDTO.class,
                        projectMemberEntity.idx,
                        projectMemberEntity.access,
                        projectMemberEntity.memberEntity.username))
                .where(projectMemberEntity.deleteYn.eq("N"),
                        projectMemberEntity.state.eq(StateType.ACCEPTED),
                        projectMemberEntity.access.eq(AccessType.EDIT))
                .from(projectMemberEntity)
                .fetch();

        assertThat(result).isEmpty();
    }

}
