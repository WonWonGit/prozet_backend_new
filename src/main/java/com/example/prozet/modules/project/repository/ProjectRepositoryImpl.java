package com.example.prozet.modules.project.repository;

import static com.example.prozet.modules.project.domain.entity.QProjectEntity.projectEntity;

import java.util.List;

import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ProjectListDTO> getProjectList(String username) {

        List<ProjectListDTO> results = query.select(Projections.constructor(ProjectListDTO.class,
                projectEntity.idx,
                projectEntity.projectKey,
                projectEntity.projectInformation.title,
                projectEntity.projectInformation.startDate,
                projectEntity.projectInformation.endDate,
                projectEntity.owner)).from(projectEntity)
                .where(projectEntity.owner.username.eq(username),
                        projectEntity.deleteYn.eq("N"))
                .fetch();
        return results;
    }

}
