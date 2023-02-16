package com.example.prozet.modules.projectMember.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.email.service.EmailService;
import com.example.prozet.modules.member.domain.dto.MemberDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberFindResDTO;
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

        // if (!projectEntity.getOwner().getUsername().equals(owner)) {
        // throw new CustomException(ErrorCode.PROJECT_OWNER_ONLY);
        // }

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
                .state(StateType.PENDING)
                .build();

        ProjectMemberEntity projectMemberEntityPS = projectMemberRepository.save(projectMemberEntity);

        if (projectMemberEntityPS != null) {
            // emailService.sendMail(memberEntity.getEmail(), memberEntity.getUsername(),
            // projectEntity.getOwner().getEmail(), projectMemberReqDTO.getProjectKey());

            return projectMemberEntityPS.toProjectMemberResDTO();
        }

        return null;
    }

    @Transactional
    public ProjectMemberResDTO saveProjectOwner(MemberDTO memberDTO, ProjectResDTO projectResDTO) {

        MemberEntity memberEntity = memberDTO.toMemberEntity();

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
                .access(AccessType.EDIT)
                .memberEntity(memberEntity)
                .projectEntity(projectResDTO.toEntity())
                .deleteYn("N")
                .state(StateType.ACCEPTED)
                .projectMemberType(ProjectMemberType.OWNER)
                .build();

        ProjectMemberEntity projectMemberEntityPS = projectMemberRepository.save(projectMemberEntity);

        return projectMemberEntityPS.toProjectMemberResDTO();

    }

    public Map<StateType, List<ProjectMemberFindResDTO>> getProjectMemberGruopByState(String projectKey) {

        List<ProjectMemberFindResDTO> projectMemberReqDTOs = projectMemberRepository
                .findByProjectEntity_ProjectKey(projectKey)
                .stream().filter(project -> project.getDeleteYn().equals("N"))
                .map(ProjectMemberEntity::toProjectMemberFindResDTO)
                .collect(Collectors.toList());

        Map<StateType, List<ProjectMemberFindResDTO>> projectMembers = projectMemberReqDTOs
                .stream().collect(Collectors.groupingBy(ProjectMemberFindResDTO::getState));

        return projectMembers;
    }

    public ProjectMemberResDTO getProjectMemberByIdx(Long idx) {

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_MEMBER_NOT_EXIST));

        return projectMemberEntity.toProjectMemberResDTO();
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

        // String projectKey = memberResDTO.getProjectResDTO().getProjectKey();

        // boolean isOwner = ProjectUtil.projectOwnerCheck(memberResDTO, username);

        // if (!isOwner) {
        // throw new CustomException(ErrorCode.PROJECT_OWNER_ONLY);
        // }

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
