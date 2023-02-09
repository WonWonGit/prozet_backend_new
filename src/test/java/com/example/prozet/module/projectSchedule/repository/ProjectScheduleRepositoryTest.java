package com.example.prozet.module.projectSchedule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.schedule.repository.ScheduleRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class ProjectScheduleRepositoryTest {

    @Autowired
    private ProjectScheduleRepository projectScheduleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveProjectScheduleTest() {

        ProjectScheduleEntity projectScheduleEntity = ProjectScheduleEntity.builder()
                .projectEntity(getProjectEntity())
                .projectMemberEntity(null)
                .scheduleEntity(getShcheduleEntity())
                .scheduleType(ScheduleType.COMPLETED)
                .build();

        ProjectScheduleEntity projectScheduleEntityPS = projectScheduleRepository.save(projectScheduleEntity);

        assertThat(projectScheduleEntityPS.getScheduleEntity()).isNotNull();

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

    public ProjectEntity getProjectEntity() {

        MemberEntity memberEntity = MemberEntity.builder()
                .name("name")
                .displayName("suwon")
                .deleteYn("N")
                .email("test@gmail.com")
                .provider(Provider.GOOGLE)
                .build();

        MemberEntity memberEntityPS = memberRepository.save(memberEntity);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectKey("projectKey")
                .projectInformation(null)
                .createDate(LocalDateTime.now())
                .deleteYn("N")
                .deleteDate(null)
                .owner(memberEntityPS)
                .build();

        return projectRepository.save(projectEntity);

    }

}
