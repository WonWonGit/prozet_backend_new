package com.example.prozet.modules.stack.repository;

import static com.example.prozet.modules.project.domain.entity.QProjectEntity.projectEntity;
import static com.example.prozet.modules.stack.domain.entity.QStackCategoryEntity.stackCategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackCategoryRepositoryImpl implements StackCategoryRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<StackCategoryFindResDTO> getStackCategory(String projectKey) {

        BooleanBuilder builder = new BooleanBuilder();

        if (stackCategoryEntity.stackType != null) {
            builder.or(stackCategoryEntity.stackType.eq(StackType.DEFAULTSTACK));
        }

        if (stackCategoryEntity.projectEntity != null) {
            builder.or(stackCategoryEntity.projectEntity.projectKey.eq(projectKey));
        }

        List<StackCategoryEntity> results = query.select(
                stackCategoryEntity).from(stackCategoryEntity)
                .leftJoin(stackCategoryEntity.projectEntity, projectEntity).fetchJoin()
                .where(builder)
                .fetch();

        List<StackCategoryFindResDTO> categoryList = results.stream()
                .map(StackCategoryEntity::toStackCategoryFindResDTO)
                .collect(Collectors.toList());

        return categoryList;
    }

}
