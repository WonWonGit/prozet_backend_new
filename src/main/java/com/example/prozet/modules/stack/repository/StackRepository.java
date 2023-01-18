package com.example.prozet.modules.stack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.stack.domain.entity.StackEntity;

public interface StackRepository extends JpaRepository<StackEntity, Long> {

    Optional<StackEntity> findByIdx(Long idx);

}
