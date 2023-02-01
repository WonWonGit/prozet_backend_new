package com.example.prozet.modules.stack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;

public interface StackCategoryRepository
        extends JpaRepository<StackCategoryEntity, Integer>, StackCategoryRepositoryCustom {
    Optional<StackCategoryEntity> findByIdx(int idx);
}
