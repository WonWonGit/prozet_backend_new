package com.example.prozet.modules.stack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;

public interface StackCategoryRepository extends JpaRepository<StackCategoryEntity, Integer> {
    Optional<StackCategoryEntity> findByIdx(int idx);
}
