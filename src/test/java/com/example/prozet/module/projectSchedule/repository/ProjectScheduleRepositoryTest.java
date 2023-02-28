package com.example.prozet.module.projectSchedule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.schedule.repository.ScheduleRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
@TestMethodOrder(OrderAnnotation.class)
public class ProjectScheduleRepositoryTest {

    @Autowired
    private ProjectScheduleRepository projectScheduleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    public void saveProjectScheduleTest() {

        ProjectEntity projectEntity = getProjectEntity();
        ProjectMemberEntity projectMemberEntity = getProjectMemberEntity();
        ProjectMemberEntity projectMemberEntity2 = getProjectMemberEntity2();
        ScheduleEntity scheduleEntity = getShcheduleEntity();

        ProjectScheduleEntity projectScheduleEntity = ProjectScheduleEntity.builder()
                .projectEntity(projectEntity)
                .projectMemberEntity(projectMemberEntity)
                .scheduleEntity(scheduleEntity)
                .scheduleType(ScheduleType.COMPLETED)
                .build();

        ProjectScheduleEntity projectScheduleEntity2 = ProjectScheduleEntity.builder()
                .projectEntity(projectEntity)
                .projectMemberEntity(projectMemberEntity2)
                .scheduleEntity(scheduleEntity)
                .scheduleType(ScheduleType.COMPLETED)
                .build();

        ProjectScheduleEntity projectScheduleEntityPS = projectScheduleRepository.save(projectScheduleEntity);
        ProjectScheduleEntity projectScheduleEntityPS2 = projectScheduleRepository.save(projectScheduleEntity2);

        assertThat(projectScheduleEntityPS.getProjectMemberEntity().getProjectMemberType())
                .isEqualTo(ProjectMemberType.OWNER);
        assertThat(projectScheduleEntityPS2.getProjectMemberEntity().getProjectMemberType())
                .isEqualTo(ProjectMemberType.TEAMMEMBER);
    }

    @Test
    @Order(1)
    public void findByIdxTest() {

        ProjectScheduleEntity projectScheduleEntityPS = projectScheduleRepository
                .findByIdx(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        assertThat(projectScheduleEntityPS.getScheduleEntity().getContent()).isEqualTo("content");

    }

    @Test
    @Order(2)
    public void findByScheduleEntityIdx() {

        List<ProjectScheduleEntity> projectScheduleEntityPS = projectScheduleRepository
                .findByScheduleEntity_Idx(2L);

        assertThat(projectScheduleEntityPS.size()).isEqualTo(2);

    }

    @Test
    @Order(3)
    public void findByScheduleEntityIdxAndProjectMemberEntityIdxTest() {

        ProjectScheduleEntity projectScheduleEntityPS = projectScheduleRepository
                .findByScheduleEntity_IdxAndProjectMemberEntity_Idx(3L, 5L)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_SCHEDULE_NOT_EXIST));

        assertThat(projectScheduleEntityPS.getScheduleEntity().getContent()).isEqualTo("content");

    }

    /********************* DATA ******************** */
    public ScheduleEntity getShcheduleEntity() {

        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .content("content")
                .title("todo")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        return scheduleRepository.save(scheduleEntity);

    }

    public ProjectMemberEntity getProjectMemberEntity() {

        MemberEntity owner = MemberEntity.builder()
                .username("google_1212")
                .name("name")
                .email("test@google.com")
                .provider(Provider.GOOGLE)
                .displayName("test")
                .build();

        MemberEntity ownerPS = memberRepository.save(owner);

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                .access(AccessType.EDIT)
                .projectEntity(getProjectEntity())
                .projectMemberType(ProjectMemberType.OWNER)
                .memberEntity(ownerPS)
                .build();

        return projectMemberRepository.save(projectMemberEntity);
    }

    public ProjectMemberEntity getProjectMemberEntity2() {

        MemberEntity member = MemberEntity.builder()
                .username("google_13333")
                .name("name")
                .email("test@google.com")
                .provider(Provider.GOOGLE)
                .displayName("test")
                .build();

        MemberEntity memberPS = memberRepository.save(member);

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                .access(AccessType.EDIT)
                .projectEntity(getProjectEntity())
                .projectMemberType(ProjectMemberType.TEAMMEMBER)
                .memberEntity(memberPS)
                .build();

        return projectMemberRepository.save(projectMemberEntity);
    }

    public ProjectEntity getProjectEntity() {

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectKey("projectKey")
                .projectInformation(null)
                .createDate(LocalDateTime.now())
                .deleteYn("N")
                .deleteDate(null)
                .build();

        return projectRepository.save(projectEntity);

    }

}
