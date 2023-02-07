package com.example.prozet.module.projectMember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.email.service.EmailService;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberFindResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberServiceTest {

        @InjectMocks
        ProjectMemberService projectMemberService;

        @Mock
        EmailService emailService;

        @Mock
        ProjectRepository projectRepository;

        @Mock
        ProjectMemberRepository projectMemberRepository;

        @Mock
        MemberRepository memberRepository;

        @Test
        public void saveProjectMemberTest() {

                MemberEntity owner = MemberEntity.builder().username("owner").build();

                ProjectEntity projectEntity = ProjectEntity.builder().deleteYn("N").projectKey("projectKey123")
                                .owner(owner)
                                .build();

                MemberEntity inviteMember = MemberEntity.builder().username("inviteMember").build();

                ProjectMemberReqDTO projectMemberReqDTO = ProjectMemberReqDTO.builder().access(AccessType.READONLY)
                                .projectKey("projectKey123").username("inviteMember").build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .access(AccessType.READONLY)
                                .deleteYn("N")
                                .state(StateType.PENDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectRepository.findByProjectKeyAndDeleteYn(any(), any()))
                                .thenReturn(Optional.of(projectEntity));
                when(projectMemberRepository.getInvitedMember(any(), any())).thenReturn(null);
                when(memberRepository.findByUsername(any())).thenReturn(Optional.of(inviteMember));
                when(projectMemberRepository.save(any())).thenReturn(projectMemberEntity);

                ProjectMemberResDTO projectMemberList = projectMemberService.saveProjectMember(projectMemberReqDTO,
                                "owner");

                assertThat(projectMemberList.getState()).isEqualTo(StateType.PENDING);

        }

        @Test
        public void editProjectMemberStateTest() {

                MemberEntity owner = MemberEntity.builder().idx(1L).username("owner").build();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().idx(1L).build();

                ProjectEntity projectEntity = ProjectEntity.builder().idx(1L).deleteYn("N").projectKey("projectKey123")
                                .owner(owner)
                                .projectInformation(projectInfoEntity)
                                .build();

                MemberEntity inviteMember = MemberEntity.builder().idx(2L).username("inviteMember").build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .access(AccessType.READONLY)
                                .deleteYn("N")
                                .state(StateType.PENDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectMemberRepository.getInvitedMember(any(), any()))
                                .thenReturn(projectMemberEntity.toProjectMemberResDTO());

                boolean result = projectMemberService.editProjectMemberState("inviteMember", "projectKey123");

                assertThat(result).isTrue();

        }

        @Test
        public void editProjectMemberAccessTest() {

                MemberEntity owner = MemberEntity.builder().idx(1L).username("owner").build();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().idx(1L).build();

                ProjectEntity projectEntity = ProjectEntity.builder().idx(1L).deleteYn("N").projectKey("projectKey123")
                                .owner(owner)
                                .projectInformation(projectInfoEntity)
                                .build();

                MemberEntity inviteMember = MemberEntity.builder().idx(2L).username("inviteMember").build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .idx(1L)
                                .access(AccessType.READONLY)
                                .deleteYn("N")
                                .state(StateType.PENDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectMemberRepository.getProjectMember(anyLong()))
                                .thenReturn(Optional.of(projectMemberEntity.toProjectMemberResDTO()));
                ProjectMemberResDTO projectMemberResDTO = projectMemberService.editProjectMemberAccess(1L,
                                AccessType.EDIT, "owner");

                assertThat(projectMemberResDTO.getAccess()).isEqualTo(AccessType.EDIT);

        }

        @Test
        public void deleteProjectMemberTest() {

                MemberEntity owner = MemberEntity.builder().idx(1L).username("owner").build();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().idx(1L).build();

                ProjectEntity projectEntity = ProjectEntity.builder().idx(1L).deleteYn("N").projectKey("projectKey123")
                                .owner(owner)
                                .projectInformation(projectInfoEntity)
                                .build();

                MemberEntity inviteMember = MemberEntity.builder().idx(2L).username("inviteMember").build();

                ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                                .idx(1L)
                                .access(AccessType.READONLY)
                                .deleteYn("N")
                                .state(StateType.PENDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectMemberRepository.getProjectMember(anyLong()))
                                .thenReturn(Optional.of(projectMemberEntity.toProjectMemberResDTO()));

                ProjectMemberResDTO projectMemberResDTO = projectMemberService.deleteProjectMember(1L);

                assertThat(projectMemberResDTO.getDeleteYn()).isEqualTo("Y");

        }

        @Test
        public void getProjectMemberByStateTest() {

                when(projectMemberRepository.findByProjectEntity_ProjectKey(anyString()))
                        .thenReturn(getProjectMemberEntityList());

                Map<StateType, List<ProjectMemberFindResDTO>> projctResDTO = projectMemberService.getProjectMemberGruopByState("projectKey");

                System.out.println(projctResDTO);

                Set<StateType> keys = projctResDTO.keySet();

                assertThat(keys.contains(StateType.ACCEPTED)).isTrue();
                assertThat(keys.contains(StateType.PENDING)).isTrue();

        }

        public List<ProjectMemberEntity> getProjectMemberEntityList() {

                MemberEntity memberEntity1 = MemberEntity.builder()
                                .idx(1L)
                                .username("member1")
                                .build();

                ProjectMemberEntity projectMemberEntity1 = ProjectMemberEntity.builder()
                                .projectEntity(getProjectEntity())
                                .access(AccessType.EDIT)
                                .state(StateType.ACCEPTED)
                                .memberEntity(memberEntity1)
                                .deleteYn("N")
                                .build();

                MemberEntity memberEntity2 = MemberEntity.builder()
                                .idx(1L)
                                .username("member2")
                                .build();

                ProjectMemberEntity projectMemberEntity2 = ProjectMemberEntity.builder()
                                .projectEntity(getProjectEntity())
                                .access(AccessType.EDIT)
                                .state(StateType.PENDING)
                                .memberEntity(memberEntity2)
                                .deleteYn("N")
                                .build();

                List<ProjectMemberEntity> projectMemberEntities = new ArrayList<>();

                projectMemberEntities.add(projectMemberEntity1);
                projectMemberEntities.add(projectMemberEntity2);

                return projectMemberEntities;
        }

        public ProjectEntity getProjectEntity() {

                MemberEntity owner = MemberEntity.builder().idx(1L).username("owner").build();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().title("title").content("content")
                                .idx(1L).build();

                ProjectEntity projectEntity = ProjectEntity.builder().idx(1L).deleteYn("N").projectKey("projectKey123")
                                .owner(owner)
                                .projectInformation(projectInfoEntity)
                                .build();

                return projectEntity;

        }

}
