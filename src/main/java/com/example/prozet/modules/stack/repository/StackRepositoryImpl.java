package com.example.prozet.modules.stack.repository;

import java.util.List;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.prozet.modules.stack.domain.entity.QStackEntity.stackEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackRepositoryImpl implements StackRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<StackFindResDTO> getStackList(String projectKey) {

        List<StackFindResDTO> results = query.select(Projections.constructor(StackFindResDTO.class,
                stackEntity.idx,
                stackEntity.name,
                stackEntity.icon,
                stackEntity.stackCategory.category)).from(stackEntity)
                .where(stackEntity.stackType.eq(StackType.DEFAULTSTACK),
                        stackEntity.projectEntity.projectKey.eq(projectKey))
                .fetch();

        return results;
    }

}
