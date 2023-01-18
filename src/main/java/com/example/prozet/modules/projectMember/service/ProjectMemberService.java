package com.example.prozet.modules.projectMember.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.email.service.EmailService;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberEditReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;

@Service
@Transactional(readOnly = true)
public class ProjectMemberService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ProjectMemberResDTO saveProjectMember(ProjectMemberReqDTO projectMemberReqDTO, String owner) {

        ProjectEntity projectEntity = projectRepository
                .findByProjectKeyAndDeleteYn(projectMemberReqDTO.getProjectKey(), "N")
                .orElseThrow(() -> new CustomException(ErrorCode.FIND_PROJECT_INFO_FAIL));

        if (!projectEntity.getOwner().getUsername().equals(owner)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_ONLY);
        }

        ProjectMemberResDTO invitedMember = projectMemberRepository
                .getInvitedMember(projectMemberReqDTO.getProjectKey(), projectMemberReqDTO.getUsername());

        // member alredy invited
        if (invitedMember != null) {
            return invitedMember;
        }

        MemberEntity memberEntity = memberRepository.findByUsername(projectMemberReqDTO.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                .access(projectMemberReqDTO.getAccess())
                .memberEntity(memberEntity)
                .projectEntity(projectEntity)
                .deleteYn("N")
                .state(StateType.PANDING)
                .build();

        ProjectMemberEntity projectMemberEntityPS = projectMemberRepository.save(projectMemberEntity);

        if (projectMemberEntityPS != null) {
            emailService.sendMail(memberEntity.getEmail(), memberEntity.getUsername(),
                    projectEntity.getOwner().getEmail(), projectMemberReqDTO.getProjectKey());

            return projectMemberEntityPS.toProjectMemberResDTO();
        }

        return null;
    }

    public Map<String, Object> getProjectMember(String projectKey) {

        Map<String, Object> member = new HashMap<String, Object>();
        ArrayList<ProjectMemberResDTO> pendingList = new ArrayList<ProjectMemberResDTO>();
        ArrayList<ProjectMemberResDTO> acceptedList = new ArrayList<ProjectMemberResDTO>();

        projectMemberRepository.findByProjectEntity_ProjectKey(projectKey)
                .filter(project -> project.getDeleteYn().equals("N"))
                .ifPresent(project -> {
                    if (project.getState().equals(StateType.PANDING)) {
                        pendingList.add(project.toProjectMemberResDTO());
                    } else {
                        acceptedList.add(project.toProjectMemberResDTO());
                    }
                });

        member.put(StateType.ACCEPTED.getState(), acceptedList);
        member.put(StateType.PANDING.getState(), pendingList);
        return member;
    }

    @Transactional
    public boolean editProjectMemberState(String username, String projectKey) {

        ProjectMemberResDTO invitedMember = projectMemberRepository
                .getInvitedMember(projectKey, username);

        if (invitedMember == null) {
            return false;
        }

        invitedMember.toEntity().inviteAccept();
        return true;
    }

    @Transactional
    public ProjectMemberResDTO editProjectMemberAccess(Long idx,
            AccessType accessType,
            String username) {

        ProjectMemberResDTO memberResDTO = projectMemberRepository.getProjectMember(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_MEMBER_NOT_EXIST));

        if (!memberResDTO.getProjectResDTO().getOwner().getUsername().equals(username)) {
            throw new CustomException(ErrorCode.PROJECT_OWNER_ONLY);
        }

        ProjectMemberEntity projectMemberEntity = memberResDTO.toEntity();
        projectMemberEntity.editMemberAccess(accessType);

        return projectMemberEntity.toProjectMemberResDTO();
    }

    @Transactional
    public ProjectMemberResDTO deleteProjectMember(Long idx) {
        Optional<ProjectMemberResDTO> projectMemberResDTO = projectMemberRepository.getProjectMember(idx);

        if (projectMemberResDTO.isEmpty()) {
            return null;
        }

        ProjectMemberEntity projectMemberEntity = projectMemberResDTO.get().toEntity();
        projectMemberEntity.memberDelete();

        return projectMemberEntity.toProjectMemberResDTO();

    }

}
