package com.example.prozet.modules.project.repository;

import static com.example.prozet.modules.project.domain.entity.QProjectEntity.projectEntity;
import static com.example.prozet.modules.projectMember.domain.entity.QProjectMemberEntity.projectMemberEntity;
import static com.example.prozet.modules.projectInformation.domain.entity.QProjectInfoEntity.projectInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ProjectListDTO> getProjectList(String username) {

        List<ProjectListDTO> results = query
                .select(Projections.constructor(
                        ProjectListDTO.class,
                        projectMemberEntity.projectEntity.idx,
                        projectMemberEntity.projectEntity.projectKey,
                        projectMemberEntity.projectEntity.projectInformation.title,
                        projectMemberEntity.projectEntity.projectInformation.startDate,
                        projectMemberEntity.projectEntity.projectInformation.endDate,
                        projectMemberEntity.projectMemberType))
                .from(projectMemberEntity)
                .where(projectMemberEntity.memberEntity.username.eq(username))
                .fetch();

        return results;

    }

    @Override
    public ProjectResDTO getProjectByProjectKey(String projectKey) {
        // TODO Auto-generated method stub
        return null;
    }

}
