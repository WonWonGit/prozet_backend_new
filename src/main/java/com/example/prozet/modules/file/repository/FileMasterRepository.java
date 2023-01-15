package com.example.prozet.modules.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.file.domain.entity.FileMasterEntity;

public interface FileMasterRepository extends JpaRepository<FileMasterEntity, Integer>{
    
    Optional<FileMasterEntity> findByIdx(int idx);

}
