package com.example.prozet.modules.stack.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.prozet.modules.stack.domain.entity.QStackEntity.stackEntity;

import static com.example.prozet.modules.project.domain.entity.QProjectEntity.projectEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackRepositoryImpl implements StackRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<StackFindResDTO> getStackList(String projectKey) {

        BooleanBuilder builder = new BooleanBuilder();

        if (stackEntity.stackType != null) {
            builder.or(stackEntity.stackType.eq(StackType.DEFAULTSTACK));
        }

        if (stackEntity.projectEntity != null) {
            builder.or(stackEntity.projectEntity.projectKey.eq(projectKey));
        }

        List<StackEntity> results = query.select(
                stackEntity).from(stackEntity)
                .leftJoin(stackEntity.projectEntity, projectEntity).fetchJoin()
                .where(builder)
                .fetch();

        List<StackFindResDTO> stackList = results.stream()
                .map(StackEntity::toStackFindResDTO)
                .collect(Collectors.toList());

        return stackList;
    }

}
