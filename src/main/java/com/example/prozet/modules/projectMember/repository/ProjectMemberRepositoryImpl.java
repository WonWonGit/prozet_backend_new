package com.example.prozet.modules.projectMember.repository;

import java.util.List;
import java.util.Optional;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberListResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.example.prozet.modules.projectMember.domain.entity.QProjectMemberEntity.projectMemberEntity;
import static com.example.prozet.modules.member.domain.entity.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryCustom {

        private final JPAQueryFactory query;

        @Override
        public ProjectMemberResDTO getInvitedMember(String projectKey, String username) {

                ProjectMemberEntity result = query.selectFrom(projectMemberEntity)
                                .where(memberEntity.username.eq(username),
                                                projectMemberEntity.projectEntity.projectKey.eq(projectKey),
                                                projectMemberEntity.deleteYn.eq("N"))
                                .fetchFirst();

                if (result == null) {
                        return null;
                }
                return result.toProjectMemberResDTO();
        }

        // 멤버 조회
        @Override
        public List<ProjectMemberListResDTO> getProjectMemberList(String projectKey) {
                List<ProjectMemberListResDTO> result = query
                                .select(Projections.constructor(ProjectMemberListResDTO.class,
                                                projectMemberEntity.idx,
                                                projectMemberEntity.access,
                                                projectMemberEntity.memberEntity.username))
                                .where(projectMemberEntity.deleteYn.eq("N"),
                                                projectMemberEntity.state.eq(StateType.ACCEPTED))
                                .from(projectMemberEntity)
                                .fetch();
                return result;
        }

        // 프로젝트 수정할 때 확인용
        @Override
        public List<ProjectMemberListResDTO> getEditProjectMemberList(String projectKey) {
                List<ProjectMemberListResDTO> result = query
                                .select(Projections.constructor(ProjectMemberListResDTO.class,
                                                projectMemberEntity.idx,
                                                projectMemberEntity.access,
                                                projectMemberEntity.memberEntity.username))
                                .where(projectMemberEntity.deleteYn.eq("N"),
                                                projectMemberEntity.projectEntity.projectKey.eq(projectKey),
                                                projectMemberEntity.state.eq(StateType.ACCEPTED),
                                                projectMemberEntity.access.eq(AccessType.EDIT))
                                .from(projectMemberEntity)
                                .fetch();
                if (result.isEmpty()) {
                        return null;
                }

                return result;
        }

        @Override
        public Optional<ProjectMemberResDTO> getProjectMember(Long idx) {

                ProjectMemberEntity result = query.selectFrom(projectMemberEntity)
                                .where(memberEntity.idx.eq(idx),
                                                projectMemberEntity.deleteYn.eq("N"))
                                .fetchFirst();

                if (result == null) {
                        return null;
                }
                return Optional.of(result.toProjectMemberResDTO());
        }

}
