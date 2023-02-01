package com.example.prozet.modules.stack.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.prozet.modules.stack.domain.entity.QStackCategoryEntity.stackCategoryEntity;
import static com.example.prozet.modules.stack.domain.entity.QStackEntity.stackEntity;
import static com.example.prozet.modules.project.domain.entity.QProjectEntity.projectEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackCategoryRepositoryImpl implements StackCategoryRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<StackCategoryResDTO> getStackCategory(String projectKey) {

        BooleanBuilder builder = new BooleanBuilder();

        if (stackCategoryEntity.stackType != null) {
            builder.or(stackCategoryEntity.stackType.eq(StackType.DEFAULTSTACK));
        }

        if (stackCategoryEntity.projectEntity != null) {
            builder.or(stackCategoryEntity.projectEntity.projectKey.eq(projectKey));
        }

        List<StackCategoryEntity> results = query.select(
                stackCategoryEntity).from(stackCategoryEntity)
                .leftJoin(stackCategoryEntity.stacks, stackEntity).fetchJoin()
                .where(builder)
                .fetch();

        List<StackCategoryResDTO> categoryList = results.stream()
                .map(StackCategoryEntity::toStackCategoryResDTO)
                .collect(Collectors.toList());

        return categoryList;
    }

}
