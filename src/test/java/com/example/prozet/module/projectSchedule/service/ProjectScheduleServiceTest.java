package com.example.prozet.module.projectSchedule.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectSchedule.domain.dto.request.ProjectScheduleSaveReqDTO;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectSchedule.repository.ProjectScheduleRepository;
import com.example.prozet.modules.projectSchedule.service.ProjectScheduleService;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleReqDTO;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

@ExtendWith(MockitoExtension.class)
public class ProjectScheduleServiceTest {

        @InjectMocks
        private ProjectScheduleService projectScheduleService;

        @Mock
        private ProjectScheduleRepository projectScheduleRepository;

        @Test
        public void saveProjectScheduleTest() {

                ProjectScheduleEntity projectScheduleEntity = getPublicScheduleEntity();

                ScheduleReqDTO scheduleReqDTO = ScheduleReqDTO.builder()
                                .content("content")
                                .title("title")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                List<Long> projectMemberIdx = new ArrayList<>();
                projectMemberIdx.add(1L);
                projectMemberIdx.add(2L);

                ProjectScheduleSaveReqDTO projectScheduleSaveReqDTO = ProjectScheduleSaveReqDTO.builder()
                                .projectKey("projectKey")
                                .scheduleReqDTO(scheduleReqDTO)
                                .projectMemberIdx(projectMemberIdx)
                                .scheduleType(ScheduleType.OPEN)
                                .build();

                when(projectScheduleRepository.save(any())).thenReturn(projectScheduleEntity);

                ProjectResDTO projectResDTO = ProjectResDTO.builder()
                                .idx(1L)
                                .projectKey("projectKey")
                                .deleteYn("N")
                                .build();

                ProjectScheduleResDTO projectScheduleResDTO = projectScheduleService.saveProjectSchedule(
                                projectResDTO,
                                projectScheduleSaveReqDTO,
                                projectScheduleEntity.getProjectMemberEntity().toProjectMemberResDTO());

                assertThat(projectScheduleResDTO).isNotNull();

        }

        @Test
        public void editProjectScheduleTypeTest() {

                List<ProjectScheduleEntity> projectScheduleEntities = new ArrayList<>();
                projectScheduleEntities.add(getPublicScheduleEntity());
                projectScheduleEntities.add(getPublicScheduleEntity2());

                when(projectScheduleRepository.findByScheduleEntity_Idx(anyLong())).thenReturn(projectScheduleEntities);

                Map<Long, List<ProjectScheduleResDTO>> projectSchduleResDTO = projectScheduleService
                                .editProjectScheduleType(1L, ScheduleType.INPROGRESS);

                System.out.println(projectSchduleResDTO + "$$$$");

        }

        public ProjectScheduleEntity getPublicScheduleEntity() {

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .deleteYn("N")
                                .build();

                MemberEntity owner = MemberEntity.builder()
                                .username("google_1212")
                                .name("name")
                                .email("test@google.com")
                                .provider(Provider.GOOGLE)
                                .displayName("test")
                                .build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .access(AccessType.EDIT)
                                .projectEntity(projectEntity)
                                .projectMemberType(ProjectMemberType.OWNER)
                                .memberEntity(owner)
                                .build();

                ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                                .idx(1L)
                                .content("content")
                                .title("title")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                ProjectScheduleEntity projectScheduleEntity = ProjectScheduleEntity.builder()
                                .scheduleType(ScheduleType.OPEN)
                                .projectMemberEntity(projectMemberEntity)
                                .projectEntity(projectEntity)
                                .scheduleEntity(scheduleEntity)
                                .build();

                return projectScheduleEntity;

        }

        public ProjectScheduleEntity getPublicScheduleEntity2() {

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .deleteYn("N")
                                .build();

                MemberEntity member = MemberEntity.builder()
                                .username("google_121212")
                                .name("name")
                                .email("test@google.com")
                                .provider(Provider.GOOGLE)
                                .displayName("test")
                                .build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .access(AccessType.EDIT)
                                .projectEntity(projectEntity)
                                .projectMemberType(ProjectMemberType.TEAMMEMBER)
                                .memberEntity(member)
                                .build();

                ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                                .idx(1L)
                                .content("content")
                                .title("title")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                ProjectScheduleEntity projectScheduleEntity = ProjectScheduleEntity.builder()
                                .scheduleType(ScheduleType.OPEN)
                                .projectMemberEntity(projectMemberEntity)
                                .projectEntity(projectEntity)
                                .scheduleEntity(scheduleEntity)
                                .build();

                return projectScheduleEntity;

        }

}
