package com.example.prozet.module.projectMember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
                                .state(StateType.PANDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectRepository.findByProjectKey(any())).thenReturn(Optional.of(projectEntity));
                when(projectMemberRepository.getInvitedMember(any(), any())).thenReturn(Optional.empty());
                when(memberRepository.findByUsername(any())).thenReturn(Optional.of(inviteMember));
                when(projectMemberRepository.save(any())).thenReturn(projectMemberEntity);

                ProjectMemberResDTO projectMemberList = projectMemberService.saveProjectMember(projectMemberReqDTO,
                                "owner");

                assertThat(projectMemberList.getState()).isEqualTo(StateType.PANDING);

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
                                .state(StateType.PANDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectMemberRepository.getInvitedMember(any(), any()))
                                .thenReturn(Optional.of(projectMemberEntity.toProjectMemberResDTO()));

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
                                .state(StateType.PANDING)
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
                                .state(StateType.PANDING)
                                .memberEntity(inviteMember)
                                .projectEntity(projectEntity)
                                .build();

                when(projectMemberRepository.getProjectMember(anyLong()))
                                .thenReturn(Optional.of(projectMemberEntity.toProjectMemberResDTO()));

                ProjectMemberResDTO projectMemberResDTO = projectMemberService.deleteProjectMember(1L);

                assertThat(projectMemberResDTO.getDeleteYn()).isEqualTo("Y");

        }

}
