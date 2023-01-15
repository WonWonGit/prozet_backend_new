package com.example.prozet.modules.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prozet.modules.file.domain.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Integer>{
    Optional<FileEntity> findByIdx(int idx);
    Optional<FileEntity> findByFileMaster_Idx(int idx);
}
